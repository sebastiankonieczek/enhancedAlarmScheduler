package com.allarma.hammington

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.allarma.hammington.services.AlarmService
import com.allarma.hammington.activities.AlarmProfileOverviewActivity
import com.allarma.hammington.database.AppDatabase
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfileWithAlarms
import kotlinx.coroutines.*
import java.time.Duration
import java.time.LocalDate
import java.util.*
import kotlin.coroutines.CoroutineContext

class SetAlarmWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

   private val contentIntent by lazy {
      PendingIntent.getActivity(
         this.applicationContext,
         0,
         Intent(this.applicationContext, AlarmProfileOverviewActivity::class.java),
         PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )
   }


   override fun doWork(): Result {
      val alarmManager =
         applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

      val broadcast = PendingIntent.getForegroundService(
         applicationContext,
         0,
         Intent(applicationContext, AlarmService::class.java),
         PendingIntent.FLAG_IMMUTABLE
      )
      alarmManager?.cancel(broadcast)

      AppDatabase.invoke(applicationContext)
         .dao()
         .getActiveAlarmProfileWithAlarms()
         .stream()
         .map(AlarmProfileWithAlarms::getAlarms)
         .flatMap(MutableList<Alarm>::stream)
         .distinct()
         .filter { alarm -> alarm.frequency_ != Alarm.Frequency.UNKNOWN }
         .filter(this::isAlarmRelevant)
         .forEach { alarm -> scheduleAlarm(alarm, alarmManager, broadcast) }
      return Result.success()
   }

   private fun scheduleAlarm(
      alarm: Alarm,
      alarmManager: AlarmManager?,
      broadcast: PendingIntent?
   ) {
      val hour = alarm.getHour()
      val minute = alarm.getMinute()

      val calendar = Calendar.getInstance()
      calendar.set(Calendar.HOUR_OF_DAY, hour!!)
      calendar.set(Calendar.MINUTE, minute!!)

      if (calendar.before(Calendar.getInstance())) {
         calendar.add(Calendar.DATE, 1)
      }
      val toEpochSecond = calendar.timeInMillis

      println("set alarm $hour:$minute")

      val alarmClockInfo = AlarmManager.AlarmClockInfo(toEpochSecond, contentIntent)
      alarmManager?.setAlarmClock(alarmClockInfo, broadcast)
   }

   private fun isAlarmRelevant(alarm: Alarm): Boolean {
      return isToday(alarm.getFrequency(), alarm.getAlarmStartDate())
   }

   private fun isToday(frequency: Alarm.Frequency, startDate: LocalDate?): Boolean {
      val today = LocalDate.now()
      if(startDate?.isAfter(today) == true) {
         return false
      }

      if(frequency == Alarm.Frequency.DAILY) {
         return true
      }

      if(startDate == null) {
         return false
      }

      return when(frequency) {
         Alarm.Frequency.WEEKLY -> Duration.between(startDate, today).toDays() % 7L == 0L
         Alarm.Frequency.TWO_WEEKLY -> Duration.between(startDate, today).toDays() % 14L == 0L
         Alarm.Frequency.MONTHLY -> {
            var theDate: LocalDate = startDate
            while(theDate.isBefore(today)) {
               theDate = theDate.plusMonths(1)
            }
            return theDate.isEqual(today)
         }
         else -> false
      }
   }
}
package com.allarma.hammington

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.allarma.hammington.activities.AlarmSchedulerService
import com.allarma.hammington.database.AppDatabase
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfileWithAlarms
import kotlinx.coroutines.*
import java.time.Duration
import java.time.LocalDate
import java.util.stream.Collectors
import kotlin.coroutines.CoroutineContext

class SetAlarmWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams), CoroutineScope {
   override fun doWork(): Result {

      AppDatabase.invoke(applicationContext)
         .dao()
         .getActiveAlarmProfileWithAlarms()
         .stream()
         .map(AlarmProfileWithAlarms::getAlarms)
         .flatMap(MutableList<Alarm>::stream)
         .distinct()
         .filter { alarm -> alarm.frequency_ != Alarm.Frequency.UNKNOWN }
         .filter(this::isAlarmRelevant)
         .forEach { alarm ->
            runBlocking {
               launch {
                  val intent =
                     Intent(applicationContext, AlarmSchedulerService::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        .putExtra("hour", alarm.getHour())
                        .putExtra("minute", alarm.getMinute())
                  ContextCompat.startForegroundService(applicationContext, intent)
                  delay(200)
               }
            }
         }
      return Result.success()
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

   override val coroutineContext: CoroutineContext
      get() = Dispatchers.IO

}
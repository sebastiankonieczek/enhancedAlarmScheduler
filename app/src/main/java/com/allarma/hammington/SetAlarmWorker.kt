package com.allarma.hammington

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.allarma.hammington.database.AppDatabase
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfileWithAlarms
import java.time.Duration
import java.time.LocalDate

class SetAlarmWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
   override fun doWork(): Result {

      Handler(Looper.getMainLooper()).post {
         Toast.makeText(applicationContext, "Do something.", Toast.LENGTH_SHORT).show()
      }

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
            val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            intent.putExtra(AlarmClock.EXTRA_HOUR, alarm.getHour())
            intent.putExtra(AlarmClock.EXTRA_MINUTES, alarm.getMinute())

            applicationContext.startActivity(intent)
         }

      return Result.success()
   }

   private fun isAlarmRelevant(alarm: Alarm): Boolean {
      return isToday(alarm.getFrequency(), alarm.getAlarmStartDate())
   }

   private fun isToday(frequency: Alarm.Frequency, startDate: LocalDate?): Boolean {
      if(frequency == Alarm.Frequency.DAILY) {
         return true
      }

      if(startDate == null) {
         return false
      }

      val today = LocalDate.now()
      if(startDate.isAfter(today)) {
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
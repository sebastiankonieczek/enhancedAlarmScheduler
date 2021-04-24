package com.allarma.hammington

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class SetAlarmWorker( appContext: Context, workerParams: WorkerParameters ) : Worker( appContext, workerParams ) {
   override fun doWork(): Result {

      Handler( Looper.getMainLooper() ).post {
         Toast.makeText(applicationContext, "Do something.", Toast.LENGTH_SHORT).show()
      }

      return Result.success()
   }

}
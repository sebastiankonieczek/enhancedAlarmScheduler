package com.allarma.hammington

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class BootupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        launchAlarmWorker(context)
    }
    private fun launchAlarmWorker(context: Context) {
        println("SET_AL boot")
        val setAlarmWorker = PeriodicWorkRequestBuilder<SetAlarmWorker>( 15, TimeUnit.MINUTES ).build()

        WorkManager
            .getInstance(context)
            .cancelUniqueWork("enqueueActiveAlarms")

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork( "enqueueActiveAlarms", ExistingPeriodicWorkPolicy.KEEP, setAlarmWorker )
    }
}
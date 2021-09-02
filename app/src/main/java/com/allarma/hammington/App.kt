package com.allarma.hammington

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }



    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            ApplicationConstants.CHANNEL_ALARM_SERVICE,
            "Alarm Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}
package com.allarma.hammington.activities

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.provider.AlarmClock
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.allarma.hammington.database.AppDatabase
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfileWithAlarms
import kotlinx.coroutines.*
import java.time.Duration
import java.time.LocalDate
import kotlin.coroutines.CoroutineContext

class AlarmSchedulerService : Service() {

    private val CHANNEL_ID = "AlarmSchedulerChannel"
    private val contentIntent by lazy {
        PendingIntent.getActivity(
            this,
            0,
            Intent(this, AlarmProfileOverviewActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle( "AlarmScheduler" )
            .setSound(null)
            .setContentIntent(contentIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }
    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startForeground(1, getNotification())

        if( intent == null ) {
            return START_NOT_STICKY;
        }

        val hour = intent.extras?.getInt("hour")
        val minute = intent.extras?.getInt("minute")

        println( "set alarm $hour:$minute" )

        val setAlarm = Intent(AlarmClock.ACTION_SET_ALARM)
        setAlarm
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        setAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        setAlarm.putExtra(
            AlarmClock.EXTRA_MESSAGE,
            "set alarm $hour:$minute"
        )
        setAlarm.putExtra(AlarmClock.EXTRA_HOUR, hour)
        setAlarm.putExtra(AlarmClock.EXTRA_MINUTES, minute)

        applicationContext.startActivity(setAlarm)
        return START_NOT_STICKY;
    }

    override fun startService(service: Intent?): ComponentName? {
        return super.startService(service)
    }



    private fun getNotification(): Notification {
        notificationManager.createNotificationChannel(createChannel())
        return notificationBuilder.build();
    }

    private fun createChannel() =
        NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT )
            .apply { setSound(null, null) }

    override fun stopService(name: Intent?): Boolean {
        stopForeground(true)
        stopSelf()
        return true
    }
}
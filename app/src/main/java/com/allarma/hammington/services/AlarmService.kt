package com.allarma.hammington.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import com.allarma.hammington.ApplicationConstants
import com.allarma.hammington.activities.AlarmProfileOverviewActivity
import com.allarma.hammington.activities.R

class AlarmService : Service() {



    // replace this with correct intent
    private val contentIntent by lazy {
        PendingIntent.getActivity(
            this,
            0,
            Intent(this, AlarmProfileOverviewActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val stopAlarmIntent by lazy {
        PendingIntent.getService(
            this,
            0,
            Intent(this, AlarmService::class.java)
                .setAction(ApplicationConstants.ACTION_STOP_ALARM),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        mediaPlayer?.isLooping = true
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action?.equals(ApplicationConstants.ACTION_STOP_ALARM) == true) {
            println("stop alarm")
            stopSelf()
            return START_NOT_STICKY
        }
        println("start alarm")
        val notification = Notification
            .Builder(this, ApplicationConstants.CHANNEL_ALARM_SERVICE)
            .setContentTitle(getText(R.string.alarm_notification_title))
            .setContentText(getText(R.string.alarm_notification_title))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(contentIntent)
            .addAction(Notification.Action.Builder(Icon.createWithResource(this, R.drawable.ic_stop_alarm), "stop", stopAlarmIntent).build())
            .build()
        startForeground(1, notification)

        mediaPlayer?.start()
        vibrator?.vibrate(VibrationEffect.createWaveform( longArrayOf(0, 100, 1000), 0 ))

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer?.stop()
        vibrator?.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
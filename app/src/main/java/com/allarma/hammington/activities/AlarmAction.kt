package com.allarma.hammington.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmAction : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("set alarm received")
        Toast.makeText(context, "ALARM", Toast.LENGTH_LONG).show()
    }
}
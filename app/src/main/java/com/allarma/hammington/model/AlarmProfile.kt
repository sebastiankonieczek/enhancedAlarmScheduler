package com.allarma.hammington.model

import com.allarma.hammington.activities.SwipeHandler
import com.allarma.hammington.activities.SwipeHandlerCallback
import java.io.Serializable
import java.util.*

class AlarmProfile( name: String ) : Serializable, SwipeHandler {


    private var name_ = name
    private var active_ = false
    private var alarms_ = mutableListOf< Alarm >()

    fun getName(): String {
        return name_
    }

    fun setName( name: String ) {
        name_ = name
    }

    fun isActive(): Boolean {
        return active_
    }

    fun setActive( active: Boolean ) {
        active_ = active
    }

    fun getAlarms(): List< Alarm > {
        return alarms_.toList()
    }

    fun addAlarm( alarm: Alarm ) {
        alarms_.add( alarm )
    }

    override fun removeAt(position: Int) {
        alarms_.removeAt( position )
    }

    override fun move(from: Int, to: Int) {
        val alarm = alarms_.removeAt(from)
        alarms_.add( to, alarm )
    }

    fun clearAlarms() {
        alarms_.clear()
    }
}
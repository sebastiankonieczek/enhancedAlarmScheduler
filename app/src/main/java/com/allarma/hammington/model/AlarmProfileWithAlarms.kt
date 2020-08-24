package com.allarma.hammington.model

import androidx.room.Embedded
import androidx.room.Relation
import com.allarma.hammington.activities.SwipeHandler
import java.io.Serializable

data class AlarmProfileWithAlarms( @Embedded val alarmProfile_: AlarmProfile ) : SwipeHandler, Serializable {

    @Relation(parentColumn = "NAME_", entityColumn = "PROFILE_NAME_")
    var alarms_: MutableList<Alarm> = mutableListOf()

    override fun removeAt(position: Int) {
        alarms_.removeAt(position)
    }

    override fun move(from: Int, to: Int) {
        val alarm = alarms_.removeAt(from)
        alarms_.add(to, alarm)
    }

    fun clearAlarms() {
        alarms_.clear()
    }

    fun addAlarm(alarm: Alarm) {
        alarms_.add(alarm)
    }

    fun getAlarms(): MutableList< Alarm > = alarms_
}
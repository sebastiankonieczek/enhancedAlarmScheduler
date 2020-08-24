package com.allarma.hammington.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.allarma.hammington.activities.SwipeHandler
import com.allarma.hammington.activities.SwipeHandlerCallback
import java.io.Serializable
import java.util.*

@Entity(tableName = "ALARM_PROFILE_")
data class AlarmProfile(
    @PrimaryKey
    @ColumnInfo(name = "NAME_")
    var name_: String,
    @ColumnInfo(name = "IS_ACTIVE_")
    var active_: Boolean = false
) : Serializable {
    fun getName(): String {
        return name_
    }

    fun setName(name: String) {
        name_ = name
    }

    fun isActive(): Boolean {
        return active_
    }

    fun setActive(active: Boolean) {
        active_ = active
    }
}
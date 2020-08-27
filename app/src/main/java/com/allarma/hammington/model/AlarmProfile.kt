package com.allarma.hammington.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "ALARM_PROFILE_")
data class AlarmProfile(
    @PrimaryKey
    @ColumnInfo(name = "NAME_")
    var name_: String,
    @ColumnInfo(name = "IS_ACTIVE_")
    var active_: Boolean = false,
    @ColumnInfo( name = "ORDER_" )
    var order_: Int
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

    fun getOrder(): Int {
        return order_
    }

    fun setOrder( order: Int ) {
        order_ = order
    }

}
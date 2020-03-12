package com.allarma.hammington.model

import java.io.Serializable
import java.util.*

class Alarm : Serializable {

    enum class Frequency {
        UNKNOWN,
        DAILY,
        WEEKLY,
        TWO_WEEKLY,
        MONTHLY
    }

    enum class DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    private var hour_:Int? = null
    private var minute_:Int? = null
    private var frequency_ = Frequency.UNKNOWN
    private val selectedDays_ = mutableListOf< DayOfWeek >()
    private var alarmStartDate_: Date? = null

    fun getHour(): Int? {
        return hour_
    }

    fun setHour( hour: Int ) {
        hour_ = hour
    }

    fun getMinute(): Int? {
        return minute_
    }

    fun setMinute( minute: Int ) {
        minute_ = minute
    }

    fun getFrequency(): Frequency {
        return frequency_
    }

    fun setFrequency( frequency: Frequency ) {
        frequency_ = frequency
    }

    fun selectDay( dayOfWeek: DayOfWeek ) {
        selectedDays_.add( dayOfWeek )
    }

    fun removeDay( dayOfWeek: DayOfWeek ) {
        selectedDays_.remove( dayOfWeek )
    }

    fun getSelectedDays() : Set< DayOfWeek > {
        return selectedDays_.toSet()
    }

    fun setAlarmStartDate( alarmStartDate: Date ) {
        alarmStartDate_ = alarmStartDate
    }

    fun getAlarmStartDate() : Date? {
        return alarmStartDate_
    }

    override fun hashCode(): Int {
        return Objects.hash( hour_, minute_, frequency_, selectedDays_, alarmStartDate_ )
    }

    override fun equals(other: Any?): Boolean {
        if( other === this ) {
            return true
        }
        if( other === null ) {
            return false
        }
        if( other !is Alarm ) {
            return false
        }
        return other.hour_ == hour_
        && other.minute_ == minute_
        && other.frequency_ == frequency_
        && other.selectedDays_ == selectedDays_
        && other.alarmStartDate_ == alarmStartDate_
    }
}
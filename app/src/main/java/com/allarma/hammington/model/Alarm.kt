package com.allarma.hammington.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable
import java.time.LocalDate
import java.util.*

@Entity( tableName = "ALARM_" )
data class Alarm(@ColumnInfo(name = "PROFILE_NAME_")
                 var profileName_: String,
                 var hour_: Int? = null,
                 var minute_: Int? = null,
                 var frequency_: Frequency = Frequency.UNKNOWN,
                 val selectedDays_: MutableList< DayOfWeek > = mutableListOf(),
                 var alarmStartDate_: LocalDate? = null ) : Serializable {

    @PrimaryKey( autoGenerate = true )
    var alarmId: Long = 0

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



    fun getHour(): Int? {
        return hour_
    }

    fun setHour(hour: Int) {
        hour_ = hour
    }

    fun getMinute(): Int? {
        return minute_
    }

    fun setMinute(minute: Int) {
        minute_ = minute
    }

    fun getFrequency(): Frequency {
        return frequency_
    }

    fun setFrequency(frequency: Frequency) {
        frequency_ = frequency
    }

    fun selectDay(dayOfWeek: DayOfWeek) {
        selectedDays_.add(dayOfWeek)
    }

    fun removeDay(dayOfWeek: DayOfWeek) {
        selectedDays_.remove(dayOfWeek)
    }

    fun getSelectedDays(): Set<DayOfWeek> {
        return selectedDays_.toSet()
    }

    fun setAlarmStartDate(alarmStartDate: LocalDate) {
        alarmStartDate_ = alarmStartDate
    }

    fun getAlarmStartDate(): LocalDate? {
        return alarmStartDate_
    }

    override fun hashCode(): Int {
        return Objects.hash(hour_, minute_, frequency_, selectedDays_, alarmStartDate_)
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other === null) {
            return false
        }
        if (other !is Alarm) {
            return false
        }
        return other.hour_ == hour_
                && other.minute_ == minute_
                && other.frequency_ == frequency_
                && other.selectedDays_ == selectedDays_
                && other.alarmStartDate_ == alarmStartDate_
    }


}
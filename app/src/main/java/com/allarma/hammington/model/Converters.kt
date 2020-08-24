package com.allarma.hammington.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun frequencyToString(value: Alarm.Frequency?): String? = enumToString(value)
    @TypeConverter
    fun stringToFrequency(value: String?): Alarm.Frequency? = value?.let { Alarm.Frequency.valueOf(it ) }
    @TypeConverter
    fun dayOfWeekToString(value: Alarm.DayOfWeek?): String? = enumToString(value)
    @TypeConverter
    fun stringToDayOfWeek(value: String?): Alarm.DayOfWeek? = value?.let { Alarm.DayOfWeek.valueOf( it ) }
    @TypeConverter
    fun dayOfWeekListToString(value: List< Alarm.DayOfWeek >?): String? = value?.let { Gson().toJson( it ) }
    @TypeConverter
    fun stringToDayOfWeekList(value: String?): MutableList< Alarm.DayOfWeek >? = value?.let {
        val type = object : TypeToken<List< DayOfWeek >>() {}.type
        return mutableListOf( Gson().fromJson( it, type ) ) }
    @TypeConverter
    fun dateToString( value: LocalDate? ) : String? = value?.toString()
    @TypeConverter
    fun stringToDate( value: String? ) : LocalDate? = value?.let { LocalDate.parse( it ) }

    private fun < T: Enum<T> > enumToString( value: Enum< T >? ): String? = value?.name
}
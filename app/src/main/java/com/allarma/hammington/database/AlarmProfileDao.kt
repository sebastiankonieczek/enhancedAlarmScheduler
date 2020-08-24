package com.allarma.hammington.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfile
import com.allarma.hammington.model.AlarmProfileWithAlarms

@Dao
internal interface AlarmProfileDao {
    @Query("SELECT * FROM ALARM_PROFILE_")
    fun getAlarmProfiles(): LiveData< List<AlarmProfile> >

    @Transaction
    @Query("SELECT * FROM ALARM_PROFILE_")
    fun getAlarmProfilesWithAlarm(): List<AlarmProfileWithAlarms>

    @Transaction
    @Insert
    fun insertAll(vararg alarmProfile: AlarmProfile)

    @Query( "select * from ALARM_" )
    fun getAlarms(): List<Alarm>

    @Transaction
    @Insert
    fun insertAlarms( vararg alarm: Alarm)
}

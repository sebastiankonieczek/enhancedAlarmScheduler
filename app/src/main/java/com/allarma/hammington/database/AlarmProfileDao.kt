package com.allarma.hammington.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfile
import com.allarma.hammington.model.AlarmProfileWithAlarms

@Dao
internal interface AlarmProfileDao {
    @Query("SELECT * FROM ALARM_PROFILE_ order by ORDER_ asc")
    fun getAlarmProfiles(): LiveData< List<AlarmProfile> >

    @Transaction
    @Query("SELECT * FROM ALARM_PROFILE_")
    fun getAlarmProfilesWithAlarm(): List<AlarmProfileWithAlarms>

    @Transaction
    @Insert( onConflict = OnConflictStrategy.ABORT )
    fun insertAll(vararg alarmProfile: AlarmProfile)

    @Query( "select * from ALARM_ where PROFILE_NAME_ = :profileName" )
    fun getAlarms( profileName: String ): LiveData< List<Alarm> >

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarms( vararg alarm: Alarm)

    @Transaction
    @Delete
    fun removeProfile( vararg alarmProfile: AlarmProfile)

    @Transaction
    @Update
    fun updateAll( vararg alarm: AlarmProfile )

    @Transaction
    @Query( "delete from ALARM_ where PROFILE_NAME_ = :profileName_" )
    fun removeAlarms(profileName_: String)

    @Transaction
    fun removeProfileAndAlarms( alarmProfile: AlarmProfile ) {
        removeAlarms( alarmProfile.getName() )
        removeProfile( alarmProfile )
    }

    @Transaction
    @Query( "select * from ALARM_PROFILE_ where NAME_ = :profileName" )
    fun getAlarmProfileWithAlarms(profileName: String): AlarmProfileWithAlarms

    @Transaction
    @Query( "select * from ALARM_PROFILE_ where IS_ACTIVE_ = 1" )
    fun getActiveAlarmProfileWithAlarms(): List< AlarmProfileWithAlarms >

    @Transaction
    @Query("delete from ALARM_ where PROFILE_NAME_ = :profileName")
    fun clearAlarms(profileName: String)

    @Query("select * from ALARM_PROFILE_ where NAME_ = :profileName")
    fun getAlarmProfile(profileName: String): AlarmProfile?
}

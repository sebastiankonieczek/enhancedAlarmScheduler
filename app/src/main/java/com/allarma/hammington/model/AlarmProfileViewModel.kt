package com.allarma.hammington.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.allarma.hammington.database.AlarmProfileDao
import com.allarma.hammington.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmProfileViewModel( application: Application ) : AndroidViewModel( application )
{
    private val dao: AlarmProfileDao = AppDatabase( application ).dao()

    private val _profiles : LiveData< List< AlarmProfile > > by lazy {
        dao.getAlarmProfiles()
    }

    fun getProfiles(): LiveData< List< AlarmProfile > > {
        return _profiles
    }

    fun addProfile( alarmProfile: AlarmProfile ) {
        alarmProfile.setOrder(_profiles.value?.size?:0 )
        viewModelScope.launch( Dispatchers.IO ) {
            dao.insertAll( alarmProfile )
        }
    }

    private fun removeProfile(alarmProfile: AlarmProfile ) {
        viewModelScope.launch( Dispatchers.IO ) {
            dao.removeProfileAndAlarms(alarmProfile)
        }
    }

    fun updateAll() {
        viewModelScope.launch( Dispatchers.IO ) {
            dao.updateAll( *_profiles.value?.toTypedArray()?: emptyArray() )
        }
    }

    fun removeProfiles(posStart: Int, numItems: Int) {
        if( _profiles.value?.size?:0 <= ( posStart + numItems - 1 ) ) {
            return
        }
        for( i in 0 until numItems ) {
           removeProfile( _profiles.value?.get(posStart + i )!! )
        }
    }

    fun addAlarm(alarm_: Alarm) {
        viewModelScope.launch( Dispatchers.IO ) {
            dao.insertAlarms( alarm_ )
        }
    }

    fun getAlarms(alarmProfile: AlarmProfile): List< Alarm > {
        return dao.getAlarms( alarmProfile.getName() ).value ?: emptyList()
    }
}
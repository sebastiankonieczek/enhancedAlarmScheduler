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
    private val newItems: MutableList< AlarmProfile > = mutableListOf()

    private val _profiles : LiveData< List< AlarmProfile > > by lazy {
        loadProfiles()
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

    private fun loadProfiles() : LiveData<List<AlarmProfile>> {
        return dao.getAlarmProfiles();
    }
}
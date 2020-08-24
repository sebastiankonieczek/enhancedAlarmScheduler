package com.allarma.hammington.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.allarma.hammington.database.AlarmProfileDao
import com.allarma.hammington.database.AppDatabase

class AlarmProfileViewModel( application: Application ) : AndroidViewModel( application )
{
    private val dao: AlarmProfileDao = AppDatabase( application ).dao()

    private val profiles : MutableLiveData< List< AlarmProfile > > by lazy {
        MutableLiveData< List< AlarmProfile > >().also{
            loadProfiles()
        }
    }

    fun getProfiles(): LiveData< List< AlarmProfile > > {
        return profiles
    }

    fun addProfile( alarmProfile: AlarmProfile ) {
        dao.insertAll( alarmProfile );
    }

    private fun loadProfiles() {
        dao.getAlarmProfiles();
    }
}
package com.allarma.hammington.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allarma.hammington.database.AlarmProfileDao
import com.allarma.hammington.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileDetailsViewModel(application: Application) : AndroidViewModel(application) {
   private val appDatabase = AppDatabase(application)
   private val dao: AlarmProfileDao = appDatabase.dao()
   private lateinit var _profile: AlarmProfileWithAlarms
   private val _alarms: MutableLiveData<List<Alarm>> by lazy {
      MutableLiveData<List<Alarm>>()
   }
   private var _isNewAlarm: Boolean = false;

   fun withProfile(profileName: String) {
      runBlocking {
         _profile = viewModelScope.async(Dispatchers.IO) {
            return@async dao.getAlarmProfileWithAlarms(profileName)
         }.await()
         _alarms.value = _profile.getAlarms().sortedBy { it.getOrder() }
      }
   }

   fun newProfile(order: Int) {
      _isNewAlarm = true
      _profile = AlarmProfileWithAlarms(AlarmProfile("", false, order))
      _alarms.value = _profile.getAlarms()
   }

   fun getAlarms(): LiveData<List<Alarm>> {
      return _alarms
   }

   fun addAlarm() {
      val alarm = Alarm(_profile.alarmProfile_.name_)
      alarm.setOrder(_profile.alarms_.size - 1)
      _profile.addAlarm(alarm)

      _alarms.value = _profile.getAlarms().sortedBy { it.getOrder() }
   }

   fun setProfileName(name: String) {
      _profile.alarmProfile_.setName(name)
      _profile.alarms_.forEach { it.profileName_ = name }
   }

   fun getProfileName(): String {
      return _profile.alarmProfile_.getName()
   }

   fun removeAlarms(posStart: Int, numItems: Int) {
      if(_profile.alarms_.size <= (posStart + numItems - 1)) {
         return
      }
      for(i in 0 until numItems) {
         _profile.alarms_.removeAt(posStart + i)
      }
      _alarms.value = _profile.alarms_
   }

   fun store() {
      viewModelScope.launch(Dispatchers.IO) {
         try {
            appDatabase.runInTransaction {
               if(_isNewAlarm) {
                  dao.insertAll(_profile.alarmProfile_)
               } else {
                  dao.updateAll(_profile.alarmProfile_)
               }
               dao.clearAlarms(_profile.alarmProfile_.name_)
               dao.insertAlarms(*_profile.alarms_.toTypedArray())
            }
         } catch(e: Exception) {
            Log.w("Store" + javaClass.simpleName, "An unexpected exception occurred", e)
         }
      }
   }

   fun checkProfileName(): Boolean {
      return runBlocking {
         viewModelScope.async( Dispatchers.IO ) {
            return@async dao.getAlarmProfile( _profile.alarmProfile_.name_ ) == null
         }.await()
      }
   }
}
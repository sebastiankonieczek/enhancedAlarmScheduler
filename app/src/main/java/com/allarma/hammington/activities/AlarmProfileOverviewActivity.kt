package com.allarma.hammington.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfile
import com.allarma.hammington.model.AlarmProfileViewModel
import com.allarma.hammington.model.AlarmProfileWithAlarms

class AlarmProfileOverviewActivity : AppCompatActivity() {

    private lateinit var viewAdapter_: AlarmProfileAdapter
    private var profiles_: MutableList< AlarmProfileWithAlarms > = mutableListOf()
    private val internalOverallProfile_ = AlarmProfileWithAlarms( AlarmProfile("internal", false ) )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: AlarmProfileViewModel by viewModels()

        setContentView(R.layout.activity_alarm_profile_overview)
        viewAdapter_ = AlarmProfileAdapter( this, profiles_ )
        val viewLayoutManager =
            LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>( R.id.alarmProfiles )
        recyclerView.apply {
            setHasFixedSize( true )
            adapter = viewAdapter_
            layoutManager = viewLayoutManager
        }.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        val itemTouchHelper = ItemTouchHelper( SwipeHandlerCallback( this, viewAdapter_ ) )
        itemTouchHelper.attachToRecyclerView( recyclerView )

        val addProfile: Button = findViewById( R.id.addProfile )
        addProfile.setOnClickListener { run {
            val intent = Intent( this.applicationContext, AlarmProfileDetailActivity::class.java )
            startActivityForResult( intent, REQUEST_ADD_PROFILE )
        } }
    }

    fun mergeProfiles()
    {
        val reversedProfiles = profiles_.reversed()
        internalOverallProfile_.clearAlarms()
        val aciveAlarms = HashSet< Alarm >()
        reversedProfiles.forEach{ profile ->
            if( !profile.alarmProfile_.isActive() ) {
                return
            }
            aciveAlarms.addAll( profile.getAlarms() )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == REQUEST_ADD_PROFILE ) {
            if( data != null ) {
                val alarmProfile: AlarmProfileWithAlarms? = data.extras?.get(REQUEST_RESULT) as AlarmProfileWithAlarms
                if( alarmProfile != null ) {
                    profiles_.add(alarmProfile)
                    viewAdapter_.notifyDataSetChanged()
                }
            }
        }

        if( requestCode == REQUEST_EDIT_PROFILE ) {
            if( data != null ) {
                val alarmProfile: AlarmProfileWithAlarms? = data.extras?.get(REQUEST_RESULT) as AlarmProfileWithAlarms
                val position: Int? = data.extras!![ "POSITION" ] as Int
                if( alarmProfile != null && position != null ) {
                    profiles_[ position ] = alarmProfile
                    viewAdapter_.notifyDataSetChanged()
                }
            }
        }
    }

    companion object {
        const val REQUEST_ADD_PROFILE = 100
        const val REQUEST_EDIT_PROFILE = 200
        const val REQUEST_RESULT = "PROFILE"
    }
}

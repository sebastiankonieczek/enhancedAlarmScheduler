package com.allarma.hammington.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.util.Preconditions
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Button
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfile
import java.util.function.Consumer

class AlarmProfileOverviewActivity : AppCompatActivity() {

    private lateinit var viewAdapter_: AlarmProfileAdapter
    private val profiles_ = ArrayList< AlarmProfile >()
    private val internalOverallProfile_ = AlarmProfile( "internal" )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_profile_overview)
        viewAdapter_ = AlarmProfileAdapter( this, profiles_ )
        val viewLayoutManager = LinearLayoutManager( this )

        val recyclerView = findViewById< RecyclerView >( R.id.alarmProfiles )
        recyclerView.apply {
            setHasFixedSize( true )
            adapter = viewAdapter_
            layoutManager = viewLayoutManager
        }.addItemDecoration( DividerItemDecoration( this, LinearLayoutManager.VERTICAL ) )

        val itemTouchHelper = ItemTouchHelper( SwipeHandlerCallback( this, viewAdapter_ ) )
        itemTouchHelper.attachToRecyclerView( recyclerView )

        val addProfile: Button = findViewById( R.id.addProfile )
        addProfile.setOnClickListener { run {
            var intent = Intent( this.applicationContext, AlarmProfileDetailActivity::class.java )
            startActivityForResult( intent, REQUEST_ADD_PROFILE )
        } }
    }

    fun mergeProfiles()
    {
        var reversedProfiles = profiles_.reversed()
        internalOverallProfile_.clearAlarms()
        var aciveAlarms = HashSet< Alarm >()
        reversedProfiles.forEach{ profile ->
            if( !profile.isActive() ) {
                return
            }
            aciveAlarms.addAll( profile.getAlarms() )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == REQUEST_ADD_PROFILE ) {
            if( data != null ) {
                var alarmProfile: AlarmProfile? = data.extras[ REQUEST_RESULT ] as AlarmProfile
                if( alarmProfile != null ) {
                    profiles_.add(alarmProfile)
                    viewAdapter_.notifyDataSetChanged()
                }
            }
        }

        if( requestCode == REQUEST_EDIT_PROFILE ) {
            if( data != null ) {
                var alarmProfile: AlarmProfile? = data.extras[REQUEST_RESULT] as AlarmProfile
                var position: Int? = data.extras[ "POSITION" ] as Int
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

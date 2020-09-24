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
import com.allarma.hammington.model.AlarmProfileViewModel
import com.allarma.hammington.model.AlarmProfileWithAlarms

class AlarmProfileOverviewActivity : AppCompatActivity() {

    private lateinit var viewAdapter_: AlarmProfileAdapter
    private var profiles_: MutableList< AlarmProfileWithAlarms > = mutableListOf()
    private val model: AlarmProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_alarm_profile_overview)
        viewAdapter_ = AlarmProfileAdapter( this, model )
        viewAdapter_.registerAdapterDataObserver( object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved( posStart: Int, numItems: Int ) {
                model.removeProfiles( posStart, numItems )
            }
        })
        val viewLayoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>( R.id.alarmProfiles )
        recyclerView.apply {
            setHasFixedSize( true )
            adapter = viewAdapter_
            layoutManager = viewLayoutManager
        }.addItemDecoration( DividerItemDecoration( this, LinearLayoutManager.VERTICAL ) )

        val itemTouchHelper = ItemTouchHelper( SwipeHandlerCallback(viewAdapter_) )
        itemTouchHelper.attachToRecyclerView( recyclerView )

        val addProfile: Button = findViewById( R.id.addProfile )
        addProfile.setOnClickListener { run {
            val intent = Intent( this.applicationContext, AlarmProfileDetailActivity::class.java )
            intent.putExtra( "ORDER", viewAdapter_.itemCount )
            startActivity( intent )
        } }
    }

    fun mergeProfiles()
    {
        val reversedProfiles = profiles_.reversed()
        val activeAlarms = HashSet< Alarm >()
        reversedProfiles.forEach{ profile ->
            if( !profile.alarmProfile_.isActive() ) {
                return
            }
            activeAlarms.addAll( profile.getAlarms() )
        }
    }

    override fun onPause() {
        super.onPause()
        model.updateAll();
    }

    companion object {
        const val REQUEST_EDIT_PROFILE = 200
    }
}

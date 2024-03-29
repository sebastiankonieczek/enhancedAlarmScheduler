package com.allarma.hammington.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.allarma.hammington.SetAlarmWorker
import com.allarma.hammington.model.AlarmProfileViewModel
import java.util.concurrent.TimeUnit

class AlarmProfileOverviewActivity : AppCompatActivity() {

    private lateinit var viewAdapter_: AlarmProfileAdapter
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

        launchAlarmWorker()
    }

    private fun launchAlarmWorker() {
        val setAlarmWorker = PeriodicWorkRequestBuilder<SetAlarmWorker>( 15, TimeUnit.MINUTES ).build()

        WorkManager
            .getInstance(this)
            .cancelUniqueWork("enqueueActiveAlarms")

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork( "enqueueActiveAlarms", ExistingPeriodicWorkPolicy.KEEP, setAlarmWorker )

        WorkManager
            .getInstance(this)
            .getWorkInfosForUniqueWorkLiveData("enqueueActiveAlarms")
            .observe( this, { workInfo: List<WorkInfo> ->
                workInfo.forEach { i -> println( "" + i.runAttemptCount +  ":" + i.state ) }
            })
    }

    override fun onPause() {
        super.onPause()
        model.updateAll()
    }
}

package com.allarma.hammington.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfile
import com.allarma.hammington.model.AlarmProfileWithAlarms
import java.io.Serializable

class AlarmProfileDetailActivity : AppCompatActivity() {

    private var alarmProfile_: AlarmProfileWithAlarms? = null
    private var position_: Int? = null

    private lateinit var viewAdapter_: AlarmDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_profile_detail)

        if( intent.hasExtra( "EDIT_PROFILE" ) ) {
            alarmProfile_ = intent.extras?.get("EDIT_PROFILE") as AlarmProfileWithAlarms
            position_ = intent.extras!![ "POSITION" ] as Int
        }
        else {
            alarmProfile_ = AlarmProfileWithAlarms( AlarmProfile( "", false, 0 ) )
        }

        viewAdapter_ = AlarmDetailAdapter( this, alarmProfile_!! )
        val viewLayoutManager =
            LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>( R.id.alarmList )
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

        val alarmProfileNameView = findViewById< EditText >(R.id.profileName)
        alarmProfileNameView.setText( alarmProfile_?.alarmProfile_?.getName(), TextView.BufferType.EDITABLE )

        findViewById< Button >( R.id.accept ).setOnClickListener { run {
            if( alarmProfile_ == null ) {
                setResult( Activity.RESULT_CANCELED )
            }
            else {
                alarmProfile_?.alarmProfile_?.setName( alarmProfileNameView.text.toString() )
                val intent = Intent()
                intent.putExtra( AlarmProfileOverviewActivity.REQUEST_RESULT , alarmProfile_ as Serializable )
                intent.putExtra( "POSITION", position_ )
                setResult(Activity.RESULT_OK, intent )
            }
            finish()
        } }

        findViewById< Button >( R.id.addAlarm ).setOnClickListener {
            alarmProfile_?.addAlarm( Alarm( alarmProfile_!!.alarmProfile_.getName() ) )
            viewAdapter_.notifyDataSetChanged()
        }
    }
}

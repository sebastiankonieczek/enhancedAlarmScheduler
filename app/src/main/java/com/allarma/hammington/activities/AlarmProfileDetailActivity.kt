package com.allarma.hammington.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.allarma.hammington.model.ProfileDetailsViewModel

class AlarmProfileDetailActivity : AppCompatActivity() {

   private val model: ProfileDetailsViewModel by viewModels()

   private lateinit var viewAdapter_: AlarmDetailAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      setContentView(R.layout.activity_alarm_profile_detail)

      if(intent.hasExtra("EDIT_PROFILE")) {
         val profileName = intent.extras!!["EDIT_PROFILE"] as String
         model.withProfile(profileName)
      } else {
         val order = intent.extras?.get("ORDER") as Int
         model.newProfile(order)
      }
      viewAdapter_ = AlarmDetailAdapter(this, model)
      viewAdapter_.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
         override fun onItemRangeRemoved(posStart: Int, numItems: Int) {
            model.removeAlarms(posStart, numItems)
         }
      })

      val viewLayoutManager = LinearLayoutManager(this)

      val recyclerView = findViewById<RecyclerView>(R.id.alarmList)
      recyclerView.apply {
         setHasFixedSize(true)
         adapter = viewAdapter_
         layoutManager = viewLayoutManager
      }.addItemDecoration(
         DividerItemDecoration(
            this,
            LinearLayoutManager.VERTICAL
         )
      )

      val itemTouchHelper = ItemTouchHelper(SwipeHandlerCallback(viewAdapter_))
      itemTouchHelper.attachToRecyclerView(recyclerView)

      val alarmProfileNameView = findViewById<EditText>(R.id.profileName)
      alarmProfileNameView.setText(model.getProfileName(), TextView.BufferType.EDITABLE)

      findViewById<Button>(R.id.accept).setOnClickListener {
         run {
            model.setProfileName(alarmProfileNameView.text.toString())
            if(!model.isProfileNameValid()) {
               Toast.makeText(
                  applicationContext,
                  "This profile already exists!",
                  Toast.LENGTH_SHORT
               ).show()
               return@setOnClickListener
            }
            try {
               model.store()
               setResult(Activity.RESULT_OK, Intent())
            } finally {
               finish()
            }
         }
      }

      findViewById<Button>(R.id.addAlarm).setOnClickListener {
         model.addAlarm()
      }
   }

   override fun onBackPressed() {
      setResult(Activity.RESULT_CANCELED)
      super.onBackPressed()
   }
}

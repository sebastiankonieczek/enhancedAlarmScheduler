package com.allarma.hammington.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.allarma.hammington.model.Alarm
import com.allarma.hammington.model.AlarmProfile
import java.lang.IllegalStateException
import java.util.*

class AlarmDetailAdapter( context: AlarmProfileDetailActivity, alarmProfile: AlarmProfile ) : SwipeHandler, RecyclerView.Adapter< AlarmDetailAdapter.ViewHolder >() {
    private val alarmProfile_ = alarmProfile

    private val context_ = context
    class ViewHolder( view: View ) : RecyclerView.ViewHolder( view ) {
        val alarmSelectionMonday_ = view.findViewById< ToggleButton >( R.id.alarmSelectionMonday )
        val alarmSelectionTuesday_ = view.findViewById< ToggleButton >( R.id.alarmSelectionTuesday )
        val alarmSelectionWednesday_ = view.findViewById< ToggleButton >( R.id.alarmSelectionWednesday )
        val alarmSelectionThrusday_ = view.findViewById< ToggleButton >( R.id.alarmSelectionThursday )
        val alarmSelectionFriday_ = view.findViewById< ToggleButton >( R.id.alarmSelectionFriday )
        val alarmSelectionSaturday_ = view.findViewById< ToggleButton >( R.id.alarmSelectionSaturday )
        val alarmSelectionSunday_ = view.findViewById< ToggleButton >( R.id.alarmSelectionSunday )
        val frequencySelection_ = view.findViewById< RadioGroup >( R.id.frequencySelection )
        val setAlarmTime_ = view.findViewById< TextView >( R.id.setAlarmTime )
        val setAlarmBegin_ = view.findViewById< TextView >( R.id.setAlarmBegin )
    }
    override fun onBindViewHolder( viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        if( item.getHour() != null ) {
            viewHolder.setAlarmTime_.text = createDisplayTime( item.getHour()?: 0, item.getMinute()?: 0 )
        }
        else viewHolder.setAlarmTime_.text = null
        viewHolder.setAlarmTime_.setOnClickListener { kotlin.run {
            TimePickerDialog( context_, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                viewHolder.setAlarmTime_.text = createDisplayTime(hourOfDay, minute)
                item.setHour( hourOfDay )
                item.setMinute( minute )
            }, item.getHour() ?: 0, item.getMinute() ?: 0, true ).show()
        } }


        val current = Calendar.getInstance()
        if( item.getAlarmStartDate() != null ) {
            current.time = item.getAlarmStartDate()
            viewHolder.setAlarmBegin_.text = createDisplayDate( current.get( Calendar.DAY_OF_MONTH ), current.get( Calendar.MONTH ) + 1, current.get( Calendar.YEAR ) )
        }
        else viewHolder.setAlarmBegin_.text = null
        viewHolder.setAlarmBegin_.setOnClickListener{ kotlin.run {
            val datePicker = DatePickerDialog( context_, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                Log.println( Log.INFO,"","${dayOfMonth}${month}${year}" )
                viewHolder.setAlarmBegin_.text = createDisplayDate( dayOfMonth, month + 1, year )
                val instance = Calendar.getInstance()
                instance.set( year, month, dayOfMonth )
                item.setAlarmStartDate( instance.time )
            }, current.get( Calendar.YEAR ), current.get( Calendar.MONTH ), current.get( Calendar.DAY_OF_MONTH ) )

            val now =Calendar.getInstance()
            datePicker.datePicker.minDate = if( current > now ) now.timeInMillis else current.timeInMillis
            datePicker.show()
        } }
        bindDaySelection(item, viewHolder)
        bindFrequencySelection(item, viewHolder)
    }

    private fun createDisplayTime(hourOfDay: Int, minute: Int): String {
        val displayHour = if (hourOfDay < 10) "0${hourOfDay}" else "$hourOfDay"
        val displayMinute = if (minute < 10) "0${minute}" else "$minute"
        return "${displayHour}:${displayMinute}"
    }

    private fun createDisplayDate(dayOfMonth: Int, month: Int, year: Int ): String {
        val displayDay = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
        val displayMonth = if (month < 10) "0${month}" else "$month"
        return "${displayDay}.${displayMonth}.${year}"
    }

    private fun bindFrequencySelection(
        item: Alarm,
        viewHolder: ViewHolder
    ) {
        when (item.getFrequency()) {
            Alarm.Frequency.DAILY -> viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionDaily)
                .isChecked = true
            Alarm.Frequency.WEEKLY -> viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionWeekly)
                .isChecked = true
            Alarm.Frequency.TWO_WEEKLY -> viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionTwoWeekly)
                .isChecked = true
            Alarm.Frequency.MONTHLY -> viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionMonthly)
                .isChecked = true
            else -> {
                viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionDaily)
                    .isChecked = false
                viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionMonthly)
                    .isChecked = false
                viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionTwoWeekly)
                    .isChecked = false
                viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionWeekly)
                    .isChecked = false
            }
        }
        viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionDaily)
            .setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                run {
                    if( isChecked )
                        item.setFrequency(Alarm.Frequency.DAILY)
                }
            }
        viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionWeekly)
            .setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                run {
                    if( isChecked )
                        item.setFrequency(Alarm.Frequency.WEEKLY)
                }
            }
        viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionTwoWeekly)
            .setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                run {
                    if( isChecked )
                        item.setFrequency(Alarm.Frequency.TWO_WEEKLY)
                }
            }
        viewHolder.frequencySelection_.findViewById<RadioButton>(R.id.repeatSelectionMonthly)
            .setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                run {
                    if( isChecked )
                        item.setFrequency(Alarm.Frequency.MONTHLY)
                }
            }
    }

    private fun bindDaySelection(
        item: Alarm,
        viewHolder: ViewHolder
    ) {
        for (selectedDay in item.getSelectedDays()) {
            when (selectedDay) {
                Alarm.DayOfWeek.MONDAY -> viewHolder.alarmSelectionMonday_.isChecked = true
                Alarm.DayOfWeek.TUESDAY -> viewHolder.alarmSelectionTuesday_.isChecked = true
                Alarm.DayOfWeek.WEDNESDAY -> viewHolder.alarmSelectionWednesday_.isChecked = true
                Alarm.DayOfWeek.THURSDAY -> viewHolder.alarmSelectionThrusday_.isChecked = true
                Alarm.DayOfWeek.FRIDAY -> viewHolder.alarmSelectionFriday_.isChecked = true
                Alarm.DayOfWeek.SATURDAY -> viewHolder.alarmSelectionSaturday_.isChecked = true
                Alarm.DayOfWeek.SUNDAY -> viewHolder.alarmSelectionSunday_.isChecked = true

            }
        }

        if( item.getSelectedDays().isEmpty() ) {
            viewHolder.alarmSelectionMonday_.isChecked = false
            viewHolder.alarmSelectionTuesday_.isChecked = false
            viewHolder.alarmSelectionWednesday_.isChecked = false
            viewHolder.alarmSelectionThrusday_.isChecked = false
            viewHolder.alarmSelectionFriday_.isChecked = false
            viewHolder.alarmSelectionSaturday_.isChecked = false
            viewHolder.alarmSelectionSunday_.isChecked = false

        }

        viewHolder.alarmSelectionMonday_.setOnCheckedChangeListener( daySelectionCahngeListener( item, Alarm.DayOfWeek.MONDAY ) )
        viewHolder.alarmSelectionTuesday_.setOnCheckedChangeListener( daySelectionCahngeListener( item, Alarm.DayOfWeek.TUESDAY ) )
        viewHolder.alarmSelectionWednesday_.setOnCheckedChangeListener( daySelectionCahngeListener( item, Alarm.DayOfWeek.WEDNESDAY ) )
        viewHolder.alarmSelectionThrusday_.setOnCheckedChangeListener( daySelectionCahngeListener( item, Alarm.DayOfWeek.THURSDAY ) )
        viewHolder.alarmSelectionFriday_.setOnCheckedChangeListener( daySelectionCahngeListener( item, Alarm.DayOfWeek.FRIDAY ) )
        viewHolder.alarmSelectionSaturday_.setOnCheckedChangeListener( daySelectionCahngeListener( item, Alarm.DayOfWeek.SATURDAY ) )
        viewHolder.alarmSelectionSunday_.setOnCheckedChangeListener( daySelectionCahngeListener(item, Alarm.DayOfWeek.SUNDAY) )
    }

    private fun daySelectionCahngeListener(item: Alarm, dayOfWeek: Alarm.DayOfWeek ): (CompoundButton, Boolean) -> Unit {
        return { _: CompoundButton, checked: Boolean ->
            run {
                if( checked ) {
                    item.selectDay( dayOfWeek )
                }
                else {
                    item.removeDay( dayOfWeek )
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return alarmProfile_.getAlarms().size
    }

    override fun onCreateViewHolder( viewGroup: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from( viewGroup.context ).inflate( R.layout.alarm_detail_entry, viewGroup, false ) )
    }

    private fun getItem( position: Int ) : Alarm {
        return alarmProfile_.getAlarms()[ position ]
    }

    override fun removeAt( position: Int ) {
        if( position < 0 || position >= alarmProfile_.getAlarms().size ) {
            return
        }
        alarmProfile_.removeAt( position )
        notifyItemRemoved( position )
    }

    override fun move(from: Int, to: Int) {
        alarmProfile_.move( from, to )
        notifyItemMoved( from, to )
    }
}

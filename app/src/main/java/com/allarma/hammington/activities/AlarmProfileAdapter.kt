package com.allarma.hammington.activities

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.allarma.hammington.model.AlarmProfile
import com.allarma.hammington.model.AlarmProfileWithAlarms
import java.io.Serializable

class AlarmProfileAdapter( context: AlarmProfileOverviewActivity, items: MutableList< AlarmProfileWithAlarms > ) : RecyclerView.Adapter< AlarmProfileAdapter.ViewHolder >(), SwipeHandler {
    private val items_ = items
    private val context_ = context

    class ViewHolder( view: View ) : RecyclerView.ViewHolder( view ) {
        val profileName_: TextView = view.findViewById( R.id.profileName )
        var profileEdit_: ImageButton = view.findViewById( R.id.editProfile )
        val profileActive_: SwitchCompat = view.findViewById( R.id.profileActive )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.profileName_.text = item.alarmProfile_.getName()
        viewHolder.profileActive_.isChecked = item.alarmProfile_.isActive()
        viewHolder.profileActive_.setOnClickListener { run {
            item.alarmProfile_.setActive( viewHolder.profileActive_.isChecked )
            context_.mergeProfiles()
        } }

        viewHolder.profileEdit_.setOnClickListener { run {
            val intent = Intent( context_.applicationContext, AlarmProfileDetailActivity::class.java )
            intent.putExtra( "EDIT_PROFILE", item as Serializable )
            intent.putExtra( "POSITION", items_.indexOf(item) )

            context_.startActivityForResult( intent, AlarmProfileOverviewActivity.REQUEST_EDIT_PROFILE)
        } }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun getItem( position: Int ) : AlarmProfileWithAlarms {
        return items_[ position ]
    }

    override fun getItemCount(): Int {
        return items_.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from( p0.context ).inflate( R.layout.profile_overview_list_entry, p0, false ) )
    }

    override fun removeAt(position: Int) {
        if( position < 0 || position >= items_.size ) {
            return
        }
        items_.removeAt( position )
        notifyItemRemoved( position )
    }

    override fun move(from: Int, to: Int) {
        val item = items_.removeAt(from)
        items_.add( to, item )
        notifyItemMoved( from, to )
    }
}

package com.allarma.hammington.activities

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.allarma.hammington.model.AlarmProfile
import com.allarma.hammington.model.AlarmProfileViewModel

class AlarmProfileAdapter( context: AlarmProfileOverviewActivity, model: AlarmProfileViewModel ) : RecyclerView.Adapter< AlarmProfileAdapter.ViewHolder >(), SwipeHandler {
    private val context_ = context
    private var _viewList: MutableList< AlarmProfile > = mutableListOf()

    class ViewHolder( view: View ) : RecyclerView.ViewHolder( view ) {
        val profileName_: TextView = view.findViewById( R.id.profileName )
        var profileEdit_: ImageButton = view.findViewById( R.id.editProfile )
        val profileActive_: SwitchCompat = view.findViewById( R.id.profileActive )
    }

    init {
       model.getProfiles().observe( context, { newList ->
           _viewList = newList.toMutableList()
           updateOrder()
           notifyDataSetChanged()
       })
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.profileName_.text = item.getName()
        viewHolder.profileActive_.isChecked = item.isActive()
        viewHolder.profileActive_.setOnClickListener { run {
            item.setActive( viewHolder.profileActive_.isChecked )
        } }

        viewHolder.profileEdit_.setOnClickListener { run {
            val intent = Intent( context_.applicationContext, AlarmProfileDetailActivity::class.java )
            intent.putExtra( "EDIT_PROFILE", item.name_ )
            context_.startActivityForResult( intent, AlarmProfileOverviewActivity.REQUEST_EDIT_PROFILE)
        } }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun getItem( position: Int ) : AlarmProfile {
        return _viewList[ position ]
    }

    override fun getItemCount(): Int {
        return _viewList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from( p0.context ).inflate( R.layout.profile_overview_list_entry, p0, false ) )
    }

    override fun removeAt(position: Int) {
        if( position < 0 || position >= _viewList.size ) {
            return
        }
        notifyItemRemoved( position )
    }

    override fun move(from: Int, to: Int) {
        val item = _viewList.removeAt(from)
        _viewList.add( to, item )
        notifyItemMoved( from, to )
        updateOrder()
    }

    private fun updateOrder() {
        _viewList.forEachIndexed { index, alarmProfile -> alarmProfile.setOrder( index )  }
    }
}

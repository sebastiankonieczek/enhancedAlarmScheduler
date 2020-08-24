package com.allarma.hammington.activities

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper

class SwipeHandlerCallback( context: Context, swipeHandler: SwipeHandler ) : ItemTouchHelper.SimpleCallback( ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT ) {
    private val swipeHandler_ = swipeHandler

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeHandler_.removeAt( viewHolder.adapterPosition )
    }

    override fun onMove(view: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val fromPos = viewHolder.adapterPosition
        val toPos = target.adapterPosition
        swipeHandler_.move( fromPos, toPos )
        return true
    }
}

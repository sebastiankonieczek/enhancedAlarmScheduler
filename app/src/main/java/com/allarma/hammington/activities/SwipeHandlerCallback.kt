package com.allarma.hammington.activities

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeHandlerCallback(swipeHandler: SwipeHandler) : ItemTouchHelper.SimpleCallback( ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT ) {
    private val _swipeHandler = swipeHandler

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        _swipeHandler.removeAt( viewHolder.adapterPosition )
    }

    override fun onMove(view: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val fromPos = viewHolder.adapterPosition
        val toPos = target.adapterPosition
        _swipeHandler.move( fromPos, toPos )
        return true
    }
}

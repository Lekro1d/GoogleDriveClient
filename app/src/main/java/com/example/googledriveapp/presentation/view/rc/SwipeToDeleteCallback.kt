package com.example.googledriveapp.presentation.view.rc

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(
    private val listener: SwipeListener
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    interface SwipeListener {
        fun onItemSwipedLeft(position: Int)
        fun onItemSwipedRight(position: Int)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            listener.onItemSwipedLeft(position)
        } else if (direction == ItemTouchHelper.RIGHT) {
            listener.onItemSwipedRight(position)
        }
    }
}
package com.recyclerview_pool_controller

import androidx.recyclerview.widget.RecyclerView

class GlobalRecycledViewPool : RecyclerView.RecycledViewPool() {

    companion object {
        private const val LOG_TAG = "GlobalRecyclerViewPool"
    }

    override fun getRecycledView(viewType: Int): RecyclerView.ViewHolder? {
        return GlobalRecycledViewPoolController.getViewHolderForType(viewType)
    }

    override fun getRecycledViewCount(viewType: Int): Int {
        val count = GlobalRecycledViewPoolController.getRecycledViewCurrentCount(viewType)
        return count
    }

    override fun putRecycledView(scrap: RecyclerView.ViewHolder?) {
        val viewType = scrap?.itemViewType ?: -1
        if (viewType != -1) {
            scrap?.let { GlobalRecycledViewPoolController.putViewHolderToStack(viewType, it) }
        }
    }

}
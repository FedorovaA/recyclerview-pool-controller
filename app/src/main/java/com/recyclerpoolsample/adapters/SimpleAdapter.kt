package com.recyclerpoolsample.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recyclerpoolsample.models.SimpleModel
import com.recyclerpoolsample.viewholders.SimpleViewHolder
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController

class SimpleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ITEM = 1
    }

    var items: List<SimpleModel> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GlobalRecycledViewPoolController.getViewHolderForType(viewType, true)
            ?: object : RecyclerView.ViewHolder(View(parent.context)) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SimpleViewHolder) {
            holder.bind(items[position].text)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
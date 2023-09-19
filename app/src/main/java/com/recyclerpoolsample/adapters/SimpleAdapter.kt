package com.recyclerpoolsample.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recyclerpoolsample.databinding.SimpleItemRvBinding
import com.recyclerpoolsample.models.SimpleModel
import com.recyclerpoolsample.viewholders.SimpleViewHolder
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController

class SimpleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ITEM = 1
    }

    init {
        GlobalRecycledViewPoolController.setupViewHolderIfNeed(TYPE_ITEM) { holder ->
            (holder as? SimpleViewHolder)?.binding?.root?.background?.setTint(Color.BLUE)
        }
    }

    var items: List<SimpleModel> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GlobalRecycledViewPoolController.getViewHolderForType(viewType, true)
            ?: SimpleViewHolder(SimpleItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
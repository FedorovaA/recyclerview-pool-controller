package com.recyclerpoolsample

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recyclerpoolsample.adapters.SimpleAdapter
import com.recyclerpoolsample.databinding.SimpleItemRvBinding
import com.recyclerpoolsample.viewholders.SimpleViewHolder
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController

class ViewHolderDelegateWrapper {

    companion object {
        private const val CACHE_SIZE = 20
    }

    fun setupAsyncViewHolder(viewType: Int, view: View): RecyclerView.ViewHolder? {
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return when (viewType) {
            SimpleAdapter.TYPE_ITEM -> {
                val binding = SimpleItemRvBinding.bind(view)
                binding.root.layoutParams = lp
                val vh = SimpleViewHolder(binding)
                vh
            }
            else -> null
        }
    }

    fun setupBgViewHolder(viewType: Int, context: Context): RecyclerView.ViewHolder? {
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return when (viewType) {
            SimpleAdapter.TYPE_ITEM -> {
                val inflater = LayoutInflater.from(context)
                val binding = SimpleItemRvBinding.inflate(inflater, null, false)
                binding.root.layoutParams = lp
                val vh = SimpleViewHolder(binding)
                vh
            }
            else -> null
        }
    }

    fun getListOfVHCacheParams(): List<GlobalRecycledViewPoolController.ViewHolderCacheParams> {
        val listOfVhCacheParams = mutableListOf<GlobalRecycledViewPoolController.ViewHolderCacheParams>()
        listOfVhCacheParams.add(GlobalRecycledViewPoolController.ViewHolderCacheParams(SimpleAdapter.TYPE_ITEM, CACHE_SIZE, R.layout.simple_item_rv))
        return listOfVhCacheParams

    }

}
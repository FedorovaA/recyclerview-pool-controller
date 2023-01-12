package com.recyclerview_pool_controller.poolController

import android.content.Context
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController
import com.recyclerview_pool_controller.utils.ViewHolderUtils
import java.util.*
import java.util.concurrent.Executors

class AsyncViewHolderInitializer(val context: Context) : ViewHolderInitializer() {

    private var asyncLayoutInflater = AsyncLayoutInflater(context)

    private val executor = Executors.newFixedThreadPool(1)

    override fun initialize(params: List<GlobalRecycledViewPoolController.ViewHolderCacheParams>,
                            createVhDelegate: GlobalRecycledViewPoolController.CreateViewHolderDelegate,
                            callback: (Stack<RecyclerView.ViewHolder>, Int, Int) -> Unit) {
        params.forEach {vhCacheParams ->
            val stack = Stack<RecyclerView.ViewHolder>()
            val viewType = vhCacheParams.viewType
            val layoutId = vhCacheParams.layoutId
            val maxCacheSize = vhCacheParams.cacheSize
            for (i in 1..maxCacheSize) {
                layoutId?.let { id ->
                    asyncLayoutInflater.inflate(id, null) {view, resid, parent ->
                        executor.execute {
                            val viewHolder = createVhDelegate.createViewHolderWithAsyncInflater(viewType, view)
                            viewHolder?.let { ViewHolderUtils.setItemViewTypeVh(it, viewType) }
                            stack.push(viewHolder)
                        }
                    }
                }
            }
            callback(stack, maxCacheSize, viewType)
        }
    }
}
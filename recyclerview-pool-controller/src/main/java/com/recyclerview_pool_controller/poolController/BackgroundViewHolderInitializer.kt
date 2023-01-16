package com.recyclerview_pool_controller.poolController

import androidx.recyclerview.widget.RecyclerView
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController
import com.recyclerview_pool_controller.utils.ViewHolderUtils
import java.util.*
import java.util.concurrent.Executors

class BackgroundViewHolderInitializer : ViewHolderInitializer() {

    private val executor = Executors.newFixedThreadPool(1)

    override fun initialize(
        params: List<GlobalRecycledViewPoolController.ViewHolderCacheParams>,
        createVhDelegate: GlobalRecycledViewPoolController.CreateViewHolderDelegate,
        processCallback: (Stack<RecyclerView.ViewHolder>, Int, Int) -> Unit,
        finishCallback: () -> Unit
    ) {
        executor.execute {
            params.forEach { vhCacheParams ->
                val stack = Stack<RecyclerView.ViewHolder>()
                val viewType = vhCacheParams.viewType
                val maxCacheSize = vhCacheParams.cacheSize
                for (i in 1..maxCacheSize) {
                    val viewHolder = createVhDelegate.createViewHolderInBackground(viewType)
                    viewHolder?.let { ViewHolderUtils.setItemViewTypeVh(it, viewType) }
                    stack.push(viewHolder)
                }
                processCallback(stack, maxCacheSize, viewType)
            }
            finishCallback()
        }
    }
}
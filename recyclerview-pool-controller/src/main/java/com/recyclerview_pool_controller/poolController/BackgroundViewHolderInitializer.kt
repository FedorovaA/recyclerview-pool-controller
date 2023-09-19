package com.recyclerview_pool_controller.poolController

import androidx.recyclerview.widget.RecyclerView
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController
import com.recyclerview_pool_controller.utils.ViewHolderUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class BackgroundViewHolderInitializer : ViewHolderInitializer() {

    override fun initialize(
        params: List<GlobalRecycledViewPoolController.ViewHolderCacheParams>,
        createVhDelegate: GlobalRecycledViewPoolController.CreateViewHolderDelegate,
        processCallback: (Stack<RecyclerView.ViewHolder>, Int, Int) -> Unit,
        completionCallback: () -> Unit
    ) {
        job?.cancel()
        job = coroutineScope.launch {
            params.forEach { vhCacheParams ->
                val stack = Stack<RecyclerView.ViewHolder>()
                val viewType = vhCacheParams.viewType
                val maxCacheSize = vhCacheParams.cacheSize
                if (!isActive) return@launch
                for (i in 1..maxCacheSize) {
                    if (isActive) {
                        val viewHolder = createVhDelegate.createViewHolderInBackground(viewType)
                        viewHolder?.let { ViewHolderUtils.setItemViewTypeVh(it, viewType) }
                        stack.push(viewHolder)
                    }
                }
                withContext(Dispatchers.Main) {
                    processCallback(stack, maxCacheSize, viewType)
                }
            }
            withContext(Dispatchers.Main) {
                completionCallback()
            }
        }
    }

    override fun cancel() {
        super.cancel()
    }

}
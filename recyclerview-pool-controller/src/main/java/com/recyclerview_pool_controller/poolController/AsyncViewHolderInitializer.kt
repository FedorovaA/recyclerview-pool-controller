package com.recyclerview_pool_controller.poolController

import android.content.Context
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController
import com.recyclerview_pool_controller.utils.ViewHolderUtils
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

class AsyncViewHolderInitializer(val context: Context) : ViewHolderInitializer() {

    private var asyncLayoutInflater: AsyncLayoutInflater? = AsyncLayoutInflater(context)

    override fun initialize(
        params: List<GlobalRecycledViewPoolController.ViewHolderCacheParams>,
        createVhDelegate: GlobalRecycledViewPoolController.CreateViewHolderDelegate,
        processCallback: (Stack<RecyclerView.ViewHolder>, Int, Int) -> Unit,
        completionCallback: () -> Unit
    ) {
        job?.cancel()
        job = coroutineScope.launch {
            params.forEachIndexed { index, vhCacheParams ->
                val stack = Stack<RecyclerView.ViewHolder>()
                val viewType = vhCacheParams.viewType
                val layoutId = vhCacheParams.layoutId
                val maxCacheSize = vhCacheParams.cacheSize
                if (!isActive) return@forEachIndexed
                for (i in 1..maxCacheSize) {
                    if (isActive) {
                        layoutId?.let { id ->
                            asyncLayoutInflater?.inflate(id, null) {view, resid, parent ->
                                val viewHolder = createVhDelegate.createViewHolderWithAsyncInflater(viewType, view)
                                viewHolder?.let { ViewHolderUtils.setItemViewTypeVh(it, viewType) }
                                stack.push(viewHolder)
                                if (stack.size == maxCacheSize) {
                                    processCallback(stack, maxCacheSize, viewType)
                                    if (index == params.lastIndex) {
                                        completionCallback()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun cancel() {
        super.cancel()
        asyncLayoutInflater = null
    }
}
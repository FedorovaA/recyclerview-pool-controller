package com.recyclerview_pool_controller.poolController

import androidx.recyclerview.widget.RecyclerView
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*

abstract class ViewHolderInitializer {

    protected val coroutineScope = CoroutineScope(Dispatchers.Default)
    protected var job: Job? = null

    abstract fun initialize(params: List<GlobalRecycledViewPoolController.ViewHolderCacheParams>,
                            createVhDelegate: GlobalRecycledViewPoolController.CreateViewHolderDelegate,
                            processCallback: (Stack<RecyclerView.ViewHolder>, Int, Int) -> Unit,
                            completionCallback: () -> Unit)

    open fun cancel() {
        job?.cancel()
        job = null
    }

}
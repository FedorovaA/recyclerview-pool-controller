package com.recyclerview_pool_controller.poolController

import androidx.recyclerview.widget.RecyclerView
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController
import java.util.*

abstract class ViewHolderInitializer {

    abstract fun initialize(params: List<GlobalRecycledViewPoolController.ViewHolderCacheParams>,
                            createVhDelegate: GlobalRecycledViewPoolController.CreateViewHolderDelegate,
                            callback: (Stack<RecyclerView.ViewHolder>, Int, Int) -> Unit)

}
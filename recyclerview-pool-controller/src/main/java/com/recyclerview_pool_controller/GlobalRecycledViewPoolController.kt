package com.recyclerview_pool_controller

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.recyclerview_pool_controller.poolController.ViewHolderInitializer
import java.util.*

object GlobalRecycledViewPoolController {

    const val LOG_TAG = "RvPerfExtension"

    var isInitialized = false
        private set

    private var mapOfStackVh = mutableMapOf<Int, Stack<RecyclerView.ViewHolder>>()
    private var mapOfMaxSizeCacheVh = mutableMapOf<Int, Int>()
    private var createVhDelegate: CreateViewHolderDelegate? = null
    private var initializer: ViewHolderInitializer? = null

    @Throws(IllegalStateException::class)
    fun initialize(params: List<ViewHolderCacheParams>,
                   initializer: ViewHolderInitializer,
                   createVhDelegate: CreateViewHolderDelegate,
                   completionCallback: (() -> Unit)? = null) {
        if (!checkDoubleViewType(params)) {
            this.createVhDelegate = createVhDelegate
            this.initializer = initializer
            initializer.initialize(params, createVhDelegate,
                processCallback = { stack, maxCacheSize, viewType ->
                    mapOfStackVh[viewType] = stack
                    mapOfMaxSizeCacheVh[viewType] = maxCacheSize
                }, completionCallback = {
                    isInitialized = true
                    completionCallback?.invoke()
                }
            )
        } else {
            throw IllegalStateException("Duplicate ViewHolder type")
        }
    }

    fun destroy() {
        initializer?.cancel()
        for ((k, v) in mapOfStackVh) {
            v.clear()
        }
        mapOfStackVh.clear()
        mapOfMaxSizeCacheVh.clear()
        initializer = null
        createVhDelegate = null
    }

    fun getViewHolderForType(viewType: Int, isFromCreateViewHolder: Boolean = false): RecyclerView.ViewHolder? {
        return if (isInitialized) {
            try {
                val stack = mapOfStackVh[viewType]
                stack?.pop()
            } catch (ex: EmptyStackException) {
                if (isFromCreateViewHolder) {
                    createVhDelegate?.createViewHolderInBackground(viewType)
                } else {
                    null
                }
            }
        } else {
            null
        }
    }

    fun getRecycledViewCurrentCount(viewType: Int): Int {
        return if (isInitialized) {
            mapOfStackVh[viewType]?.size ?: 0
        } else {
            0
        }
    }

    fun putViewHolderToStack(viewType: Int, vh: RecyclerView.ViewHolder) {
        require(isInitialized)
        mapOfStackVh[viewType]?.push(vh)
    }

    //If you need set once to vh
    //you can call this method when adapter is init
    //but after GlobalRecycledViewPoolController.initialize
    fun setupViewHolderIfNeed(viewType: Int, callback: (RecyclerView.ViewHolder) -> Unit) {
        if (isInitialized) {
            val stack = mapOfStackVh[viewType]
            stack?.map {
                callback.invoke(it)
            }
            if (stack != null) {
                mapOfStackVh[viewType] = stack
            }
        }
    }

    private fun checkDoubleViewType(params: List<ViewHolderCacheParams>): Boolean {
        val mapOfViewType = mutableMapOf<Int, Boolean>()
        var result = false
        params.forEach {
            if (!mapOfViewType.containsKey(it.viewType)){
                mapOfViewType[it.viewType] = true
            } else {
                result = true
            }
        }
        return result
    }

    data class ViewHolderCacheParams(val viewType: Int, val cacheSize: Int, val layoutId: Int? = null)

    interface CreateViewHolderDelegate {

        fun createViewHolderInBackground(viewType: Int): RecyclerView.ViewHolder? { return null }

        fun createViewHolderWithAsyncInflater(viewType: Int, view: View): RecyclerView.ViewHolder? { return null }

    }
}
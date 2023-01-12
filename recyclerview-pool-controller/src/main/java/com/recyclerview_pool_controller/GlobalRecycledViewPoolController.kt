package com.recyclerview_pool_controller

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.recyclerview_pool_controller.poolController.ViewHolderInitializer
import java.util.*

object GlobalRecycledViewPoolController {

    const val LOG_TAG = "RvPerfExtension"

    private var isInitialized = false
    private var mapOfStackVh = mutableMapOf<Int, Stack<RecyclerView.ViewHolder>>()
    private var mapOfMaxSizeCacheVh = mutableMapOf<Int, Int>()
    private lateinit var createVhDelegate: CreateViewHolderDelegate

    fun initialize(params: List<ViewHolderCacheParams>,
                   initializer: ViewHolderInitializer,
                   createVhDelegate: CreateViewHolderDelegate) {
        if (!checkDoubleViewType(params)) {
            isInitialized = true
            this.createVhDelegate = createVhDelegate
            initializer.initialize(params, createVhDelegate) { stack, maxCacheSize, viewType ->
                mapOfStackVh[viewType] = stack
                mapOfMaxSizeCacheVh[viewType] = maxCacheSize
            }
        } else {
            throw IllegalStateException("Duplicate ViewHolder type")
        }
    }

    fun getViewHolderForType(viewType: Int, isFromCreateViewHolder: Boolean = false): RecyclerView.ViewHolder? {
        require(isInitialized)
        return try {
            val stack = mapOfStackVh[viewType]
            stack?.pop()
        } catch (ex: EmptyStackException) {
            if (isFromCreateViewHolder) {
                createVhDelegate.createViewHolderInBackground(viewType)
            } else {
                null
            }
        }
    }

    fun getRecycledViewCurrentCount(viewType: Int): Int {
        require(isInitialized)
        return mapOfStackVh[viewType]?.size ?: 0
    }

    fun putViewHolderToStack(viewType: Int, vh: RecyclerView.ViewHolder) {
        require(isInitialized)
        mapOfStackVh[viewType]?.push(vh)
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
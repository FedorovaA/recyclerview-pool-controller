package com.recyclerpoolsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recyclerpoolsample.adapters.SimpleAdapter
import com.recyclerpoolsample.databinding.ActivityMainBinding
import com.recyclerpoolsample.models.SimpleModel
import com.recyclerview_pool_controller.GlobalRecycledViewPool
import com.recyclerview_pool_controller.GlobalRecycledViewPoolController
import com.recyclerview_pool_controller.poolController.AsyncViewHolderInitializer

class MainActivity : AppCompatActivity() {

    private lateinit var activityBinding: ActivityMainBinding

    private lateinit var adapter: SimpleAdapter

    //made singleton
    private val viewHolderDelegateWrapper = ViewHolderDelegateWrapper()

    //made singleton
    private val globalPool: GlobalRecycledViewPool = GlobalRecycledViewPool()

    private val createVhDelegate = object : GlobalRecycledViewPoolController.CreateViewHolderDelegate {
        // simple creation
        override fun createViewHolderWithAsyncInflater(viewType: Int, view: View): RecyclerView.ViewHolder? {
            return viewHolderDelegateWrapper.setupAsyncViewHolder(viewType, view)
        }
//        override fun createViewHolderInBackground(viewType: Int): RecyclerView.ViewHolder? {
//            return viewHolderDelegateWrapper.setupBgViewHolder(viewType, this@MainActivity)
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activityBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)
        setContentView(activityBinding.root)
        GlobalRecycledViewPoolController.initialize(viewHolderDelegateWrapper.getListOfVHCacheParams(), AsyncViewHolderInitializer(this), createVhDelegate) {
        }
        adapter = SimpleAdapter()
        activityBinding.recyclerView.adapter = adapter
        activityBinding.recyclerView.setRecycledViewPool(globalPool)
        activityBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        setupAdapter()
    }

    private fun setupAdapter() {
        val items = mutableListOf<SimpleModel>()
        for (i in 0..39) {
            items.add(SimpleModel("$i in List"))
        }
        adapter.items = items
    }

    override fun onDestroy() {
        GlobalRecycledViewPoolController.destroy()
        super.onDestroy()
    }
}
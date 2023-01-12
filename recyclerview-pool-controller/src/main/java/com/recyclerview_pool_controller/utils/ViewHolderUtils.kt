package com.recyclerview_pool_controller.utils

import androidx.recyclerview.widget.RecyclerView

object ViewHolderUtils {

    fun setItemViewTypeVh(viewHolder: RecyclerView.ViewHolder, viewType: Int) {
        return try {
            val itemViewType = RecyclerView.ViewHolder::class.java.getDeclaredField("mItemViewType")
            itemViewType.isAccessible = true
            itemViewType.setInt(viewHolder, viewType)
        } catch (ex: NoSuchFieldException) {
            ex.printStackTrace()
        } catch (ex: IllegalAccessException) {
            ex.printStackTrace()
        }
    }
}
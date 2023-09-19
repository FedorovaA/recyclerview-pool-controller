package com.recyclerpoolsample.viewholders

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.recyclerpoolsample.databinding.SimpleItemRvBinding

class SimpleViewHolder(val binding: SimpleItemRvBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String) {
        binding.root.updateLayoutParams<ViewGroup.LayoutParams> {
            height = 300
        }
        binding.text.setText(text)
    }
}
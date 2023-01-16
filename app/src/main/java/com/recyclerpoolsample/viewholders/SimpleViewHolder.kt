package com.recyclerpoolsample.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.recyclerpoolsample.databinding.SimpleItemRvBinding

class SimpleViewHolder(val binding: SimpleItemRvBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String) {
        binding.text.setText(text)
    }
}
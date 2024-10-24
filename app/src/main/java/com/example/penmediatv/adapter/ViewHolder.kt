package com.example.penmediatv.adapter

import androidx.core.util.Consumer
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.databinding.ItemDeleteBinding
import com.example.penmediatv.databinding.ItemKeyboardBinding

class KeyboardViewHolder(private val binding: ItemKeyboardBinding) :
    BaseViewHolder(binding.root) {
    fun bind(text: String, consumer: Consumer<String>) {
        binding.key.text = text
        binding.key.setOnClickListener { consumer.accept(text) }
    }

}

class DeleteViewHolder(private val binding: ItemDeleteBinding) :
    BaseViewHolder(binding.root) {
    fun bind(function: () -> Unit) {
        binding.btnDelete.setOnClickListener { function() }
    }

}

abstract class BaseViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView)

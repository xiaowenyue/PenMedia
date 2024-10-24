package com.example.penmediatv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.databinding.ItemKeyboardBinding

abstract class AlphabetAdapter :
    RecyclerView.Adapter<KeyboardViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KeyboardViewHolder {
        val binding =
            ItemKeyboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeyboardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 26
    }

    override fun onBindViewHolder(holder: KeyboardViewHolder, position: Int) {
        val text = ('A' + position).toString()
        holder.bind(text) { onClick(it) }
    }

    abstract fun onClick(text: String)
}
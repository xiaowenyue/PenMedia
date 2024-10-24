package com.example.penmediatv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.databinding.ItemDeleteBinding
import com.example.penmediatv.databinding.ItemKeyboardBinding

abstract class NumberAdapter :
    RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        const val VIEW_TYPE_DELETE = 0
        const val VIEW_TYPE_NUMBER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 10) VIEW_TYPE_DELETE else VIEW_TYPE_NUMBER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_DELETE -> {
                val binding =
                    ItemDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DeleteViewHolder(binding)
            }

            VIEW_TYPE_NUMBER -> {
                val binding =
                    ItemKeyboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                KeyboardViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return 11
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val text = ((11 + position) % 10).toString()
        when (holder) {
            is KeyboardViewHolder -> holder.bind(text) { onClickNumber(it) }
            is DeleteViewHolder -> holder.bind{ onDelete() }
        }
    }

    abstract fun onClickNumber(text: String)
    abstract fun onDelete()
}

package com.example.penmediatv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = GridLayoutManager(context,3)
        val movieList = listOf(
            Movie("Movie 1", R.drawable.movie),
            Movie("Movie 2", R.drawable.movie),
            Movie("Movie 3", R.drawable.movie),
            Movie("Movie 4", R.drawable.movie),
            Movie("Movie 5", R.drawable.ic_search),
            Movie("Movie 6", R.drawable.ic_history),
        )
        binding.recyclerView.adapter = HistoryAdapter(movieList)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.post{
            binding.recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.requestFocus()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
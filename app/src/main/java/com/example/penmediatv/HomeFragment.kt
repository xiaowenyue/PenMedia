package com.example.penmediatv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        binding.recyclerView.adapter = MovieAdapter(getMovies())

        binding.cv0.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv0.strokeWidth = 6
                binding.cv0.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                binding.llContent0.visibility = View.VISIBLE
            } else {
                binding.cv0.strokeWidth = 0
                binding.llContent0.visibility = View.GONE
            }
        }
        binding.cv1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv1.strokeWidth = 6
                binding.cv1.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
                binding.llContent1.visibility = View.VISIBLE
            } else {
                binding.cv1.strokeWidth = 0
                binding.llContent1.visibility = View.GONE
            }
        }
        binding.cv2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv2.strokeWidth = 6
                binding.cv2.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
            } else {
                binding.cv2.strokeWidth = 0
            }
        }
        binding.cv3.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv3.strokeWidth = 6
                binding.cv3.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)

            } else {
                binding.cv3.strokeWidth = 0
            }
        }
        binding.cv4.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv4.strokeWidth = 6
                binding.cv4.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)

            } else {
                binding.cv4.strokeWidth = 0
            }
        }
        binding.cv5.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.cv5.strokeWidth = 6
                binding.cv5.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)

            } else {
                binding.cv5.strokeWidth = 0
            }
        }
    }

    private fun getMovies(): List<Movie> {
        // Generate dummy movie data
        return listOf(
            Movie("Movie 1", R.drawable.movie),
            Movie("Movie 2", R.drawable.ic_search),
            Movie("Movie 3", R.drawable.ic_history),
            Movie("Movie 4", R.drawable.ic_mine),
            Movie("Movie 5", R.drawable.ic_search),
            Movie("Movie 6", R.drawable.ic_history),
            Movie("Movie 7", R.drawable.ic_mine),
            Movie("Movie 8", R.drawable.ic_search),
            Movie("Movie 9", R.drawable.ic_history),
            Movie("Movie 10", R.drawable.ic_mine),
            Movie("Movie 11", R.drawable.ic_search),
            Movie("Movie 12", R.drawable.ic_history),
            Movie("Movie 13", R.drawable.ic_mine),
            Movie("Movie 14", R.drawable.ic_search),
            Movie("Movie 15", R.drawable.ic_history),
            Movie("Movie 16", R.drawable.ic_mine)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


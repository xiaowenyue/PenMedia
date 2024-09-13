package com.example.penmediatv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.imageView.setOnClickListener(View.OnClickListener {})
        binding.imageView.setOnFocusChangeListener{ _, hasFocus ->

        }
        binding.imageView2.setOnClickListener {
            // Handle image click event
        }
        binding.imageView3.setOnClickListener {
            // Handle image click event
        }
        binding.imageView4.setOnClickListener {
            // Handle image click event
        }
        binding.imageView5.setOnClickListener {
            // Handle image click event
        }
        binding.imageView5.setOnFocusChangeListener{_, hasFocus ->
            if (hasFocus){
                binding.imageView5.setImageResource(R.color.purple_200)
            } else {
                binding.imageView5.setImageResource(R.color.white)
            }
        }
        binding.imageView6.setOnClickListener {
            // Handle image click event
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        binding.recyclerView.adapter = MovieAdapter(getMovies())
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


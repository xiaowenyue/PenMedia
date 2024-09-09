package com.example.penmediatv

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.penmediatv.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    val details: String =
        "1991年，香港黑帮三合会会员刘健明听从老大韩琛的吩咐，加入警察部队成为黑帮卧底，韩琛许诺刘健明会帮其在七年后晋升为见习督察。1992年，警察训练…. 更多"
    private val popularMovies = listOf(
        Movie("无双", R.drawable.movie, "电影描述..."),
        Movie("扫毒", R.drawable.movie, "电影描述..."),
        Movie("无双", R.drawable.movie, "电影描述..."),
        Movie("扫毒", R.drawable.movie, "电影描述..."),
        Movie("无双", R.drawable.movie, "电影描述..."),
        Movie("扫毒", R.drawable.movie, "电影描述..."),
        Movie("无双", R.drawable.movie, "电影描述...")
    )

    private val searchResults = listOf(
        Movie("无双", R.drawable.movie, details),
        Movie("无双", R.drawable.movie, details),
        Movie("无双", R.drawable.movie, details),
        Movie("无双", R.drawable.movie, details),
        Movie("无双", R.drawable.movie, "电影描述..."),
        Movie("扫毒", R.drawable.movie, "电影描述..."),
        // 添加更多电影数据
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupKeyboard()
        setupPopularMoviesList()
        setupSearchResults()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                searchMovies(query)
            }

            // 其他TextWatcher方法可以留空
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupKeyboard() {
        val buttons = listOf<Button>(
            binding.keyA,
            // 继续添加其他键盘按钮
        )

        for (button in buttons) {
            button.setOnClickListener {
                val currentText = binding.etSearch.text.toString()
                binding.etSearch.setText(currentText + button.text)
            }
        }
    }

    private fun setupPopularMoviesList() {
        popularMoviesAdapter = PopularMoviesAdapter(popularMovies)
        binding.popularMoviesList.layoutManager = LinearLayoutManager(context)
        binding.popularMoviesList.adapter = popularMoviesAdapter
    }

    private fun setupSearchResults() {
        searchResultsAdapter = SearchResultsAdapter(searchResults)
        binding.searchResults.layoutManager = LinearLayoutManager(context)
        binding.searchResults.adapter = searchResultsAdapter
    }

    private fun searchMovies(query: String) {
        // 假设这里是调用API来获取查询结果，代码只是演示
        val results = listOf(
            Movie("搜索结果1", R.drawable.movie, "描述..."),
            // 添加更多搜索结果
        )

        (searchResults as ArrayList<Movie>).clear()
        searchResults.addAll(results)
        searchResultsAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

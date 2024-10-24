package com.example.penmediatv

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.penmediatv.API.SearchApi
import com.example.penmediatv.Data.SearchRequest
import com.example.penmediatv.Data.SearchResponse
import com.example.penmediatv.Data.TrendRecommendItem
import com.example.penmediatv.Data.TrendRecommendResponse
import com.example.penmediatv.adapter.AlphabetAdapter
import com.example.penmediatv.adapter.NumberAdapter
import com.example.penmediatv.databinding.FragmentSearchBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var alphabetAdapter: AlphabetAdapter
    private lateinit var numberAdapter: NumberAdapter
    private var searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private var currentPage = 1
    private var totalPages = 1
    private var pageSize = 7
    private var isLoading = false

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
        fetchTrendingRecommend()

        // 搜索框监听
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    currentPage = 1 // 重置页码
                    searchMovies(query)
                }
                searchHandler.postDelayed(searchRunnable!!, 500)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.popularMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 检查是否到达底部
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // 如果到达底部，并且按下向下按钮
                if (lastVisibleItemPosition == 6) {
                    recyclerView.setOnKeyListener { _, keyCode, event ->
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.action == KeyEvent.ACTION_DOWN) {
                            // 将焦点设置到 cvMore 按钮
                            binding.cvMore.requestFocus()
                            true
                        } else {
                            false
                        }
                    }
                }
            }
        })
        // 设置查看更多按钮点击事件
        binding.cvMore.setOnClickListener {
            currentPage++ // 加载下一页
            searchMovies(binding.etSearch.text.toString(), isLoadMore = true) // 加载下一页数据
        }
    }

    private fun fetchTrendingRecommend() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val searchApi = retrofit.create(SearchApi::class.java)
        val call = searchApi.getTrendingRecommend()
        call.enqueue(object : retrofit2.Callback<TrendRecommendResponse> {
            override fun onResponse(
                call: retrofit2.Call<TrendRecommendResponse>,
                response: retrofit2.Response<TrendRecommendResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.data
                    // 处理电影数据
                    if (movies != null) {
                        setupPopularMoviesList(movies)
                        setupSearchResults(movies)
                        binding.cvMore.visibility =
                            if (movies.size >= 7) View.VISIBLE else View.GONE
                    }
                } else {
                    // 处理错误
                    ErrorHandler.handleUnsuccessfulResponse(
                        binding.root.context,
                        this::class.java.simpleName
                    )
                }
            }

            override fun onFailure(call: retrofit2.Call<TrendRecommendResponse>, t: Throwable) {
                // 处理错误
                ErrorHandler.handleFailure(
                    t,
                    binding.root.context,
                    this::class.java.simpleName
                )
            }
        })
    }

    private fun setupKeyboard() {
        binding.alphabet.layoutManager = GridLayoutManager(context, 6)
        val layoutManager = GridLayoutManager(context, 6)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == 10) {
                    return 2
                }
                return 1
            }
        }
        binding.number.layoutManager = layoutManager
        alphabetAdapter = object : AlphabetAdapter() {
            override fun onClick(text: String) {
                val currentText = binding.etSearch.text.toString()
                binding.etSearch.setText(currentText + text)
            }
        }
        binding.alphabet.adapter = alphabetAdapter
        numberAdapter = object : NumberAdapter() {
            override fun onClickNumber(text: String) {
                val currentText = binding.etSearch.text.toString()
                binding.etSearch.setText(currentText + text)
            }

            override fun onDelete() {
                val currentText = binding.etSearch.text.toString()
                binding.etSearch.text = if (currentText.isNotEmpty()) TextUtils.concat(
                    currentText.substring(0, currentText.length - 1)
                ) else null
            }
        }
        binding.number.adapter = numberAdapter
    }

    private fun setupPopularMoviesList(nameList: List<TrendRecommendItem>) {
        popularMoviesAdapter = PopularMoviesAdapter(nameList.toMutableList())
        binding.popularMoviesList.layoutManager = LinearLayoutManager(context)
        binding.popularMoviesList.adapter = popularMoviesAdapter
    }

    private fun setupSearchResults(detailsList: List<TrendRecommendItem>) {
        searchResultsAdapter = SearchResultsAdapter(detailsList.toMutableList())
        binding.searchResults.layoutManager = LinearLayoutManager(context)
        binding.searchResults.adapter = searchResultsAdapter
    }

    private fun searchMovies(query: String, isLoadMore: Boolean = false) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val searchApi = retrofit.create(SearchApi::class.java)

        val searchRequest = SearchRequest(page = currentPage, pageSize = pageSize, searchList = listOf(query))
        val call = searchApi.search(searchRequest)

        call.enqueue(object : retrofit2.Callback<SearchResponse> {
            override fun onResponse(
                call: retrofit2.Call<SearchResponse>,
                response: retrofit2.Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.data?.records
                    if (movies != null) {
                        // 加载新页的数据
                        updateMovieLists(movies, isLoadMore)
                    }
                } else {
                    ErrorHandler.handleUnsuccessfulResponse(
                        binding.root.context,
                        this::class.java.simpleName
                    )
                }
            }

            override fun onFailure(call: retrofit2.Call<SearchResponse>, t: Throwable) {
                ErrorHandler.handleFailure(t, binding.root.context, this::class.java.simpleName)
            }
        })
    }

    private fun updateMovieLists(movies: List<TrendRecommendItem>, isLoadMore: Boolean = false) {
        if (isLoadMore) {
            // 只更新新请求的七条数据
            popularMoviesAdapter.updateMovies(movies)  // 更新热门影片列表
            searchResultsAdapter.updateMovies(movies)  // 更新搜索结果列表
        } else {
            popularMoviesAdapter.updateMovies(movies)  // 更新热门影片列表
            searchResultsAdapter.updateMovies(movies)  // 更新搜索结果列表
        }
        // 显示或隐藏“查看更多”按钮
        binding.cvMore.visibility = if (movies.size >= 7) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.penmediatv

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.penmediatv.API.SearchApi
import com.example.penmediatv.Data.SearchRequest
import com.example.penmediatv.Data.SearchResponse
import com.example.penmediatv.Data.TrendRecommendItem
import com.example.penmediatv.Data.TrendRecommendResponse
import com.example.penmediatv.databinding.FragmentSearchBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private var searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
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

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                // 移除之前的搜索任务，避免重复搜索
                searchRunnable?.let { searchHandler.removeCallbacks(it) }

                // 延迟执行搜索请求
                searchRunnable = Runnable {
                    searchMovies(query)
                }
                searchHandler.postDelayed(searchRunnable!!, 500) // 延迟500毫秒后执行搜索
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
                        binding.cvMore.visibility = if (movies.size > 7) View.VISIBLE else View.GONE
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
        val buttons = listOf<Button>(
            binding.keyA,
            binding.keyB,
            binding.keyC,
            binding.keyD,
            binding.keyE,
            binding.keyF,
            binding.keyG,
            binding.keyH,
            binding.keyI,
            binding.keyJ,
            binding.keyK,
            binding.keyL,
            binding.keyM,
            binding.keyN,
            binding.keyO,
            binding.keyP,
            binding.keyQ,
            binding.keyR,
            binding.keyS,
            binding.keyT,
            binding.keyU,
            binding.keyV,
            binding.keyW,
            binding.keyX,
            binding.keyY,
            binding.keyZ,
            binding.key0,
            binding.key1,
            binding.key2,
            binding.key3,
            binding.key4,
            binding.key5,
            binding.key6,
            binding.key7,
            binding.key8,
            binding.key9,
        )

        for (button in buttons) {
            button.setOnClickListener {
                val currentText = binding.etSearch.text.toString()
                binding.etSearch.setText(currentText + button.text)
            }
        }
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

    private fun searchMovies(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val searchApi = retrofit.create(SearchApi::class.java)
        val searchRequest = SearchRequest(page = 1, pageSize = 7, searchList = listOf(query))
        val call = searchApi.search(searchRequest)

        call.enqueue(object : retrofit2.Callback<SearchResponse> {
            override fun onResponse(
                call: retrofit2.Call<SearchResponse>,
                response: retrofit2.Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()?.data?.records
                    if (movies != null) {
                        // 更新列表
                        updateMovieLists(movies)
                    }
                } else {
                    // 处理错误
                    ErrorHandler.handleUnsuccessfulResponse(
                        binding.root.context,
                        this::class.java.simpleName
                    )
                }
            }

            override fun onFailure(call: retrofit2.Call<SearchResponse>, t: Throwable) {
                Log.e("SearchFragment", "search onFailure: ${t.message}")
                // 处理失败
                ErrorHandler.handleFailure(
                    t,
                    binding.root.context,
                    this::class.java.simpleName
                )
            }
        })
    }

    private fun updateMovieLists(movies: List<TrendRecommendItem>) {
        popularMoviesAdapter.updateMovies(movies)  // 更新热门影片列表
        searchResultsAdapter.updateMovies(movies)  // 更新搜索结果列表

        // 根据影片数量显示或隐藏 “查看更多” 按钮
        binding.cvMore.visibility = if (movies.size > 7) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

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
import com.example.penmediatv.API.SearchApi
import com.example.penmediatv.Data.TrendRecommendItem
import com.example.penmediatv.Data.TrendRecommendResponse
import com.example.penmediatv.databinding.FragmentSearchBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var searchResultsAdapter: SearchResultsAdapter

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
//                searchMovies(query)
            }

            // 其他TextWatcher方法可以留空
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
                }
            }

            override fun onFailure(call: retrofit2.Call<TrendRecommendResponse>, t: Throwable) {
                // 处理错误
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

//    private fun searchMovies(query: String) {
//        // 假设这里是调用API来获取查询结果，代码只是演示
//        val results = listOf(
//            Movie("搜索结果1", R.drawable.movie, "描述..."),
//            // 添加更多搜索结果
//        )
//
//        searchResults.clear()
//        searchResults.addAll(results)
//        searchResultsAdapter.notifyDataSetChanged()
//
//        val resultsSimple = listOf(
//            Movie("搜索结果1", R.drawable.movie, "描述..."),
//            // 添加更多搜索结果
//        )
//        popularMovies.clear()
//        popularMovies.addAll(resultsSimple)
//        popularMoviesAdapter.notifyDataSetChanged()
//        binding.cvMore.visibility = if (results.size > 7) View.VISIBLE else View.GONE
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

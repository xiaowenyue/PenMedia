package com.example.penmediatv

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.API.HistoryApi
import com.example.penmediatv.Data.HistoryAddResponse
import com.example.penmediatv.Data.HistoryClearRequest
import com.example.penmediatv.Data.HistoryResponse
import com.example.penmediatv.databinding.FragmentHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private var currentPage = 1
    private var pageSize = 10
    private var totalPages = 1 // 从服务器获取的总页数
    private var isLoading = false
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchHistories(currentPage, pageSize)
        binding.hisClear.setOnClickListener {
            clearHistories()
        }
    }

    private fun clearHistories() {
        val androidId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val historyApi = retrofit.create(HistoryApi::class.java)

        val clearRequest = HistoryClearRequest(deviceId = androidId)
        val call = historyApi.clearHistory(clearRequest)
        call.enqueue(object : Callback<HistoryAddResponse> {
            override fun onResponse(
                call: Call<HistoryAddResponse>,
                response: Response<HistoryAddResponse>
            ) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()
                    if (historyResponse?.code == "0000") {
                        // 清除成功，更新UI
                        adapter.clearMovies()
                        showEmptyState()
                    } else {
                        // 清除失败，显示失败提示
                        Log.e(
                            "HistoryFragment",
                            "Error: ${response.code()} - ${response.errorBody()?.string()}"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<HistoryAddResponse>, t: Throwable) {
                // 请求失败处理
                Log.e("HistoryFragment", "Failed to clear: ${t.message}")
            }
        })
    }
    private fun showRecyclerView() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.llEmpty.visibility = View.GONE
    }
    private fun showEmptyState() {
        binding.recyclerView.visibility = View.GONE
        binding.llEmpty.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        adapter = HistoryAdapter(mutableListOf())
        binding.recyclerView.adapter = adapter
    }

    private fun fetchHistories(page: Int, pageSize: Int) {
        isLoading = true
        val androidId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val historyApi = retrofit.create(HistoryApi::class.java)
        val call = historyApi.getHistory(androidId, page, pageSize)

        call.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val historyData = response.body()?.data
                    if (historyData != null) {
                        totalPages = (historyData.totalRecords + pageSize - 1) / pageSize
                        val historyList = historyData.records

                        if (historyList.isNotEmpty()) {
                            // 将新数据追加到现有数据中
                            adapter.updateHistories(historyList)
                            showRecyclerView()
                        } else {
                            showEmptyState()
                        }
                    } else {
                        showEmptyState()
                    }
                } else {
                    Log.e(
                        "HistoryFragment",
                        "Error: ${response.code()} - ${response.errorBody()?.string()}"
                    )
                }
                isLoading = false
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e("HistoryFragment", "Network Error: ${t.message}")
                isLoading = false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
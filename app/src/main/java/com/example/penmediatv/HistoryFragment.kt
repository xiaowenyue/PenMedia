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
        fetchAnimations(currentPage, pageSize)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 5)
        adapter = HistoryAdapter(mutableListOf())
        binding.recyclerView.adapter = adapter
    }

    private fun fetchAnimations(page: Int, pageSize: Int) {
        isLoading = true
        val androidId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val historyApi = retrofit.create(HistoryApi::class.java)
        val call = historyApi.getHistory(androidId,page, pageSize)

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
                        }
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
package com.example.penmediatv

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.io.IOException

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
                } else {
                    // 根据不同的状态码进行处理
                    when (response.code()) {
                        404 -> {
                            // 资源未找到
                            Log.e("HistoryFragment", "资源未找到 (404): ${response.message()}")
                            Toast.makeText(binding.root.context, "资源未找到", Toast.LENGTH_SHORT)
                                .show()
                        }

                        403 -> {
                            // 权限不足
                            Log.e("HistoryFragment", "权限不足 (403): ${response.message()}")
                            Toast.makeText(
                                binding.root.context,
                                "您没有访问该资源的权限",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        401 -> {
                            // 未授权
                            Log.e("HistoryFragment", "未授权 (401): ${response.message()}")
                            Toast.makeText(binding.root.context, "请先登录", Toast.LENGTH_SHORT)
                                .show()
                        }

                        500 -> {
                            // 服务器错误
                            Log.e("HistoryFragment", "服务器错误 (500): ${response.message()}")
                            val dialog = Dialog(binding.root.context)
                            dialog.setContentView(R.layout.dialog_network_dismiss)
                            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                            dialog.show()

                            Handler(Looper.getMainLooper()).postDelayed({
                                dialog.dismiss()
                            }, 2000)
                        }

                        else -> {
                            // 其他错误
                            Log.e(
                                "HistoryFragment",
                                "未知错误: ${response.code()}, ${response.message()}"
                            )
                            val dialog = Dialog(binding.root.context)
                            dialog.setContentView(R.layout.dialog_network_dismiss)
                            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                            dialog.show()

                            Handler(Looper.getMainLooper()).postDelayed({
                                dialog.dismiss()
                            }, 2000)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<HistoryAddResponse>, t: Throwable) {
                // 根据不同的错误类型进行处理
                if (t is IOException) {
                    // 网络或服务器不可达
                    Log.e("HistoryFragment", "网络错误或服务器不可达: ${t.message}")
                } else {
                    // 其他类型错误（如转换错误）
                    Log.e("HistoryFragment", "未知错误: ${t.message}")
                }
                val dialog = Dialog(binding.root.context)
                dialog.setContentView(R.layout.dialog_network_disconnect)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    dialog.dismiss()
                }, 2000)
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
                    val dialog = Dialog(binding.root.context)
                    dialog.setContentView(R.layout.dialog_network_dismiss)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 2000)
                }
                isLoading = false
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                Log.e("HistoryFragment", "Network Error: ${t.message}")
                isLoading = false
                if (t is IOException) {
                    // 网络或服务器不可达
                    Log.e("HistoryFragment", "网络错误或服务器不可达: ${t.message}")
                } else {
                    // 其他类型错误（如转换错误）
                    Log.e("HistoryFragment", "未知错误: ${t.message}")
                }
                val dialog = Dialog(binding.root.context)
                dialog.setContentView(R.layout.dialog_network_disconnect)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    dialog.dismiss()
                }, 2000)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // 重置分页参数
        currentPage = 1
        // 清空现有数据
        adapter.clearMovies()
        // 从第一页重新获取数据
        fetchHistories(currentPage, pageSize)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
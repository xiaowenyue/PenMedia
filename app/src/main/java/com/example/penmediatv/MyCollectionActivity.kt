package com.example.penmediatv

import android.opengl.Visibility
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.API.AnimationApi
import com.example.penmediatv.API.CollectionApi
import com.example.penmediatv.Data.AnimationResponse
import com.example.penmediatv.Data.CollectionClearRequest
import com.example.penmediatv.Data.CollectionResponse
import com.example.penmediatv.databinding.ActivityMyCollectionBinding
import com.example.penmediatv.utils.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyCollectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCollectionBinding
    private var currentPage = 1
    private var pageSize = 10
    private var totalPages = 1 // 从服务器获取的总页数
    private var isLoading = false
    private lateinit var adapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchCollections(currentPage, pageSize)

        binding.btnClear.setOnClickListener {
            clearCollections()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 5)
        adapter = CollectionAdapter(mutableListOf())
        binding.recyclerView.adapter = adapter
    }

    private fun fetchCollections(page: Int, pageSize: Int) {
        isLoading = true
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val collectionApi = retrofit.create(CollectionApi::class.java)
        val call = collectionApi.getCollection(androidId, page, pageSize)

        call.enqueue(object : Callback<CollectionResponse> {
            override fun onResponse(
                call: Call<CollectionResponse>,
                response: Response<CollectionResponse>
            ) {
                if (response.isSuccessful) {
                    val collectionResponse = response.body()?.data
                    if (collectionResponse != null) {
                        totalPages = (collectionResponse.totalRecords + pageSize - 1) / pageSize
                        val collectionList = collectionResponse.records

                        if (collectionList.isNotEmpty()) {
                            // 将新数据追加到现有数据中
                            adapter.updateMovies(collectionList)
                            showRecyclerView()
                        } else {
                            showEmptyState()
                        }
                    } else {
                        showEmptyState()
                    }
                } else {
                    Log.e(
                        "CollectionActivity",
                        "Error: ${response.code()} - ${response.errorBody()?.string()}"
                    )
                    showEmptyState()
                    ErrorHandler.handleUnsuccessfulResponse(
                        this@MyCollectionActivity,
                        this::class.java.simpleName
                    )
                }
                isLoading = false
            }

            override fun onFailure(call: Call<CollectionResponse>, t: Throwable) {
                Log.e("CollectionActivity", "Network Error: ${t.message}")
                showEmptyState()
                isLoading = false
                ErrorHandler.handleFailure(
                    t,
                    this@MyCollectionActivity,
                    this::class.java.simpleName
                )
            }
        })
    }

    private fun clearCollections() {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://44.208.55.69/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val collectionApi = retrofit.create(CollectionApi::class.java)

        val clearRequest = CollectionClearRequest(deviceId = androidId)
        val call = collectionApi.clearCollection(clearRequest)
        call.enqueue(object : Callback<CollectionResponse> {
            override fun onResponse(
                call: Call<CollectionResponse>,
                response: Response<CollectionResponse>
            ) {
                if (response.isSuccessful) {
                    val collectionResponse = response.body()
                    if (collectionResponse?.code == "0000") {
                        // 清除成功，更新UI
                        adapter.clearMovies()
                        showEmptyState()
                    } else {
                        // 清除失败，显示失败提示
                        Log.e(
                            "CollectionActivity",
                            "Error: ${response.code()} - ${response.errorBody()?.string()}"
                        )
                    }
                } else {
                    ErrorHandler.handleUnsuccessfulResponse(
                        this@MyCollectionActivity,
                        this::class.java.simpleName
                    )
                }
            }

            override fun onFailure(call: Call<CollectionResponse>, t: Throwable) {
                // 请求失败处理
                Log.e("CollectionActivity", "Failed to clear: ${t.message}")
                ErrorHandler.handleFailure(
                    t,
                    this@MyCollectionActivity,
                    this::class.java.simpleName
                )
            }
        })
    }

    private fun showEmptyState() {
        binding.recyclerView.visibility = View.GONE
        binding.llCollectionEmpty.visibility = View.VISIBLE
    }

    private fun showRecyclerView() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.llCollectionEmpty.visibility = View.GONE
    }
}
package com.example.penmediatv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.penmediatv.databinding.ActivityMyCollectionBinding

class MyCollectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCollectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置 RecyclerView 的布局管理器
        binding.rvCollection.layoutManager = GridLayoutManager(this, 3)

        // 创建电影列表
        val movieList = listOf(
            Movie("Movie 1", R.drawable.movie),
            Movie("Movie 2", R.drawable.movie),
            Movie("Movie 3", R.drawable.movie),
            Movie("Movie 4", R.drawable.movie),
            Movie("Movie 5", R.drawable.ic_search),
            Movie("Movie 6", R.drawable.ic_history),
        )

        // 设置适配器
        binding.rvCollection.adapter = HistoryAdapter(movieList)
    }
}
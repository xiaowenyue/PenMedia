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
        setContentView(R.layout.activity_my_collection)

        binding.rvCollection.layoutManager = GridLayoutManager(this, 3)
        val movieList = listOf(
            Movie("Movie 1", R.drawable.movie),
            Movie("Movie 2", R.drawable.movie),
            Movie("Movie 3", R.drawable.movie),
            Movie("Movie 4", R.drawable.movie),
            Movie("Movie 5", R.drawable.ic_search),
            Movie("Movie 6", R.drawable.ic_history),
        )
        binding.rvCollection.adapter = HistoryAdapter(movieList)
    }
}
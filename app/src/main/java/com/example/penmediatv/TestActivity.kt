package com.example.penmediatv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.databinding.FragmentSearchBinding

class TestActivity :AppCompatActivity(){
    private lateinit var binding:FragmentSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etSearch.setOnFocusChangeListener { v, hasFocus ->
        }

    }
}
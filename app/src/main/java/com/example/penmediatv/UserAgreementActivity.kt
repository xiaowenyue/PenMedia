package com.example.penmediatv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.penmediatv.databinding.ActivityAboutUsBinding
import com.example.penmediatv.databinding.ActivityUserAgreementBinding

class UserAgreementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserAgreementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAgreementBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.company.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                binding.viewCompany.setBackgroundColor(resources.getColor(R.color.button_pressed))
//            } else {
//                binding.viewCompany.setBackgroundColor(resources.getColor(R.color.button))
//            }
//        }
    }
}
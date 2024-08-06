package com.example.penmediatv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.penmediatv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_movies -> replaceFragment(HomeFragment())
                R.id.nav_tv_series -> replaceFragment(HomeFragment())
                R.id.nav_animation -> replaceFragment(HomeFragment())
                R.id.nav_documentary -> replaceFragment(HomeFragment())
                R.id.nav_mine -> replaceFragment(MineFragment())
                R.id.nav_search -> replaceFragment(HomeFragment())
                R.id.nav_history -> replaceFragment(HomeFragment())
            }
            true
        }

        if (savedInstanceState == null) {
            binding.navView.setCheckedItem(R.id.nav_home)
            replaceFragment(HomeFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

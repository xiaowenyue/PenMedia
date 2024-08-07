package com.example.penmediatv

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.penmediatv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navButtons = listOf(
            binding.navMine,
            binding.navSearch,
            binding.navHistory,
            binding.navHome,
            binding.navMovies,
            binding.navTvSeries,
            binding.navAnimation,
            binding.navDocumentary
        )

        navButtons.forEach { button ->
            button.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    onNavButtonFocused(view)
                }
            }
        }

        if (savedInstanceState == null) {
            binding.navHome.performClick()
        }
    }

    private fun onNavButtonFocused(view: View) {
        val fragment: Fragment = when (view.id) {
            R.id.nav_mine -> MineFragment()
            R.id.nav_search -> MineFragment()
            R.id.nav_history -> MineFragment()
            R.id.nav_home -> HomeFragment()
            R.id.nav_movies -> MineFragment()
            R.id.nav_tv_series -> MineFragment()
            R.id.nav_animation -> MineFragment()
            R.id.nav_documentary -> MineFragment()
            else -> HomeFragment()
        }
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

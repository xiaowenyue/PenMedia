package com.example.penmediatv

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        navButtons.forEach { button ->
            button.setOnClickListener { view ->
                onNavButtonFocused(view)
            }
        }

        if (savedInstanceState == null) {
            binding.navHome.performClick()
        }

        val width = 30 // 设置你想要的宽度
        val height = 30 // 设置你想要的高度
        val textViewMine: TextView = findViewById(R.id.nav_mine)
        val drawableMine = ContextCompat.getDrawable(this, R.drawable.ic_mine_selector)!!
        drawableMine.setBounds(0, 0, width, height)
        textViewMine.setCompoundDrawables(drawableMine, null, null, null)
        val textViewSearch: TextView = findViewById(R.id.nav_search)
        val drawableSearch = ContextCompat.getDrawable(this, R.drawable.ic_search_selector)!!
        drawableSearch.setBounds(0, 0, width, height)
        textViewSearch.setCompoundDrawables(drawableSearch, null, null, null)
        val textViewHistory: TextView = findViewById(R.id.nav_history)
        val drawableHistory = ContextCompat.getDrawable(this, R.drawable.ic_history_selector)!!
        drawableHistory.setBounds(0, 0, width, height)
        textViewHistory.setCompoundDrawables(drawableHistory, null, null, null)
    }

    private fun onNavButtonFocused(view: View) {
        val fragment: Fragment = when (view.id) {
            R.id.nav_mine -> HomeFragment()
            R.id.nav_search -> HistoryFragment()
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

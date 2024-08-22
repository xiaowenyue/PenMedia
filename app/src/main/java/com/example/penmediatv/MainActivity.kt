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

        /// 设置导航栏图标大小
        val width = 30 // 设置你想要的宽度
        val height = 30 // 设置你想要的高度
        val drawableMine = ContextCompat.getDrawable(this, R.drawable.ic_mine_selector)!!
        drawableMine.setBounds(0, 0, width, height)
        binding.navMine.setCompoundDrawables(drawableMine, null, null, null)
        val drawableSearch = ContextCompat.getDrawable(this, R.drawable.ic_search_selector)!!
        drawableSearch.setBounds(0, 0, width, height)
        binding.navSearch.setCompoundDrawables(drawableSearch, null, null, null)
        val drawableHistory = ContextCompat.getDrawable(this, R.drawable.ic_history_selector)!!
        drawableHistory.setBounds(0, 0, width, height)
        binding.navHistory.setCompoundDrawables(drawableHistory, null, null, null)
    }

    private fun onNavButtonFocused(view: View) {
        val fragment: Fragment = when (view.id) {
            R.id.nav_mine -> MineFragment()
            R.id.nav_search -> MineFragment()
            R.id.nav_history -> HistoryFragment()
            R.id.nav_home -> HomeFragment()
            R.id.nav_movies -> MoviesFragment()
            R.id.nav_tv_series -> TvSeriesFragment()
            R.id.nav_animation -> AnimationFragment()
            R.id.nav_documentary -> DocumentaryFragment()
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

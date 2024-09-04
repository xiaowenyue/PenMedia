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
    private var lastFocusedNavButtonId: Int = R.id.nav_home  // 默认焦点是nav_home

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
                    lastFocusedNavButtonId = view.id
                    onNavButtonFocused(view)
//                    binding.fragmentContainer.requestFocus()
                }
            }
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

        binding.fragmentContainer.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                // 焦点回到上一个聚焦的navButton
                findViewById<View>(lastFocusedNavButtonId).requestFocus()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun onNavButtonFocused(view: View) {
        val fragment: Fragment = when (view.id) {
            R.id.nav_mine -> MineFragment()
            R.id.nav_search -> com.example.penmediatv.SearchFragment()
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
//        binding.fragmentContainer.requestFocus()
        //在Fragment加载后请求焦点
//        binding.fragmentContainer.post{
//            binding.fragmentContainer.requestFocus()
//        }
    }
}

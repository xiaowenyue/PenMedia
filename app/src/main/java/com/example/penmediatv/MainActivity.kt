package com.example.penmediatv

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
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

        binding.navHome.requestFocus()
    }

    private fun canMoveFocusLeft(view: View): Boolean {
        val leftNeighbor = view.focusSearch(View.FOCUS_LEFT)
        return leftNeighbor != null && leftNeighbor.isFocusable && leftNeighbor.parent === view.parent
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && binding.fragmentContainer.hasFocus()) {
            // 获取当前fragment中的可见视图
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            val currentFocusedView = currentFragment?.view?.findFocus()

            // 检查当前Fragment中是否有其他可聚焦的左边组件
            if (currentFocusedView != null && canMoveFocusLeft(currentFocusedView)) {
//                Toast.makeText(
//                    this,
//                    "FragmentContainer中还有可聚焦组件，返回的navButton是$lastFocusedNavButtonId",
//                    Toast.LENGTH_SHORT
//                ).show()
                return false // 允许Fragment处理焦点移动
            } else {
//                Toast.makeText(
//                    this,
//                    "FragmentContainer中没有可聚焦组件，现在返回的navButton是$lastFocusedNavButtonId",
//                    Toast.LENGTH_SHORT
//                ).show()
                // 否则将焦点返回给导航按钮
                val lastFocusedButton = findViewById<TextView>(lastFocusedNavButtonId)
                lastFocusedButton.requestFocus()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
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
    }
}

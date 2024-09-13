package com.example.penmediatv

import android.os.Bundle
import android.util.Log
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
            button.setOnClickListener {
                onNavButtonClicked(button)
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
                return false // 允许Fragment处理焦点移动
            } else {
                val lastFocusedButton = findViewById<TextView>(lastFocusedNavButtonId)
                lastFocusedButton.requestFocus()
                return true
            }
        }
        // 如果按下右键，焦点从导航按钮移到Fragment中的第一个可聚焦控件
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && findViewById<View>(lastFocusedNavButtonId).hasFocus()) {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            Log.d("FragmentName", "currentFragment: ${currentFragment?.javaClass?.simpleName}")
            // 确定Fragment中的第一个可聚焦控件
            var firstFocusableView: View? = null
            if (currentFragment?.javaClass?.simpleName == "HomeFragment") {
                firstFocusableView = currentFragment?.view?.findViewById<View>(R.id.imageView)
            } else if (currentFragment?.javaClass?.simpleName == "HistoryFragment") {
                firstFocusableView = currentFragment?.view?.findViewById<View>(R.id.recyclerView)
            } else if (currentFragment?.javaClass?.simpleName == "MineFragment") {
                firstFocusableView = currentFragment?.view?.findViewById<View>(R.id.my_collection)
            } else if (currentFragment?.javaClass?.simpleName == "SearchFragment") {
                firstFocusableView = currentFragment?.view?.findViewById<View>(R.id.et_search)
            } else if (currentFragment?.javaClass?.simpleName == "MoviesFragment") {
                firstFocusableView = currentFragment?.view?.findViewById<View>(R.id.viewPagerLayout)
            } else if (currentFragment?.javaClass?.simpleName == "TvSeriesFragment") {
                firstFocusableView = currentFragment?.view?.findViewById<View>(R.id.card1)
            } else if (currentFragment?.javaClass?.simpleName == "DocumentaryFragment") {
                firstFocusableView = currentFragment?.view?.findViewById<View>(R.id.card0)
            } else if (currentFragment?.javaClass?.simpleName == "AnimationFragment") {
                firstFocusableView = currentFragment?.view?.findViewById<View>(R.id.vpm_animation)
            }

            // 手动将焦点转移到Fragment的第一个控件
            firstFocusableView?.requestFocus()
            return true
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

    private fun onNavButtonClicked(view: View) {
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

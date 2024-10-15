package com.example.penmediatv

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.penmediatv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var lastFocusedNavButtonId: Int = R.id.nav_home  // 默认焦点是nav_home
    private var currentFragmentTag: String? = null  // 用于记录当前显示的Fragment类型

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

        // 设置导航栏图标大小
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

    // 判断左边是否有可以移动焦点的元素
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
        // 焦点从导航栏按钮移到Fragment时
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && findViewById<View>(lastFocusedNavButtonId).hasFocus()) {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            Log.d("FragmentName", "currentFragment: ${currentFragment?.javaClass?.simpleName}")
            var firstFocusableView: View? = null

            // 定位Fragment中第一个可聚焦的控件
            when (currentFragment?.javaClass?.simpleName) {
                "HomeFragment" -> firstFocusableView =
                    currentFragment?.view?.findViewById(R.id.cv_0)

                "HistoryFragment" -> firstFocusableView =
                    currentFragment?.view?.findViewById(R.id.recyclerView)

                "MineFragment" -> firstFocusableView =
                    currentFragment?.view?.findViewById(R.id.my_collection)

                "SearchFragment" -> firstFocusableView =
                    currentFragment?.view?.findViewById(R.id.keyA)

                "MoviesFragment" -> firstFocusableView =
                    currentFragment?.view?.findViewById(R.id.viewPagerLayout)

                "TvSeriesFragment" -> firstFocusableView =
                    currentFragment?.view?.findViewById(R.id.card1)

                "DocumentaryFragment" -> firstFocusableView =
                    currentFragment?.view?.findViewById(R.id.card0)

                "AnimationFragment" -> firstFocusableView =
                    currentFragment?.view?.findViewById(R.id.vpm_animation)
            }

            firstFocusableView?.requestFocus()
            return true
        }
        // 在导航栏循环上下切换
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && binding.navDocumentary.hasFocus()) {
            binding.navMine.requestFocus()
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && binding.navMine.hasFocus()) {
            binding.navDocumentary.requestFocus()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // 处理导航按钮聚焦
    private fun onNavButtonFocused(view: View) {
        val fragmentTag = when (view.id) {
            R.id.nav_mine -> "MineFragment"
            R.id.nav_search -> "SearchFragment"
            R.id.nav_history -> "HistoryFragment"
            R.id.nav_home -> "HomeFragment"
            R.id.nav_movies -> "MoviesFragment"
            R.id.nav_tv_series -> "TvSeriesFragment"
            R.id.nav_animation -> "AnimationFragment"
            R.id.nav_documentary -> "DocumentaryFragment"
            else -> "HomeFragment"
        }

        // 仅在Fragment未显示时才替换
        if (currentFragmentTag != fragmentTag) {
            currentFragmentTag = fragmentTag
            val fragment: Fragment = when (fragmentTag) {
                "MineFragment" -> MineFragment()
                "SearchFragment" -> SearchFragment()
                "HistoryFragment" -> HistoryFragment()
                "HomeFragment" -> HomeFragment()
                "MoviesFragment" -> MoviesFragment()
                "TvSeriesFragment" -> TvSeriesFragment()
                "AnimationFragment" -> AnimationFragment()
                "DocumentaryFragment" -> DocumentaryFragment()
                else -> HomeFragment()
            }
            replaceFragment(fragment)
        }
    }

    // 处理导航按钮点击
    private fun onNavButtonClicked(view: View) {
        onNavButtonFocused(view) // 点击时也调用聚焦逻辑，确保Fragment刷新
    }

    // 替换Fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

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
    private val navButtonIds = setOf(
        R.id.nav_mine,
        R.id.nav_search,
        R.id.nav_history,
        R.id.nav_home,
        R.id.nav_movies,
        R.id.nav_tv_series,
        R.id.nav_documentary,
        R.id.nav_animation
    )

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
        setNavButtonIcon(binding.navMine, R.drawable.ic_mine_selector, 30, 30)
        setNavButtonIcon(binding.navSearch, R.drawable.ic_search_selector, 30, 30)
        setNavButtonIcon(binding.navHistory, R.drawable.ic_history_selector, 30, 30)

        // 设置默认焦点
        binding.navHome.requestFocus()
    }

    // 图标设置方法，减少重复代码
    private fun setNavButtonIcon(button: TextView, iconResId: Int, width: Int, height: Int) {
        val drawable = ContextCompat.getDrawable(this, iconResId)!!
        drawable.setBounds(0, 0, width, height)
        button.setCompoundDrawables(drawable, null, null, null)
    }

    // 判断左边是否有可以移动焦点的元素
    private fun canMoveFocusLeft(view: View): Boolean {
        val leftNeighbor = view.focusSearch(View.FOCUS_LEFT)
        return leftNeighbor != null && leftNeighbor.isFocusable && leftNeighbor.parent === view.parent
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            // 获取当前fragment中的可见视图
            val currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
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
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            // 获取当前焦点的视图
            val currentFocusedView = currentFocus

            // 判断当前焦点是否在导航栏上，比如R.id.navButtons是你的导航栏布局
            if (currentFocusedView != null && currentFocusedView.id in navButtonIds) {
                // 定位到Fragment的第一个可聚焦控件
                focusFirstViewInCurrentFragment()
                return true
            }
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

        // 仅当Fragment未显示时才进行Fragment的显示
        if (currentFragmentTag != fragmentTag) {
            showFragment(fragmentTag)
        }
    }

    // 处理导航按钮点击
    private fun onNavButtonClicked(view: View) {
        onNavButtonFocused(view) // 点击时也调用聚焦逻辑，确保Fragment刷新
    }

    private val fragmentMap = mutableMapOf<String, Fragment>()

    // 替换Fragment的方法变成显示和隐藏Fragment的方法
    private fun showFragment(tag: String) {
        val transaction = supportFragmentManager.beginTransaction()

        // 如果当前有显示的Fragment，则将其隐藏
        val currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }

        // 尝试从缓存中获取Fragment
        var fragment = fragmentMap[tag]
        if (fragment == null) {
            // 如果缓存中不存在Fragment，创建新的Fragment
            fragment = when (tag) {
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
            // 将Fragment添加到FragmentManager，并缓存
            fragmentMap[tag] = fragment
            transaction.add(R.id.fragment_container, fragment, tag)
        } else {
            // 如果Fragment已经存在，则显示
            transaction.show(fragment)
        }

        // 提交事务
        transaction.commit()

        // 更新当前Fragment的Tag
        currentFragmentTag = tag
    }

    private fun focusFirstViewInCurrentFragment() {
        val currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
        if (currentFragment != null) {
            var firstFocusableView: View? = null
            when (currentFragment.javaClass.simpleName) {
                "HomeFragment" -> firstFocusableView = currentFragment.view?.findViewById(R.id.cv_0)
                "HistoryFragment" -> firstFocusableView =
                    currentFragment.view?.findViewById(R.id.recyclerView)

                "MineFragment" -> firstFocusableView =
                    currentFragment.view?.findViewById(R.id.my_collection)

                "SearchFragment" -> firstFocusableView =
                    currentFragment.view?.findViewById(R.id.keyA)

                "MoviesFragment" -> firstFocusableView =
                    currentFragment.view?.findViewById(R.id.viewPagerLayout)

                "TvSeriesFragment" -> firstFocusableView =
                    currentFragment.view?.findViewById(R.id.card1)

                "DocumentaryFragment" -> firstFocusableView =
                    currentFragment.view?.findViewById(R.id.card0)

                "AnimationFragment" -> firstFocusableView =
                    currentFragment.view?.findViewById(R.id.vpm_animation)
            }
            // 请求焦点
            firstFocusableView?.requestFocus()
        }
    }
}

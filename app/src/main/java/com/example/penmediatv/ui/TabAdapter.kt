package com.example.penmediatv.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class TabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Fragment1()
            1 -> Fragment2()
            2 -> Fragment3()
            else -> Fragment1()
        }
    }

    override fun getItemCount(): Int {
        return 3 // 标签页的数量
    }
}

class Fragment3 : Fragment() {

}

class Fragment2 : Fragment() {

}

class Fragment1 : Fragment() {

}

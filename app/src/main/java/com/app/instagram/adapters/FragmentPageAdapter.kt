package com.app.instagram.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.instagram.R
import com.app.instagram.fragments.MyPostFragment
import com.app.instagram.fragments.MyReelsFragment

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        // Return the number of tabs
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        // Return the fragment for each tab
        return when (position) {
            0 -> MyPostFragment()
            1 -> MyReelsFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    fun getTabIcon(position: Int): Int {
        // Return the icon for each tab
        return when (position) {
            0 -> R.drawable.grid
            1 -> R.drawable.video
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}

package com.app.instagram.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.instagram.adapters.FragmentPageAdapter
import com.app.instagram.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a FragmentPageAdapter
        val adapter = FragmentPageAdapter(childFragmentManager, lifecycle)

        // Set the adapter to the ViewPager2
        binding.viewPager2.adapter = adapter

        // Connect the TabLayout with the ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            // Set the text and icon for each tab
//            tab.text = when (position) {
//                0 -> "My Posts"
//                1 -> "My Reels"
//                else -> null
//            }
            tab.icon = requireContext().getDrawable(adapter.getTabIcon(position))
        }.attach()
    }
}


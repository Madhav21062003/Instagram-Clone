package com.app.instagram.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.instagram.R
import com.app.instagram.adapters.ViewPagerAdapter
import com.app.instagram.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater , container, false)


        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)

        viewPagerAdapter.addFragments(MyPostFragment(), "My Posts")
        viewPagerAdapter.addFragments(MyReelsFragment(), "My Reels")

//        binding.viewPager.adapter = viewPagerAdapter
//
//        binding.myTabs.setupWithViewPager(binding.viewPager)
        return binding.root

    }



}
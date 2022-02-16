package com.levp.catsandmath.presentation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.levp.catsandmath.presentation.fragment.CatFragment
import com.levp.catsandmath.presentation.fragment.NumFactsFragment

class ViewPagerFragmentStateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val colors = intArrayOf(
        android.R.color.black,
        android.R.color.holo_red_light,
        android.R.color.holo_blue_dark,
        android.R.color.holo_purple
    )

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position){
        0->CatFragment()
        else->NumFactsFragment()
    }
}
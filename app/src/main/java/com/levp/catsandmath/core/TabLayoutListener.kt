package com.levp.catsandmath.core

import com.google.android.material.tabs.TabLayout

abstract class TabLayoutListener : TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab?) {}
    override fun onTabUnselected(tab: TabLayout.Tab?) {}
    override fun onTabSelected(tab: TabLayout.Tab?) {}
}
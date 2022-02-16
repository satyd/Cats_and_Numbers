package com.levp.catsandmath

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import by.iba.rpms.presentation.common.navigation.RoutingParams
import com.google.android.material.tabs.TabLayoutMediator
import com.levp.catsandmath.core.isOnline
import com.levp.catsandmath.core.noInternetToast
import com.levp.catsandmath.presentation.CatViewModel
import com.levp.catsandmath.presentation.NFViewModel
import com.levp.catsandmath.presentation.ViewPagerFragmentStateAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity() {
    
    private val numFactNFViewModel: NFViewModel by viewModels()

    private val catViewModel: CatViewModel by viewModels()

    private val tabTitles = arrayListOf<String>("Cats", "NumFacts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rParams = RoutingParams(supportFragmentManager, this, R.id.main_page)
        vp2_main.adapter = ViewPagerFragmentStateAdapter(this)
        configureView()
        if (!isOnline(this)) {
            noInternetToast(this)
        }
        
    }
    
    private fun configureView() {
        val tabLayout = main_tabs
        val viewPager = vp2_main
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }


}
package by.iba.rpms.presentation.common.navigation

import android.app.Activity
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.levp.catsandmath.R

class RoutingParams(val fragmentManager: FragmentManager,
                    val activity: Activity,
                    @IdRes val containerId: Int) {

    companion object {

        @IdRes
        private val defaultContainerId: Int = R.id.main_page

        fun of(fragment: Fragment, @IdRes containerId: Int = defaultContainerId): RoutingParams =
            RoutingParams(
                fragmentManager = fragment.parentFragmentManager,
                activity = fragment.requireActivity(),
                containerId = containerId
            )

        fun of(fragmentActivity: FragmentActivity, @IdRes containerId: Int = defaultContainerId): RoutingParams =
            RoutingParams(
                fragmentManager = fragmentActivity.supportFragmentManager,
                activity = fragmentActivity,
                containerId = containerId
            )
    }
}
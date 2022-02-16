package com.levp.catsandmath.presentation.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import by.iba.rpms.presentation.common.navigation.RoutingParams
import javax.inject.Inject

/**
 * [Router] is used to perform navigation between fragments.
 *
 * This class encapsulates low-level operations with fragment transactions.
 *
 * Important:
 * Since this class holds reference to Fragment, it shouldn't be injected in any things,
 * that live longer than Fragment (such as ViewModels, Repositories etc.)
 * */
class Router @Inject constructor(
    routingParams: RoutingParams,
    private val context: Context
) {

    private val activity: Activity =
        routingParams.activity

    private val fragmentManager: FragmentManager =
        routingParams.fragmentManager

    @IdRes
    private val containerId: Int =
        routingParams.containerId

    private val inputMethodManager: InputMethodManager
        get() = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    val isBackStackEmpty: Boolean
        get() = fragmentManager.backStackEntryCount == 0

    fun showDialog(dialogFragment: DialogFragment) {
        dialogFragment.show(fragmentManager, null)
    }

    fun replace(fragment: Fragment) {
        performTransaction {
            replace(containerId, fragment)
        }
    }

    fun replaceWithClearStack(fragment: Fragment) {
        performTransaction {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            replace(containerId, fragment)
        }
    }

    fun addToBackStack(fragment: Fragment) {
        performTransaction {
            add(containerId, fragment)
            addToBackStack(null)
        }
    }

    fun replaceTopOfBackStack(fragment: Fragment) {
        popBackStack()
        addToBackStack(fragment)
    }

    fun popBackStack() {
        fragmentManager.popBackStack()
    }

    fun navigateUp() {
        activity.onBackPressed()
    }

    fun finishWithResult(result: Intent) {
        activity.setResult(Activity.RESULT_OK, result)
        activity.finish()
    }

    fun openUrl(url: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
            .let(activity::startActivity)
    }

    fun hideKeyboardFor(view: View) {
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboardFor(view: View) {
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun performTransaction(action: FragmentTransaction.() -> Unit) {
        fragmentManager
            .beginTransaction()
            .apply(action)
            .commit()
    }
}
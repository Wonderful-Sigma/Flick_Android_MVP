package com.wonderfulsigma.flick.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wonderfulsigma.flick.R

//fun setDeleteBottomNav(activity: FragmentActivity?) {
//    val bottomNavigationBar = activity?.findViewById<BottomNavigationView>(R.id.bnv)
//    bottomNavigationBar?.visibility = View.GONE
//}

fun androidx.appcompat.widget.Toolbar.setPopBackStack() {
    this.setNavigationOnClickListener {
        findNavController().popBackStack()
    }
}

fun setStatusBarColorBackground(activity: Activity, context: Context) {
    activity.window.statusBarColor =
        ContextCompat.getColor(context, R.color.activity_background)
}

fun setStatusBarColorWhite(activity: Activity, context: Context) {
    activity.window.statusBarColor =
        ContextCompat.getColor(context, R.color.white)
}
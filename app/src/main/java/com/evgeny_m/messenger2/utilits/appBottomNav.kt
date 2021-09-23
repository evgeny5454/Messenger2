package com.evgeny_m.messenger2.utilits

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.R
import com.google.android.material.bottomnavigation.BottomNavigationView

lateinit var  navView : BottomNavigationView

 fun MainActivity.initBottomNav() {
     navView = binding.navView
    val navController = findNavController(R.id.nav_host_fragment_content_main)

    val appBarConfiguration = AppBarConfiguration(setOf(
        R.id.navigation_chats,
        R.id.navigation_settings,
        R.id.navigation_contacts
    ))

    navView.setupWithNavController(navController)

}

fun Fragment.hideNavView(){
    navView.visibility = View.GONE
}
fun Fragment.visibleNavView(){
    navView.visibility = View.VISIBLE
}

fun Fragment.addBackButton(toolbar: Toolbar) {
    //toolbar.title = fragment_name
    //drawerLayout.close()
    //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    toolbar.setNavigationIcon(R.drawable.icon_nav_back)
    toolbar.setNavigationOnClickListener {
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        navView.visibility = View.VISIBLE
        APP_ACTIVITY.onBackPressed()
    }
}
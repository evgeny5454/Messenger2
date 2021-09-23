package com.evgeny_m.messenger2.settings_fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.R

open class BaseChangeFragment: Fragment() {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_save,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings_menu_save -> {
                change()
            }
        }
        return true
    }

    open fun change() {

    }
}
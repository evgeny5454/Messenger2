package com.evgeny_m.messenger2

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.evgeny_m.messenger2.activites.RegisterActivity
import com.evgeny_m.messenger2.databinding.ActivityMainBinding
import com.evgeny_m.messenger2.utilits.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity() : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser()
        CoroutineScope(Dispatchers.IO).launch {
            initContacts()
        }
        initFields()
        initFunc()
        initBottomNav()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        navView.visibility = View.VISIBLE
    }


    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    private fun initFields() {
        AUTH = FirebaseAuth.getInstance()
    }



    private fun initFunc() {
        if (AUTH.currentUser == null) replaceActivity(RegisterActivity())

    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            initContacts()
        }
    }
}
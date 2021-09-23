package com.evgeny_m.messenger2.fragments_register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.databinding.FragmentRegisterCodeBinding
import com.evgeny_m.messenger2.utilits.*
import com.google.firebase.auth.PhoneAuthProvider


class RegisterCodeFragment() : Fragment() {

    private lateinit var binding: FragmentRegisterCodeBinding
    private lateinit var toolbar: Toolbar
    private lateinit var credential: String
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        toolbar = binding.registerToolbarEnterCode

        autoRegistation()

        toolbar.title = "Account verification in progress"
        credential = arguments?.getString("credential").toString()

        binding.registerInputCode.addTextChangedListener(AppTextWatcher {

            val string = binding.registerInputCode.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun autoRegistation() {
        progressBar = binding.progressBarEnterCode
        progressBar.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
            toolbar.title = arguments?.getString("phone_number").toString()
            binding.registerLayout.visibility = View.VISIBLE
            binding.registerCodeText.visibility = View.VISIBLE
        }, 10000)
    }

    override fun onResume() {
        super.onResume()

    }

    private fun enterCode() {
        val code = binding.registerInputCode.text.toString()
        Log.d("AUTH", code)
        val credential = PhoneAuthProvider.getCredential(credential, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                val phoneNumber = arguments?.getString("phone_number").toString()
                dateMap[ID] = uid
                dateMap[PHONE] = phoneNumber

                REF_DATABASE_ROOT.child(NODE_PHONES)
                    .child(phoneNumber)
                    .setValue(uid)
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT
                            .child(NODE_USERS)
                            .child(uid)
                            .updateChildren(dateMap)
                            .addOnCompleteListener { task2 ->
                                if (task2.isSuccessful) {
                                    showToast("$PHONE is valid with code")
                                    replaceActivity(MainActivity())
                                } else {
                                    showToast("Please try again later")
                                    Log.d("AUTH", task2.exception.toString())
                                }
                            }
                    }

            } else {
                showToast("Please try again later")
                Log.d("AUTH", task.exception.toString())
            }
        }
    }


}
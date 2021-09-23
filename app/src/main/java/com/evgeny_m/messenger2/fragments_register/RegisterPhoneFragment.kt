package com.evgeny_m.messenger2.fragments_register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.R
import com.evgeny_m.messenger2.databinding.FragmentRegisterPhoneBinding
import com.evgeny_m.messenger2.utilits.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class RegisterPhoneFragment : Fragment() {

    private lateinit var binding: FragmentRegisterPhoneBinding
    private lateinit var phoneNumber: String
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private val bundle = Bundle()
    private lateinit var progressBar: ProgressBar



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        progressBar = binding.progressBarEnterPhone

        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener {
                    //progressBar.visibility = View.VISIBLE
                    if (it.isSuccessful) {
                        val uid = AUTH.currentUser?.uid.toString()
                        val dateMap = mutableMapOf<String, Any>()

                        dateMap[ID] = uid
                        dateMap[PHONE] = phoneNumber
                        REF_DATABASE_ROOT
                            .child(NODE_PHONES)
                            .child(phoneNumber)
                            .setValue(uid)
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT
                                    .child(NODE_USERS)
                                    .child(uid)
                                    .updateChildren(dateMap).addOnCompleteListener {
                                        showToast("$phoneNumber is valid")
                                        replaceActivity(MainActivity())
                                    }
                        }
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
                progressBar.visibility = View.GONE
            }

            override fun onCodeSent(
                credential: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                bundle.putString("credential", credential)
                bundle.putString("phone_number", phoneNumber)
                view?.findNavController()
                    ?.navigate(R.id.action_registerPhoneFragment_to_registerCodeFragment, bundle)
            }
        }

        binding.registerBtnNext.setOnClickListener {
            hideKeyboard()
            progressBar.visibility = View.VISIBLE
            sendCode()
        }
    }

    private fun sendCode() {
        if (binding.registerInputPhoneNumber.text.toString().isEmpty()) {
            showToast("Please enter your phone")
        } else {
            authUser()
        }
    }

    private fun authUser() {
        phoneNumber = binding.registerInputPhoneNumber.text.toString()
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}
package com.evgeny_m.messenger2.settings_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.databinding.FragmentChangeBioBinding
import com.evgeny_m.messenger2.utilits.*


class ChangeBioFragment : BaseChangeFragment() {

    private lateinit var binding: FragmentChangeBioBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.settingsInputBio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = binding.settingsInputBio.text.toString()
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(BIO)
            .setValue(newBio)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast("Data update")
                    USER.bio = newBio
                    (activity as MainActivity).onBackPressed()
                    hideKeyboard()
                }
        }
    }


}
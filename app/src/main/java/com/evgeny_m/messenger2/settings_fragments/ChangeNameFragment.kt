package com.evgeny_m.messenger2.settings_fragments

import android.os.Bundle
import android.view.*
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.databinding.FragmentChangeNameBinding
import com.evgeny_m.messenger2.utilits.*


class ChangeNameFragment : BaseChangeFragment() {

    private lateinit var binding: FragmentChangeNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initFullName()
    }

    private fun initFullName() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size == 2) {
            binding.settingsInputName.setText(fullnameList[0])
            binding.settingsInputSurname.setText(fullnameList[1])
        } else {
            binding.settingsInputName.setText(fullnameList[0])
        }
    }

    override fun change() {
        initFirebase()
        val name = binding.settingsInputName.text.toString()
        val surname = binding.settingsInputSurname.text.toString()
        if (name.isEmpty()){
            showToast("Please enter your name")
        } else {
            val fullName = "$name $surname"
            REF_DATABASE_ROOT
                .child(NODE_USERS)
                .child(CURRENT_UID)
                .child(FULL_NAME)
                .setValue(fullName).addOnCompleteListener {
                    if (it.isSuccessful){
                        showToast("Data update")
                        USER.fullname = fullName
                        (activity as MainActivity).onBackPressed()
                        hideKeyboard()
                    }
                }
        }
    }
}
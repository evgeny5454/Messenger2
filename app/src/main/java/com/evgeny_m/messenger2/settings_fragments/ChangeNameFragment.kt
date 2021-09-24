package com.evgeny_m.messenger2.settings_fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.R
import com.evgeny_m.messenger2.databinding.FragmentChangeNameBinding
import com.evgeny_m.messenger2.utilits.*


class ChangeNameFragment : Fragment() {

    private lateinit var binding: FragmentChangeNameBinding
    private lateinit var toolbar: Toolbar

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
        toolbar = binding.chandeFullnameFragmentToolbar

        toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.menu_save -> {
                    change()
                    APP_ACTIVITY.onBackPressed()
                    true
                }
                else -> false
            }
        }
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

    private fun change() {
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
                        //showToast("Data update")
                        USER.fullname = fullName
                        (activity as MainActivity).onBackPressed()
                        hideKeyboard()
                    }
                }
        }
    }
}
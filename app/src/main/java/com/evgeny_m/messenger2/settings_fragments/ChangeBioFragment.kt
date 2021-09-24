package com.evgeny_m.messenger2.settings_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.R
import com.evgeny_m.messenger2.databinding.FragmentChangeBioBinding
import com.evgeny_m.messenger2.utilits.*


class ChangeBioFragment : Fragment() {

    private lateinit var binding: FragmentChangeBioBinding
    private lateinit var toolbar:Toolbar

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

        toolbar = binding.chandeBioFragmentToolbar

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

    private fun change() {

        val newBio = binding.settingsInputBio.text.toString()
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(BIO)
            .setValue(newBio)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    //showToast("Data update")
                    USER.bio = newBio
                    (activity as MainActivity).onBackPressed()
                    hideKeyboard()
                }
        }
    }


}
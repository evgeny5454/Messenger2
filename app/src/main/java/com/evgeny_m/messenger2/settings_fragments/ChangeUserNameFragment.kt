package com.evgeny_m.messenger2.settings_fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.R
import com.evgeny_m.messenger2.databinding.FragmentChangeUserNameBinding
import com.evgeny_m.messenger2.utilits.*
import java.util.*


class ChangeUserNameFragment : Fragment() {

    private lateinit var binding: FragmentChangeUserNameBinding
    private lateinit var newUserName: String
    private lateinit var oldUserName: String
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initUserName()

        toolbar = binding.changeUsernameFragmentToolbar

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

    private fun initUserName() {
        binding.settingsInputUserName.setText(USER.username)
        oldUserName = USER.username
    }

    private fun change() {

        newUserName = binding.settingsInputUserName.text.toString().lowercase(Locale.getDefault())
        if (newUserName.isEmpty()){
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(NODE_USER_NAMES).addListenerForSingleValueEvent(AppValueEventListener{
                if (it.hasChild(newUserName)) {
                    showToast("Имя занято")
                } else {
                    changeUserName()
                }
            })

        }
    }

    private fun changeUserName() {
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(newUserName).setValue(CURRENT_UID).addOnCompleteListener {
            if (it.isSuccessful){
                updateCurrentUserName()
            }
        }
    }

    private fun updateCurrentUserName() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(USER_NAME).setValue(newUserName).addOnCompleteListener {
            if (it.isSuccessful){
                if (oldUserName.isEmpty()) {
                    addUserName()
                }else {
                    deleteOldUserName()
                }

            }else {
                showToast("Какието проблемы")
            }
        }
    }

    private fun addUserName() {
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(newUserName).setValue(CURRENT_UID).addOnCompleteListener {
            if (it.isSuccessful) {
                showToast("Данные обновлены")
                (activity as MainActivity).onBackPressed()
                hideKeyboard()
            }
        }
    }

    private fun deleteOldUserName() {
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(oldUserName).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                //showToast("Данные обновлены")
                (activity as MainActivity).onBackPressed()
                hideKeyboard()
            } else {
                //showToast("Какието проблемы")
            }
        }
    }
}
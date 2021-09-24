package com.evgeny_m.messenger2.fragments_main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.evgeny_m.messenger2.R
import com.evgeny_m.messenger2.activites.RegisterActivity
import com.evgeny_m.messenger2.databinding.FragmentSettingsBinding
import com.evgeny_m.messenger2.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onStart() {
        super.onStart()

    }



    override fun onResume() {
        super.onResume()
        toolbar = binding.settingsFragmentToolbar
        initFields()
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings_menu_exit -> {
                    AppStates.updateState(AppStates.OFFLINE)
                    AUTH.signOut()
                    replaceActivity(RegisterActivity())
                    true
                }
                R.id.settings_menu_change_photo ->{
                    changePhotoUser()
                    true
                }
                R.id.settings_menu_change_full_name -> {
                    view?.findNavController()?.navigate(R.id.action_navigation_settings_to_changeNameFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun initFields() {
        binding.settingsUserFullName.text = USER.fullname
        binding.settingsUserPhoneNumber.text = USER.phone
        binding.settingsUserName.text = "@${USER.username}"
        binding.settingsBio.text = USER.bio
        binding.settingsState.text = USER.state
        updateUserPhoto()

        binding.settingsChangeUserName.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_navigation_settings_to_changeUserNameFragment)
        }
        binding.settingsChangeBio.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_navigation_settings_to_changeBioFragment)
        }
    }

    private fun updateUserPhoto() {
        Glide
            .with(requireContext())
            .load(USER.photoUrl)
            .centerCrop()
            .into(binding.settingsUserPhoto)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }





    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = STORAGE
                .child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
                .child(CURRENT_UID)

            putImageToStorage(uri, path) {
                getUrlFromStorage(path){
                    putUrlToDatabase(it){
                        showToast("Фото загруженно и ссылка в базе данных")
                        USER.photoUrl = it
                        updateUserPhoto()
                        //initUser()
                    }
                }
            }
        }
    }
}
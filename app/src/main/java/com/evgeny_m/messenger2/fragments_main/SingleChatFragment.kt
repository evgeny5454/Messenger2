package com.evgeny_m.messenger2.fragments_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.evgeny_m.messenger2.databinding.FragmentSingleChatBinding
import com.evgeny_m.messenger2.fragments_main.ContactsFragment.Companion.ID_USER
import com.evgeny_m.messenger2.models.UserModel
import com.evgeny_m.messenger2.utilits.*
import com.google.firebase.database.DatabaseReference


class SingleChatFragment : Fragment() {

    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var toolbar: Toolbar
    private lateinit var listenerInfoToolbar: AppValueEventListener
    private lateinit var receivingUser: UserModel
    private lateinit var refUser:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        hideNavView()
        toolbar = binding.singleChatFragmentToolbar
        addBackButton(toolbar)

        listenerInfoToolbar = AppValueEventListener {
            receivingUser = it.getUserModel()
            initInfoToolbar()
        }

        refUser = REF_DATABASE_ROOT.child(NODE_USERS).child(ID_USER)
        refUser.addValueEventListener(listenerInfoToolbar)
    }

    private fun initInfoToolbar() {
        Glide
            .with(requireContext())
            .load(receivingUser.photoUrl)
            .centerCrop()
            .into(binding.singleChatFragmentUserImage)

        binding.singleChatFragmentUserFullName.text = receivingUser.fullname
        binding.singleChatStatus.text = receivingUser.state
    }

    override fun onPause() {
        super.onPause()
        refUser.removeEventListener(listenerInfoToolbar)
    }
}
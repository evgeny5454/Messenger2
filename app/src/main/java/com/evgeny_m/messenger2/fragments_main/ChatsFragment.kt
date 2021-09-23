package com.evgeny_m.messenger2.fragments_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger2.databinding.FragmentChatsBinding


class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentChatsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        //toolbar = binding.chatsFragmentToolbar

        //toolbar.title = "Chats"

        //NavHeaderFragment().bindingNavHeader.navHeaderProfileFullName.text = USER.fullname

        //addBurgerMenu(toolbar)



    }
}
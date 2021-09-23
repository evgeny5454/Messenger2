package com.evgeny_m.messenger2.fragments_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evgeny_m.messenger2.R



class GropeChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grope_chat, container, false)
    }

    override fun onStart() {
        super.onStart()

       // getBackButtonOnAppBar()

    }

    override fun onResume() {
        super.onResume()

       // getBackButtonOnAppBar()
    }
}
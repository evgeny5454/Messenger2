package com.evgeny_m.messenger2.fragments_main.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evgeny_m.messenger2.databinding.FragmentSingleChatBinding
import com.evgeny_m.messenger2.fragments_main.ContactsFragment.Companion.ID_USER
import com.evgeny_m.messenger2.models.CommonModel
import com.evgeny_m.messenger2.models.UserModel
import com.evgeny_m.messenger2.utilits.*
import com.google.firebase.database.DatabaseReference


class SingleChatFragment : Fragment() {

    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var toolbar: Toolbar
    private lateinit var listenerInfoToolbar: AppValueEventListener
    private lateinit var receivingUser: UserModel
    private lateinit var refUser: DatabaseReference

    private lateinit var refMessages: DatabaseReference
    private lateinit var adapter: SingleChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var messagesListener: AppValueEventListener
    private var listMessages = emptyList<CommonModel>()


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
        //initInfoToolbar()

        listenerInfoToolbar = AppValueEventListener {
            receivingUser = it.getUserModel()
            initInfoToolbar()

        }
        initRecycleView()


        listenerInfoToolbar = AppValueEventListener {
             receivingUser = it.getUserModel()
             initInfoToolbar()

             binding.chatBtnSend.setOnClickListener {
                 val message = binding.chatInputMessage.text.toString()

                 if (message.isEmpty()){
                     showToast("введите сообщение")
                 } else{
                     sendMessage(message, ID_USER, TYPE_TEXT){
                         binding.chatInputMessage.setText("")
                     }
                 }
             }
         }

         refUser = REF_DATABASE_ROOT.child(NODE_USERS).child(ID_USER)
         refUser.addValueEventListener(listenerInfoToolbar)
        binding.chatBtnSend.setOnClickListener {

            val message = binding.chatInputMessage.text.toString()

            if (message.isEmpty()) {
                showToast("введите сообщение")
            } else {
                sendMessage(message, ID_USER, TYPE_TEXT) {
                    binding.chatInputMessage.setText("")
                }
            }
        }
    }

    private fun initRecycleView() {
        recyclerView = binding.chatRecyclerView
        adapter = SingleChatAdapter()
        refMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGE)
            .child(CURRENT_UID)
            .child(ID_USER)

        recyclerView.adapter = adapter

        messagesListener = AppValueEventListener { dataSnapshot ->
            listMessages = dataSnapshot.children.map {
                it.getCommonModel()
            }
            adapter.setList(listMessages)
            recyclerView.smoothScrollToPosition(adapter.itemCount)
        }
        refMessages.addValueEventListener(messagesListener)
    }

    /*private fun sendMessage(message: String, idUser: String, typeText: String, function: () -> Unit) {

    }*/

    private fun initInfoToolbar() {


        /*refUser = REF_DATABASE_ROOT.child(NODE_USERS).child(ID_USER)
        refUser.addValueEventListener(listenerInfoToolbar)*/


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
        refMessages.removeEventListener(messagesListener)
    }
}
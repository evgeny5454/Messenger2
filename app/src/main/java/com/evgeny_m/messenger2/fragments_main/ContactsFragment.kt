package com.evgeny_m.messenger2.fragments_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.evgeny_m.messenger2.R
import com.evgeny_m.messenger2.databinding.FragmentContactsBinding
import com.evgeny_m.messenger2.models.CommonModel
import com.evgeny_m.messenger2.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var refContacts: DatabaseReference
    private lateinit var refUsers: DatabaseReference
    private lateinit var refUsersListener: AppValueEventListener
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()
    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        //getBackButtonOnAppBar()
        initRecyclerView()
    }

    private fun initRecyclerView() {

        recyclerView = binding.contactsRecyclerView
        refContacts = REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(refContacts, CommonModel::class.java).build()

        adapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_item, parent, false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                refUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                refUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()

                    holder.userName.text = contact.fullname
                    holder.status.text = contact.state
                    val url = contact.photoUrl

                    Glide
                        .with(requireContext())
                        .load(url)
                        .placeholder(R.drawable.ic_baseline_add_a_photo_24)
                        .centerCrop()
                        .into(holder.photo)

                    holder.itemView.setOnClickListener {
                        ID_USER = contact.id
                        view?.findNavController()

                            ?.navigate(
                                R.id.action_navigation_contacts_to_singleChatFragment,
                                bundle
                            )
                    }
                }
                refUsers.addValueEventListener(refUsersListener)
                mapListeners[refUsers] = refUsersListener
            }
        }
        recyclerView.adapter = adapter
        adapter.startListening()
    }

    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName = view.findViewById<TextView>(R.id.item_user_full_name)
        val status = view.findViewById<TextView>(R.id.item_user_state)
        val photo = view.findViewById<ImageView>(R.id.item_profile_photo)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        adapter.stopListening()
        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }
    }

    companion object{
         lateinit var ID_USER : String
    }
}

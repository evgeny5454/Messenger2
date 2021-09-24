package com.evgeny_m.messenger2.utilits

import android.content.Intent
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger2.models.CommonModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(requireContext(), activity::class.java)
    startActivity(intent)
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
}

fun initContacts() {
    if (checkPermission(READ_CONTACTS)) {
        var arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val fullName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullName
                newModel.phone = phone.replace(Regex("[\\s,()-]"), "")
                //newModel.phone = phone.replace(Regex("8"),"+7")
                if (newModel.phone[0].toString() == "8") {
                    newModel.phone = newModel.phone.replaceFirst("8", "+7")
                }
                arrayContacts.add(newModel)
            }
        }
        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }
}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
            it.children.forEach { snapshot ->
                arrayContacts.forEach { contact ->
                    if (snapshot.key == contact.phone) {
                        REF_DATABASE_ROOT
                            .child(NODE_PHONE_CONTACTS)
                            .child(CURRENT_UID)
                            .child(snapshot.value.toString())
                            .child(ID)
                            .setValue(snapshot.value.toString())
                    }
                }
            }
        })
    }
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}


package com.evgeny_m.messenger2.utilits

import android.net.Uri
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import com.evgeny_m.messenger2.MainActivity
import com.evgeny_m.messenger2.models.CommonModel
import com.evgeny_m.messenger2.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var USER: UserModel
lateinit var CURRENT_UID: String
var STORAGE = FirebaseStorage.getInstance().reference
lateinit var REF_DATABASE_ROOT: DatabaseReference


const val NODE_USERS = "users"
const val NODE_USER_NAMES = "usernames"
const val ID = "id"
const val BIO = "bio"
const val PHONE = "phone"
const val NODE_PHONE_CONTACTS = "phone_contacts"
const val USER_NAME = "username"
const val FULL_NAME = "fullname"
const val PHOTO_URI = "photoUrl"
const val STATE = "state"
const val NODE_PHONES = "phones"

const val FOLDER_PROFILE_IMAGE = "profile_image"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    USER = UserModel()
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT
        .child(NODE_USERS)
        .child(CURRENT_UID)
        .child(PHOTO_URI).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener {}
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener {}
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener {}
}

fun MainActivity.initUser() {
    REF_DATABASE_ROOT
        .child(NODE_USERS)
        .child(CURRENT_UID)
        .addValueEventListener(AppValueEventListener {
            USER = it.getValue(UserModel::class.java) ?: UserModel()
        })
}
fun Fragment.initUser() {
    REF_DATABASE_ROOT
        .child(NODE_USERS)
        .child(CURRENT_UID)
        .addValueEventListener(AppValueEventListener {
            USER = it.getValue(UserModel::class.java) ?: UserModel()
        })
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
                val fullName = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullName
                newModel.phone = phone.replace(Regex("[\\s,()-]"),"")
                //newModel.phone = phone.replace(Regex("8"),"+7")
                arrayContacts.add(newModel)
            }
        }
        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }
}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener{
        it.children.forEach{ snapshot ->
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

fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()


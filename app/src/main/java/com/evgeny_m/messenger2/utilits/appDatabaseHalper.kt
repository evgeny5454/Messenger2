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
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var USER: UserModel
lateinit var CURRENT_UID: String
var STORAGE = FirebaseStorage.getInstance().reference
lateinit var REF_DATABASE_ROOT: DatabaseReference

const val TYPE_TEXT = "text"


const val NODE_USERS = "users"
const val NODE_USER_NAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_MESSAGE= "messages"

const val ID = "id"
const val BIO = "bio"
const val PHONE = "phone"
const val NODE_PHONE_CONTACTS = "phone_contacts"
const val USER_NAME = "username"
const val FULL_NAME = "fullname"
const val PHOTO_URI = "photoUrl"
const val STATE = "state"


const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timeStamp"

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

/*fun initContacts() {
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
}*/



fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun sendMessage(message: String, receivingUserId: String, typeText: String, function: () -> Unit) {

    val dialogUser = "$NODE_MESSAGE/$CURRENT_UID/$receivingUserId"
    val dialogReceivingUser = "$NODE_MESSAGE/$receivingUserId/$CURRENT_UID"
    val messegeKey = REF_DATABASE_ROOT.child(dialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()

    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$dialogUser/$messegeKey"] = mapMessage
    mapDialog["$dialogReceivingUser/$messegeKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener {  }
}

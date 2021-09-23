package com.evgeny_m.messenger2.utilits

enum class AppStates(val state: String) {
    ONLINE("online"),
    OFFLINE("offline"),
    TYPING("is typing...");

    companion object{

        fun updateState(appStates: AppStates){
            REF_DATABASE_ROOT
                .child(NODE_USERS)
                .child(CURRENT_UID)
                .child(STATE)
                .setValue(appStates.state)
                .addOnSuccessListener {
                    USER.state = appStates.state
                }
        }
    }
}
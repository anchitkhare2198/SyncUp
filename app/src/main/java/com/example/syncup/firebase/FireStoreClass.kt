package com.example.syncup.firebase

import com.example.syncup.activities.SignInActivity
import com.example.syncup.activities.SignUpActivity
import com.example.syncup.models.Users
import com.example.syncup.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private var mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: Users){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }

    fun getCurrentUserId(): String{
        var currenUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currenUser!=null){
            currentUserId = currenUser.uid
        }
        return currentUserId
    }

    fun signInUser(activity: SignInActivity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(Users::class.java)
                if(loggedInUser!= null){
                    activity.signInSuccess(loggedInUser)
                }
            }
    }
}
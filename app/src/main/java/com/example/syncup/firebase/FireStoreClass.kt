package com.example.syncup.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.syncup.activities.HomepageActivity
import com.example.syncup.activities.MyProfileActivity
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

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String,Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName,"Profile Updated Successfully")
                Toast.makeText(activity, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                activity.updateProfile()
            }.addOnFailureListener{
                e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error!!",e)
                Toast.makeText(activity, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }
    }

    fun baseUserDetails(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(Users::class.java)!!

                when(activity){
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is HomepageActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser)
                    }
                    is MyProfileActivity ->{
                        activity.showDetails(loggedInUser)
                    }
                }

            }
            .addOnFailureListener{
                e ->
                when(activity){
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is HomepageActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("SignInUser","Error writing document",e)
            }
    }
}
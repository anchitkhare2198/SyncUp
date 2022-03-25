package com.example.syncup.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.syncup.R
import com.example.syncup.databinding.ActivityMyProfileBinding
import com.example.syncup.firebase.FireStoreClass
import com.example.syncup.models.Users
import com.example.syncup.utils.Constants
import com.example.syncup.utils.Constants.getFileExtension
import com.example.syncup.utils.Constants.selectImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


class MyProfileActivity : BaseActivity() {

    private var binding: ActivityMyProfileBinding? = null



    private var mSelectedImage: Uri? = null
    private var ProfileImageURL: String = ""
    private lateinit var mUserDetails: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        showDialog("Please wait!!")
        FireStoreClass().baseUserDetails(this)

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.userImage?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED ){
                selectImage(this)
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding?.btnUpdate?.setOnClickListener {
            if(mSelectedImage!=null){
                uploadImage()
            }else{
                showDialog("Please Wait!!")
                updateUserProfileData()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage(this)
            }
        }else{
            Toast.makeText(this, "Oops!! You denied the permissions.", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null){
            mSelectedImage = data.data

            try {
                Glide
                    .with(this)
                    .load(mSelectedImage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(binding?.userImage!!)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun showDetails(user: Users){
        mUserDetails = user
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding?.userImage!!)
        binding?.etName?.setText(user.name)
        binding?.etEmail?.setText(user.email)
        if(user.mobile != 0L)
            binding?.etMobile?.setText(user.mobile.toString())
        hideProgressDialog()
    }

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String,Any>()
        if(ProfileImageURL.isNotEmpty() && ProfileImageURL!=mUserDetails.image){
            userHashMap[Constants.IMAGE] = ProfileImageURL
        }
        if(binding?.etName?.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = binding?.etName?.text.toString()
        }
        if(binding?.etMobile?.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = binding?.etMobile?.text.toString().toLong()
        }

        FireStoreClass().updateUserProfileData(this,userHashMap)
    }



    private fun uploadImage(){
        showDialog("Please wait!!")
        if(mSelectedImage!=null){
            val sRef: StorageReference = FirebaseStorage
                .getInstance().reference.child(FireStoreClass().getCurrentUserId()+
                        "."+getFileExtension(this,mSelectedImage))

            sRef.putFile(mSelectedImage!!).addOnSuccessListener {
                taskSnapshot ->
                Log.i("Image URL"
                    ,taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.i("Downloadable Image Uri", uri.toString())
                    ProfileImageURL = uri.toString()
                    updateUserProfileData()
                }
            }.addOnFailureListener{
                exception ->
                hideProgressDialog()
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateProfile(){
        hideProgressDialog()
        setResult(RESULT_OK)
        finish()
    }
}
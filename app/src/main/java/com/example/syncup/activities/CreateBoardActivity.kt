package com.example.syncup.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.syncup.R
import com.example.syncup.databinding.ActivityCreateBoardBinding
import com.example.syncup.firebase.FireStoreClass
import com.example.syncup.models.Board
import com.example.syncup.utils.Constants
import com.example.syncup.utils.Constants.PICK_IMAGE_REQUEST_CODE
import com.example.syncup.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.example.syncup.utils.Constants.selectImage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class CreateBoardActivity : BaseActivity() {
    private var binding: ActivityCreateBoardBinding? = null

    private var mSelectedImage: Uri? = null
    private var BoardImageURL: String = ""

    private lateinit var mUserName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if(intent.hasExtra(Constants.NAME)){
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.boardImage?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED ){
                selectImage(this)
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }


        binding?.btnCreate?.setOnClickListener {
            val boardName: String = binding?.etBoardName?.text.toString().trim{it <= ' '}
            if(boardName.isNullOrEmpty()){
                binding?.etBoardName?.setError("Please Enter a board name")
            }else{
                if(mSelectedImage!=null){
                    uploadImage()
                }else{
                    showDialog("Creating Board..")
                    createBoard()
                }
            }
        }
    }

    fun CreateBoardSuccess(){
        hideProgressDialog()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ_STORAGE_PERMISSION_CODE){
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
            && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data != null){
            mSelectedImage = data.data

            try {
                Glide
                    .with(this)
                    .load(mSelectedImage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(binding?.boardImage!!)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(){
        showDialog("Please wait!!")
        if(mSelectedImage!=null){
            val sRef: StorageReference = FirebaseStorage
                .getInstance().reference.child("BOARD_IMAGE"+
                    System.currentTimeMillis()+
                        "."+ Constants.getFileExtension(this, mSelectedImage)
                )

            sRef.putFile(mSelectedImage!!).addOnSuccessListener {
                    taskSnapshot ->
                Log.i("Board Image URL"
                    ,taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                    Log.i("Downloadable Image Uri", uri.toString())
                    BoardImageURL = uri.toString()
                    createBoard()
                }
            }.addOnFailureListener{
                    exception ->
                hideProgressDialog()
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createBoard(){
        val assignedUserArrayList: ArrayList<String> = ArrayList()
        assignedUserArrayList.add(getCurrentUserId())

         var board = Board(
             binding?.etBoardName?.text.toString(),
             BoardImageURL,
             mUserName,
             assignedUserArrayList
         )

        FireStoreClass().createBoard(this,board)
    }
}
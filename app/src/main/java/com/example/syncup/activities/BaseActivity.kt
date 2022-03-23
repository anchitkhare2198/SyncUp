package com.example.syncup.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.syncup.R
import com.example.syncup.databinding.ActivityBaseBinding
import com.example.syncup.databinding.DialogProgressBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

open class BaseActivity : AppCompatActivity() {
    private var binding: ActivityBaseBinding? = null
    private var ProgressBinding:DialogProgressBinding? = null
    private var doubleBackPressed = false
    private lateinit var mProgressDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

    }

    fun showDialog(text: String){
        mProgressDialog = Dialog(this)
        ProgressBinding = DialogProgressBinding.inflate(layoutInflater)
        mProgressDialog.setContentView(ProgressBinding?.root!!)
        ProgressBinding?.tvProgressText?.text = text
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun getCurrentUserId(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit(){
        if(doubleBackPressed){
            super.onBackPressed()
            return
        }

        this.doubleBackPressed = true
        Toast.makeText(this, "Please Click back again to exit!", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({doubleBackPressed = false},2500)
    }

//    fun showErrorSnackBar(message: String){
//        val snackbar = Snackbar.make(findViewById(androidx.appcompat.R.id.content), message, Snackbar.LENGTH_LONG)
//        val snackbarView = snackbar.view
//        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error_color))
//        snackbar.show()
//    }

}
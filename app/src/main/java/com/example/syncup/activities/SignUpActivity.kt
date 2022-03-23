package com.example.syncup.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.syncup.R
import com.example.syncup.databinding.ActivitySignUpBinding
import com.example.syncup.databinding.ActivitySplashScreenBinding
import com.example.syncup.firebase.FireStoreClass
import com.example.syncup.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {

    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val typeFace : Typeface = Typeface.createFromAsset(assets,"Mangabey.ttf")
        binding?.tvAppName?.typeface = typeFace

        setSupportActionBar(binding?.toolbarSignUpActivity)
        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.btnSignUp?.setOnClickListener {
            registerUser()
        }

    }

    fun userRegisteredSuccess(){
        Toast.makeText(this, "You have Successfully Registered!!", Toast.LENGTH_SHORT).show()
        hideProgressDialog()
//        startActivity(Intent(this,HomepageActivity::class.java))
//        finish()
    }

    private fun registerUser(){
        val name:String = binding?.etName?.text.toString().trim{it <= ' '}
        val email:String = binding?.etEmail?.text.toString().trim{it <= ' '}
        val password:String = binding?.etPassword?.text.toString().trim{it <= ' '}

        if (validateForm(name,email, password)){
//            Toast.makeText(this, "Working!!", Toast.LENGTH_SHORT).show()
            showDialog("Please Wait!!")
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = Users(firebaseUser.uid,name,registeredEmail)
                        FireStoreClass().registerUser(this,user)
                    } else {
                        Toast.makeText(
                            this, task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(name:String,email:String,password:String):Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                //showErrorSnackBar("Please Enter a Name")
                binding?.etName?.error = "Please Enter a Name"
                false
            }
            TextUtils.isEmpty(email)->{
//                showErrorSnackBar("Please Enter an Email")
                binding?.etEmail?.error = "Please Enter an Email"
                false
            }
            TextUtils.isEmpty(password)->{
//                showErrorSnackBar("Please Enter a Password")
                binding?.etPassword?.error = "Please Enter a Password"
                false
            }
            else -> {
                true
            }
        }
    }

}
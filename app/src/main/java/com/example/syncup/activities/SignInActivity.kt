package com.example.syncup.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.syncup.databinding.ActivitySignInBinding
import com.example.syncup.firebase.FireStoreClass
import com.example.syncup.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : BaseActivity() {

    private var binding: ActivitySignInBinding? = null
//    private val sharedPrefFile = "sharedPreference"
//    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val typeFace: Typeface = Typeface.createFromAsset(assets, "Mangabey.ttf")
        binding?.tvAppName?.typeface = typeFace

//        sharedPreferences = this.getSharedPreferences(sharedPrefFile,
//            Context.MODE_PRIVATE)

        setSupportActionBar(binding?.toolbarSignInActivity)
        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.btnSignIn?.setOnClickListener {
            signInUser()
        }
    }

    fun signInSuccess(user: Users){
        hideProgressDialog()
        var intent = Intent(this,HomepageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()

    }

    private fun signInUser() {
        val email: String = binding?.etEmail?.text.toString().trim { it <= ' ' }
        val password: String = binding?.etPassword?.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            showDialog("Signing In!!")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
//                        val editor:SharedPreferences.Editor =  sharedPreferences!!.edit()
//                        editor.putString("USER", firebaseUser.toString())
//                        editor.putString("EMAIL",registeredEmail)
//                        editor.apply()
//                        editor.commit()
                        FireStoreClass().baseUserDetails(this)
                    } else {
                        Toast.makeText(
                            this, task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
//                showErrorSnackBar("Please Enter an Email")
                binding?.etEmail?.error = "Please Enter an Email"
                false
            }
            TextUtils.isEmpty(password) -> {
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


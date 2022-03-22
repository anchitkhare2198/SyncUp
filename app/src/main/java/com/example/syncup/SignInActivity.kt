package com.example.syncup

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.syncup.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private var binding:ActivitySignInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val typeFace : Typeface = Typeface.createFromAsset(assets,"Mangabey.ttf")
        binding?.tvAppName?.typeface = typeFace

        setSupportActionBar(binding?.toolbarSignInActivity)
        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }
}
package com.example.syncup

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.syncup.databinding.ActivitySignUpBinding
import com.example.syncup.databinding.ActivitySplashScreenBinding

class SignUpActivity : AppCompatActivity() {

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
    }
}
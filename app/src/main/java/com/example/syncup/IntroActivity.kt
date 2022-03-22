package com.example.syncup

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.syncup.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private var binding: ActivityIntroBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val typeFace : Typeface = Typeface.createFromAsset(assets,"Mangabey.ttf")
        binding?.tvAppName?.typeface = typeFace

        binding?.btnSignUpIntro?.setOnClickListener(View.OnClickListener {
            var i = Intent(this, SignUpActivity::class.java)
            startActivity(i)
        })

        binding?.btnSignInIntro?.setOnClickListener(View.OnClickListener {
            var i = Intent(this, SignInActivity::class.java)
            startActivity(i)
        })
    }
}
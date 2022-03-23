package com.example.syncup.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.syncup.databinding.ActivitySplashScreenBinding
import com.example.syncup.firebase.FireStoreClass

class SplashScreen : BaseActivity() {

    private var binding: ActivitySplashScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val typeFace : Typeface = Typeface.createFromAsset(assets,"Mangabey.ttf")
        binding?.tvAppName?.typeface = typeFace

        Handler().postDelayed({

            var currentUserId = FireStoreClass().getCurrentUserId()
            if(currentUserId.isNotEmpty()){
                startActivity(Intent(this, HomepageActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }
        },2500)
    }
}
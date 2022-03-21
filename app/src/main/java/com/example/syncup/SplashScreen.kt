package com.example.syncup

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.syncup.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private var binding: ActivitySplashScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val typeFace : Typeface = Typeface.createFromAsset(assets,"carbon bl.ttf")
        binding?.tvAppName?.typeface = typeFace
    }
}
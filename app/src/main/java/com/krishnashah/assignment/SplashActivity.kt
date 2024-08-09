package com.krishnashah.assignment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.krishnashah.assignment.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    //access views from the layout
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivitySplashBinding.inflate(layoutInflater)

        // Set the content view to the root of the binding
        setContentView(binding.root)

        // Load the animation defined in the splash_animation.xml resource file
        val animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)

        //animation on the splash logo
        binding.splashLogo.startAnimation(animation)

        //transition to MainActivity by 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the MainActivity
            startActivity(Intent(this, MainActivity::class.java))

            // Finish
            finish()
        }, 5000)
    }
}

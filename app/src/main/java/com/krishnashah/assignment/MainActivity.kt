package com.krishnashah.assignment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.krishnashah.assignment.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    // Lateinit variables for view binding and Firebase Authentication
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // user is not signed
        if (auth.currentUser == null) {
            // If no user is signed in, navigate to the RegisterActivity
            navigateToRegister()
            return
        }

        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // sign-in button to navigate to RegisterActivity
        binding.signIn.setOnClickListener {
            navigateToRegister()
        }

        // sign-out button to sign out the user
        binding.signOut.setOnClickListener {
            auth.signOut()
            // Sign out from Firebase
            updateUI(null)
        // Update the UI to reflect that the user is signed out
        }

        // Update the UI with the current user's email
        updateUI(auth.currentUser?.email)
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    // Finish
    }


    private fun updateUI(email: String?) {
        // If the email is not null, display it;
        binding.userDetails.text = email?.let { "Email: $it" } ?: getString(R.string.no_data)
    }
}

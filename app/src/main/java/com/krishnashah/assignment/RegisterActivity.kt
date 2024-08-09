package com.krishnashah.assignment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.krishnashah.assignment.databinding.ActivityRegisterBinding
import com.krishnashah.assignment.model.UserModel


class RegisterActivity : AppCompatActivity() {

    // Lateinit variable for view binding to access views from the layout
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        // Set the content view to root of the binding
        setContentView(binding.root)

        // Set the action bar title
        supportActionBar?.title = "Register"

        // Configure Google Sign-In options to request
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            // Request the ID token from Google's authentication service
            .requestEmail()
            // Request the user's email
            .build()

        // Create a Google Sign-In client
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set a click listener on the login
        binding.loginTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Set a click listener on the create account button to initiate email/password registration
        binding.createAccountBtn.setOnClickListener {
            val email = binding.emailRegister.text.toString()
            val password = binding.passwordRegister.text.toString()
            val userModel = UserModel(email, password)

            // Check email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                createAccount(userModel)
            }
        }

        // Google sign-in button to initiate Google sign-in
        binding.googleBtn.setOnClickListener {
            // Sign out any previously signed-in Google account to avoid conflicts
            googleSignInClient.signOut()

            // Start the Google sign-in intent
            startActivityForResult(googleSignInClient.signInIntent, 13)
        }
    }

    // Firebase Authentication with the provided email and password
    private fun createAccount(userModel: UserModel) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userModel.email, userModel.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // If the account is created successfully, navigate to LoginActivity
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }.addOnFailureListener {
                // Show an error message if account creation fails
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    //the Google sign-in intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Google sign-in and is successful
        if (requestCode == 13 && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Get the Google account from the sign-in result
                val account = task.getResult(ApiException::class.java)!!

                // Authenticate with Firebase using the Google account's ID token
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Handle exception if sign-in fails
                Toast.makeText(this, "Google Sign-In Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // navigate to MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }.addOnFailureListener {
                // Show an error message if authentication fails
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }
}

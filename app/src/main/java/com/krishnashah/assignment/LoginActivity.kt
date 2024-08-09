package com.krishnashah.assignment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.krishnashah.assignment.databinding.ActivityLoginBinding
import com.krishnashah.assignment.model.UserModel


class LoginActivity : AppCompatActivity() {

    //access views from the layout
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Set the content view to the root of the binding
        setContentView(binding.root)

        // Set the title
        supportActionBar?.title = "Login"

        //navigate to the RegisterActivity
        binding.registerTV.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java)) // Start the RegisterActivity
            finish()
        // Finish
        }

        // login button to initiate the login process
        binding.loginBtn.setOnClickListener {
            // Get the email and password from the EditText fields
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()

            // store the user's email and password
            val userModel = UserModel(email, password)

            // email and password fields are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(userModel)
            // Call the signIn function to authenticate the user
            }
        }
    }


    private fun signIn(userModel: UserModel) {
        // Use FirebaseAuth to sign in with the provided email and password
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userModel.email, userModel.password)
            .addOnCompleteListener { task ->
                // navigate to MainActivity
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                // Finish
                }
            }
            // sign-in fails
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }
}

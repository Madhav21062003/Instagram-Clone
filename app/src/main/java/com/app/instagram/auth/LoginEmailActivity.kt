package com.app.instagram.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.instagram.HomeActivity
import com.app.instagram.R
import com.app.instagram.Utils
import com.app.instagram.databinding.ActivityLoginEmailBinding
import com.google.firebase.auth.FirebaseAuth

class LoginEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginEmailBinding

    companion object {
        private const val TAG = "LOGIN_TAG"
    }

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.toolbarBackBtn.setOnClickListener {
            onBackPressed()
        }

        // Not have account Go to Register Screen
        binding.noAccount.setOnClickListener {
            startActivity(Intent(this, RegisterEmailActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            validateData()
        }

    }

    private var email = ""
    private var password = ""

    private fun validateData() {

        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        Log.d(TAG, "validateData: email $email")
        Log.d(TAG, "validateData: password $password")

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.error = "Invalid Email address"
            binding.emailEt.requestFocus()
        }

        else if (email.isEmpty()){
            binding.emailEt.error = "Enter email"
            binding.emailEt.requestFocus()
        }
        else if (password.isEmpty()){
            binding.passwordEt.error = "Enter Password"
            binding.passwordEt.requestFocus()
        }

        else {
            loginUser()
        }
    }

    private fun loginUser() {
        Log.d(TAG, "loginUser: ")

        // Show progress
        progressDialog.setMessage("Logging In")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // user login successful
                Log.d(TAG, "loginUser: Logged In...")
                progressDialog.dismiss()

                Utils.toast(this@LoginEmailActivity, "Logged In Successfully")

                // start MainActivity
                startActivity(Intent(this, HomeActivity::class.java))
                finish()  // finish current and all activities from backstack

            }
            .addOnFailureListener { e ->
                Log.e(TAG, "LoginUSer: ", e)
                // user login failed
                progressDialog.dismiss()
                Utils.toast(this@LoginEmailActivity, "Unable to login due to ${e.message}")
            }
    }
}
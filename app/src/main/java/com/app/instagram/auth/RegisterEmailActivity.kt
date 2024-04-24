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
import com.app.instagram.databinding.ActivityRegisterEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEmailBinding

    private companion object{
        private const val TAG = "REGISTER_TAG"
    }

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityRegisterEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener{
            validateData()
        }

    }

    private var email = ""
    private var password = ""
    private var cPassword = ""

    private fun validateData() {
        // input data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        cPassword = binding.confirmPasswordEt.text.toString().trim()
        
        //
        Log.e(TAG, "validateData: email: $email", )
        Log.e(TAG, "validateData: password: $password", )
        Log.e(TAG, "validateData: confirm Password: $cPassword", )

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.error = "Invalid email pattern"
            binding.emailEt.requestFocus()
        }
        else if (email.isEmpty()){
            binding.emailEt.error = "Enter Email"
            binding.emailEt.requestFocus()
        }

        else if (password.isEmpty()){
            binding.passwordEt.error = "Enter Password"
            binding.passwordEt.requestFocus()
        }
        else if (cPassword.isEmpty()){
            binding.confirmPasswordEt.error = "Enter Confirm Password"
            binding.confirmPasswordEt.requestFocus()
        }
        else if (password == cPassword){
            registerUser()
        }
        else{
            binding.confirmPasswordEt.error = "Password does not match"
            binding.confirmPasswordEt.requestFocus()
        }

    }

    private fun registerUser() {
        Log.d(TAG, "registerUser: ")

        // shoe progress
        progressDialog.setMessage("Creating account")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d(TAG, "registerUser: Resgister Success")
                updateUserInfo()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Log.d(TAG, "registerUser: Register Failed:",e)
                Utils.toast(this, "Failed to register due to ${e.message}")
            }

    }

    private fun updateUserInfo() {
        Log.d(TAG, "updateUserInfo: ")

        // change progress dialog message
        progressDialog.setMessage("Saving User Info")

        val timeStamp = Utils.getTimeStamp()
        val userEmail = firebaseAuth.currentUser!!.email
        val registerUserUid = firebaseAuth.uid

        // setup data to save in firebase realtime db , most of the data will be empty and will set in edit profile
        val hashMap = HashMap<String,Any>()
        hashMap["name"] = ""
        hashMap["phoneCode"] = ""
        hashMap["phoneNumber"] = ""
        hashMap["profileImageUrl"] = ""
        hashMap["dob"] = ""
        hashMap["userType"] = "Email" // possible values Email/phone/Google
        hashMap["typingTo"] = ""
        hashMap["timeStamp"] = timeStamp
        hashMap["onlineStatus"] = true
        hashMap["email"] = "$userEmail"
        hashMap["uid"] = "$registerUserUid"
        hashMap["bio"] = ""


        // set data to firebase db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(registerUserUid!!)
            .setValue(hashMap)
            .addOnSuccessListener {

                // Firebase Db save success
                Log.d(TAG, "updateUserInfo: userRegistered")
                progressDialog.dismiss()

                Utils.toast(this,"Account Created Successfully")
                // Start HomeActivity
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener {e->
                // Firebase db save failed
                Log.e(TAG, "updateUserInfo: Register failed",e )
                progressDialog.dismiss()
                Utils.toast(this,"Failed due to ${e.message}")
            }

    }
}
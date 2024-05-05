package com.app.instagram.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.instagram.HomeActivity
import com.app.instagram.R
import com.app.instagram.Utils
import com.app.instagram.databinding.ActivityAuthOptionsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class AuthOptionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthOptionsBinding

    private companion object {
        // To show logs
        private const val TAG = "LOGIN_OPTIONS_TAG"
    }

    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val Req_Code: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        FirebaseApp.initializeApp(this)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_webclient_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)


        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Auth Using Email
        binding.btnLoginEmail.setOnClickListener {
            startActivity(Intent(this, LoginEmailActivity::class.java))
        }

        // Auth Using Phone Number
        binding.btnPhoneLogin.setOnClickListener {
            startActivity(Intent(this, LoginPhoneActivity::class.java))
        }

        // Auth using Google Sign In
        binding.btnGoogleSignIn.setOnClickListener {
            beginGoogleLogin()
        }

    }

    private fun beginGoogleLogin() {
        Log.d(TAG, "beginGoogleLogin: ")

        // intent  to launch google Sign In Options Dialog
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)

            if (account != null){
                UpdateUI(account)
            }
        }
        catch (e:ApiException){
            Utils.toast(this, e.toString())
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->

            if (task.isSuccessful){
                updateUSerInfoDb()
            }

        }
    }

    private fun updateUSerInfoDb() {
        Log.d(TAG, "updateUSerInfoDb: ")

        progressDialog.setMessage("Saving User Info...")
        progressDialog.show()

        // get current timestamp e.g to show user registration date/time
        val timestamp = Utils.getTimeStamp()
        val registeredUserEmail = firebaseAuth.currentUser?.email   // get email of registered user
        val registeredUSerUid = firebaseAuth.uid // get uid of registered user

        val name = firebaseAuth.currentUser?.displayName // Since each google user has same name so we can get it to save in firebase db

        // setup data to save in firebase realtime db , most of the data will be empty and will set in edit profile
        val hashMap = HashMap<String, Any>()
        hashMap["name"] = "$name"
        hashMap["phoneCode"] = ""
        hashMap["phoneNumber"] = ""
        hashMap["profileImageUrl"] = ""
        hashMap["dob"] = ""
        hashMap["userType"] = "Google" // possible values Email/phone/Google
        hashMap["typingTo"] = ""
        hashMap["timeStamp"] = timestamp
        hashMap["onlineStatus"] = true
        hashMap["email"] = "$registeredUserEmail"
        hashMap["uid"] = "$registeredUSerUid"
        hashMap["bio"] = ""

        // set data to firebase db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(registeredUSerUid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "updateUSerInfoDb: User info saved")
                Utils.toast(this, "Logged In")
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Log.e(TAG, "updateUSerInfoDb: ", e)
                Utils.toast(this, "Failed to save user info due to ${e.message}")
            }
    }

}


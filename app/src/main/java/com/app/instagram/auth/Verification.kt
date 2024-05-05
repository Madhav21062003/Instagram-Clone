package com.app.instagram.auth

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.app.instagram.R
import com.app.instagram.fragments.HomeFragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class verification : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var verifybtn:Button
    private lateinit var resendOTP:TextView
    private lateinit var OTP1:EditText
    private lateinit var OTP2:EditText
    private lateinit var OTP3:EditText
    private lateinit var OTP4:EditText
    private lateinit var OTP5:EditText
    private lateinit var OTP6:EditText

    private lateinit var OTP:String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber:String
    //private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        OTP=intent.getStringExtra("OTP").toString()
        resendToken=intent.getParcelableExtra("resendToken")!!
        phoneNumber=intent.getStringExtra("phoneNumber")!!
        //textView.setText(String.format("+91",intent.getStringExtra("number")))


        init()
        addTextChangeListener()
        resendOTPVisibility()

        resendOTP.setOnClickListener{
            ResendVerification()
            resendOTPVisibility()

        }

        verifybtn.setOnClickListener{
            val typeOTP=(OTP1.text.toString()+OTP2.text.toString()+OTP3.text.toString()
                    +OTP4.text.toString()+OTP5.text.toString()+OTP6.text.toString())

            if(typeOTP.isNotEmpty()){
                if(typeOTP.length==6){
                    val credential:PhoneAuthCredential=PhoneAuthProvider.getCredential(
                        OTP,typeOTP
                    )
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(this,"Please Enter Correct OTP",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Please Enter Correct OTP",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resendOTPVisibility(){
        OTP1.setText("")
        OTP2.setText("")
        OTP3.setText("")
        OTP4.setText("")
        OTP5.setText("")
        OTP6.setText("")
        resendOTP.visibility=View.INVISIBLE
        resendOTP.isEnabled=false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            resendOTP.visibility=View.VISIBLE
            resendOTP.isEnabled=true
        },40000)
    }

    private fun ResendVerification(){
        val options = PhoneAuthOptions.newBuilder(auth)//Pass the authentication
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.d("TAG", "onVerificationFailed: ${e.toString()}")

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")

            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")

            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            OTP=verificationId
            resendToken=token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    Toast.makeText(this,"Authenticate successfully",Toast.LENGTH_SHORT).show()
                    sendToMain()

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure:${ task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }
    private fun sendToMain(){
        startActivity(Intent(this, HomeFragment::class.java))
    }

    private fun addTextChangeListener(){
        OTP1.addTextChangedListener(TextJump(OTP1))
        OTP2.addTextChangedListener(TextJump(OTP2))
        OTP3.addTextChangedListener(TextJump(OTP3))
        OTP4.addTextChangedListener(TextJump(OTP4))
        OTP5.addTextChangedListener(TextJump(OTP5))
        OTP6.addTextChangedListener(TextJump(OTP6))
    }
    private fun init(){
        auth=FirebaseAuth.getInstance()
        verifybtn=findViewById(R.id.Verifybutton)
        resendOTP=findViewById(R.id.resend)
        OTP1=findViewById(R.id.otp1)
        OTP2=findViewById(R.id.otp2)
        OTP3=findViewById(R.id.otp3)
        OTP4=findViewById(R.id.otp4)
        OTP5=findViewById(R.id.otp5)
        OTP6=findViewById(R.id.otp6)
    }

    //Made class for jumping the cursor automatically from 1 edit text to another edit text
    inner class TextJump(private val view: View):TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            val text=s.toString()
            when(view.id){
                R.id.otp1->if(text.length==1) OTP2.requestFocus()
                R.id.otp2->if(text.length==1) OTP3.requestFocus() else if(text.isEmpty()) OTP1.requestFocus()
                R.id.otp3->if(text.length==1) OTP4.requestFocus() else if(text.isEmpty()) OTP2.requestFocus()
                R.id.otp4->if(text.length==1) OTP5.requestFocus() else if(text.isEmpty()) OTP3.requestFocus()
                R.id.otp5->if(text.length==1) OTP6.requestFocus() else if(text.isEmpty()) OTP4.requestFocus()
                R.id.otp6->if(text.isEmpty()) OTP6.requestFocus()

            }
        }

    }
}


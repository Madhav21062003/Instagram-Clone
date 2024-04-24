package com.app.instagram.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.app.instagram.HomeActivity
import com.app.instagram.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()


    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // This is used to hide the status bar and make
            // the splash screen as a full screen activity.
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            // we used the postDelayed(Runnable, time) method
            // to send a message with a delayed time.
            //Normal Handler is deprecated , so we have to change the code little bit

            // Handler().postDelayed({
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.

        } else {
            // This is used to hide the status bar and make
            // the splash screen as a full screen activity.
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            // we used the postDelayed(Runnable, time) method
            // to send a message with a delayed time.
            //Normal Handler is deprecated , so we have to change the code little bit

            // Handler().postDelayed({
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, AuthOptionsActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.

        }
    }


    override fun onRestart() {
        super.onRestart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // This is used to hide the status bar and make
            // the splash screen as a full screen activity.
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            // we used the postDelayed(Runnable, time) method
            // to send a message with a delayed time.
            //Normal Handler is deprecated , so we have to change the code little bit

            // Handler().postDelayed({
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.

        } else {
            // This is used to hide the status bar and make
            // the splash screen as a full screen activity.
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            // we used the postDelayed(Runnable, time) method
            // to send a message with a delayed time.
            //Normal Handler is deprecated , so we have to change the code little bit

            // Handler().postDelayed({
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, AuthOptionsActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.
        }
    }
}
package com.app.instagram

import android.app.ProgressDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.instagram.databinding.ActivityHomeBinding
import com.app.instagram.fragments.HomeFragment
import com.app.instagram.fragments.ProfileFragment
import com.app.instagram.fragments.ReelsFragment
import com.app.instagram.fragments.SearchFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding

    private companion object{
        private const val TAG = "LOGIN_TAG"
    }

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // get instance of firebase auth for Auth related task
        firebaseAuth = FirebaseAuth.getInstance()

        //By Default at the opening of application show Home Fragment first
        showHomeFragment()

        // handle BottomNav item click to navigate Fragments
        binding.bottomNV.setOnItemSelectedListener {item->

            when(item.itemId){

                R.id.menu_home ->{
                    // Home item click, show HomeFragment
                    showHomeFragment()
                    true
                }

                R.id.menu_search ->{
                    // Show Search fragment
                    showSearchFragment()
                    true
                }

                R.id.menu_reels ->{
                    showReelsFragment()
                    true
                }

                R.id.menu_profile ->{
                    showProfileFragment()
                    true
                }
                else -> {
                    false
                }
            }
        }


    }

    private fun showHomeFragment() {
        // show Home Fragment
        val fragment = HomeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id, fragment, "HomeFragment")
        fragmentTransaction.commit()
    }

    private fun showSearchFragment() {
        val fragment = SearchFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id, fragment,"Search Fragment")
        fragmentTransaction.commit()
    }

    private fun showReelsFragment() {
        val fragment = ReelsFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,fragment,"Reels Fragment")
        fragmentTransaction.commit()
    }
    private fun showProfileFragment() {

        val fragment = ProfileFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentsFl.id,fragment,"Profile Fragment")
        fragmentTransaction.commit()
    }




}
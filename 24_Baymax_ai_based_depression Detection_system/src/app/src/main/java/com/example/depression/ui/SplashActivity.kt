package com.example.depression.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.depression.MainActivity
import com.example.depression.R
import com.example.depression.repository.FirebaseViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private val TAG = "SplashScreen"

class SplashActivity : AppCompatActivity() {
    private val firebaseViewModel: FirebaseViewModel by inject()
    private var currentFirebaseUser: FirebaseUser? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        coroutineScope.launch {
            delay(3_000)
            currentFirebaseUser = firebaseViewModel.checkUserLoggedIn()

            if (currentFirebaseUser == null) {
                startActivity(RegisterActivity())
            } else {
                currentFirebaseUser?.let { firebaseUser ->
                    Log.i(TAG, firebaseUser.uid)
                    firebaseViewModel.getUserFromFirestore(
                        firebaseUser.uid,
                        this@SplashActivity
                    )
                    startActivity(MainActivity())
                }
            }
        }
    }

    private fun startActivity(activity: Activity) {
        val intent = Intent(this@SplashActivity, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

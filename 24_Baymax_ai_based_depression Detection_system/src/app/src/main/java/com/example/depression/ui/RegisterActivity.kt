package com.example.depression.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.depression.R
import com.example.depression.repository.FirebaseViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.inject

private val TAG = "RegisterActivity"

class RegisterActivity : AppCompatActivity() {
    private val firebaseViewModel: FirebaseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register_login.setOnClickListener {
            if (validateName() && validateEmail() && validateNNumber() && validatePassword()) {
                firebaseViewModel.registerUserFromAuthWithEmailAndPassword(
                    tiet_register_name.text.toString(),
                    tiet_register_email.text.toString(),
                    tiet_register_password.text.toString(),
                    tiet_register_number.text.toString(),
                    this
                )
            }
        }

        tv_register_loginnow.setOnClickListener {
            startLoginActivity()
        }

        firebaseViewModel.toast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                firebaseViewModel.onToastShown()
            }
        })

        firebaseViewModel.spinner.observe(this, Observer { value ->
            value.let { show ->
                spinner_register.visibility = if (show) View.VISIBLE else View.GONE
                Log.i(TAG, "$show")
            }
        })
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun validateName(): Boolean {
        val name = tiet_register_name.text.toString().trim()

        return if (name.length < 6) {
            tiet_register_name.error = "Use at least 5 characters"
            false
        } else {
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = tiet_register_email.text.toString().trim()

        return if (!email.contains("@") && !email.contains(".")) {
            tiet_register_email.error = "Enter a valid email"
            false
        } else if (email.length < 6) {
            tiet_register_email.error = "Use at least 5 characters"
            false
        } else {
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = tiet_register_password.text.toString().trim()

        return if (password.length < 6) {
            tiet_register_password.error = "Use at least 5 characters"
            false
        } else {
            true
        }
    }

    private fun validateNNumber(): Boolean {
        val password = tiet_register_number.text.toString().trim()

        return if (password.length < 10) {
            tiet_register_number.error = "Invalid Mobile Number"
            false
        } else {
            true
        }
    }
}

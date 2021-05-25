package com.example.depression.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.depression.R
import com.example.depression.repository.FirebaseViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    val firebaseViewModel: FirebaseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login_login.setOnClickListener {
           if (validateEmail(tiet_login_email) && validatePassword()) {
                firebaseViewModel.logInUserFromAuthWithEmailAndPassword(
                    tiet_login_email.text.toString(),
                    tiet_login_password.text.toString(),
                    this
                )
            }


        }

        tv_login_regsiternow.setOnClickListener {
            startRegisterActivity()
        }

        firebaseViewModel.toast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                firebaseViewModel.onToastShown()
            }
        })

        firebaseViewModel.spinner.observe(this, Observer { value ->
            value.let { show ->
                spinner_login.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        btn_mobile_login.setOnClickListener {
            if(validateNNumber())
            {
                firebaseViewModel.loginUserviaMobile( "+91"+tiet_login_phone.text.toString(),this);
            }
        }
    }

    fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun validateEmail(view: View): Boolean {
        val email = (view as TextInputEditText).text.toString().trim()

        return if (!email.contains("@") && !email.contains(".")) {
            view.error = "Enter a valid email"
            false
        } else if (email.length < 6) {
            view.error = "Use at least 5 characters"
            false
        } else {
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = tiet_login_password.text.toString().trim()

        return if (password.length < 6) {
            tiet_login_password.error = "Use at least 5 characters"
            false
        } else {
            true
        }


    }





    private fun validateNNumber(): Boolean {
        val password = tiet_login_phone.text.toString().trim()

        return if (password.length < 10) {
            tiet_register_number.error = "Invalid Mobile Number"
            false
        } else {
            true
        }
    }
}

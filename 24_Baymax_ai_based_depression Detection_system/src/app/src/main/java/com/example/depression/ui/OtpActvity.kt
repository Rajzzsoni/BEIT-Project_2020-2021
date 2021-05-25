package com.example.depression.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.depression.MainActivity
import com.example.depression.R
import com.example.depression.repository.FirebaseViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.spinner_login
import kotlinx.android.synthetic.main.activity_otp.*
import org.koin.android.ext.android.inject

class OtpActvity : AppCompatActivity()
{
    val firebaseViewModel: FirebaseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)


        //verify
        btn_verify_otp.setOnClickListener {
                   if (validateOTP(tiet_otp)) {
                       val credential = PhoneAuthProvider.getCredential(firebaseViewModel.vId, tiet_otp.text.toString())
                       firebaseViewModel.signInWithPhoneAuthCredential(credential,this);
                   }

        }

        //resend otp
        tv_resend.setOnClickListener {
            firebaseViewModel.resendOtp(firebaseViewModel.mobileNumber,this)
        }

        firebaseViewModel.toast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                firebaseViewModel.onToastShown()
            }
        })

        firebaseViewModel.spinner.observe(this, Observer { value ->
            value.let { show ->
                spinner_otp.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    private fun validateOTP(view: View): Boolean {
        val otp = (view as TextInputEditText).text.toString().trim()

        return if (otp.length != 6) {
            view.error = "Enter valid 6 digit OTP number"
            false
        } else {
            true
        }
    }
}
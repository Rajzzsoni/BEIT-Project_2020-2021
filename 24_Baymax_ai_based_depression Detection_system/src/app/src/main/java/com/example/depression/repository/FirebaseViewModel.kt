package com.example.depression.repository

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.example.depression.MainActivity
import com.example.depression.R
import com.example.depression.model.User
import com.example.depression.ui.LoginActivity
import com.example.depression.ui.OtpActvity
import com.example.depression.ui.RegisterActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


private val TAG = "FirebaseViewModel"

class FirebaseViewModel(var userRepository: UserRepository) : ViewModel() {
    private val _toast = MutableLiveData<String?>()
    lateinit var vId: String;
     lateinit var reToken: PhoneAuthProvider.ForceResendingToken;
    lateinit var credential: PhoneAuthCredential;

    lateinit var mobileNumber: String;

    val toast: LiveData<String?>
        get() = _toast

    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _currentUserMLD = MutableLiveData<User>(User())
    val currentUserLD: LiveData<User>
        get() = _currentUserMLD

    //Email
    fun registerUserFromAuthWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        activity: Activity
    ) {
        launchDataLoad {
            viewModelScope.launch {
                when (val result = userRepository.registerUserFromAuthWithEmailAndPassword(
                    email,
                    password,
                    activity.applicationContext
                )) {
                    is com.example.depression.utils.Result.Success -> {
                        Log.e(TAG, "Result.Success")
                        result.data?.let { firebaseUser ->
                            createUserInFirestore(createUserObject(firebaseUser, name, phoneNumber), activity)
                        }
                    }
                    is com.example.depression.utils.Result.Error -> {
                        Log.e(TAG, "${result.exception.message}")
                        _toast.value = result.exception.message
                    }
                    is com.example.depression.utils.Result.Canceled -> {
                        Log.e(TAG, "${result.exception!!.message}")
                        _toast.value = activity.getString(R.string.request_canceled)
                    }
                }
            }
        }
    }

    private suspend fun createUserInFirestore(user: User, activity: Activity) {
        Log.d(TAG, "Result - ${user.name}")
        when (val result = userRepository.createUserInFirestore(user)) {
            is com.example.depression.utils.Result.Success -> {
                Log.d(TAG, activity::class.java.simpleName)
                when (activity) {
                    is RegisterActivity -> {
                        _toast.value = activity.getString(R.string.registration_successful)
                    }
                    is LoginActivity -> {
                        Log.d(TAG, "Result - ${user.name}")
                        _toast.value = activity.getString(R.string.login_successful)
                    }
                }
                Log.d(TAG, "Result.Error - ${user.name}")
                _currentUserMLD.value = user
                startMainActivitiy(activity)
            }
            is com.example.depression.utils.Result.Error -> {
                _toast.value = result.exception.message
            }
            is com.example.depression.utils.Result.Canceled -> {
                _toast.value = activity.getString(R.string.request_canceled)
            }
        }
    }

    fun logInUserFromAuthWithEmailAndPassword(email: String, password: String, activity: Activity) {
        launchDataLoad {
            viewModelScope.launch {
                when (val result =
                    userRepository.logInUserFromAuthWithEmailAndPassword(email, password)) {
                    is com.example.depression.utils.Result.Success -> {
                        Log.i(TAG, "SignIn - Result.Success - User: ${result.data}")
                        result.data?.let { firebaseUser ->
                            _toast.value = activity.getString(R.string.login_successful)
                            getUserFromFirestore(firebaseUser.uid, activity)
                        }
                    }
                    is com.example.depression.utils.Result.Error -> {
                        _toast.value = result.exception.message
                    }
                    is com.example.depression.utils.Result.Canceled -> {
                        _toast.value = activity.getString(R.string.request_canceled)
                    }
                }
            }
        }
    }

    suspend fun getUserFromFirestore(userId: String, activity: Activity) {
        when (val result = userRepository.getUserFromFirestore(userId)) {
            is com.example.depression.utils.Result.Success -> {
                val _user = result.data
                Log.d(TAG, "${result.data}")
                _currentUserMLD.value = _user
                startMainActivitiy(activity = activity)
            }
            is com.example.depression.utils.Result.Error -> {
                _toast.value = result.exception.message
            }
            is com.example.depression.utils.Result.Canceled -> {
                _toast.value = activity.getString(R.string.request_canceled)
            }
        }
    }

    fun checkUserLoggedIn(): FirebaseUser? {
        var _firebaseUser: FirebaseUser? = null
        viewModelScope.launch {
            _firebaseUser = userRepository.checkUserLoggedIn()
        }
        return _firebaseUser
    }

    fun createUserObject(
        firebaseUser: FirebaseUser,
        name: String,
        phoneNumber: String = ""
    ): User {
        val currentUser = User(
            id = firebaseUser.uid,
            name = name,
            phoneNumber = phoneNumber
        )

        return currentUser
    }

    fun onToastShown() {
        _toast.value = null
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: Throwable) {
                _toast.value = error.message
                _spinner.value = false
            }
        }
    }

    private fun startMainActivitiy(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }




    //::::::::::::::: Phone verification

    fun getCallBack(activity: Activity): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
         var  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credentials: PhoneAuthCredential) {

            Log.e(TAG, "onVerificationCompleted:$credential")
            _toast.value = "Login Successfully";
            credential = credentials;
            _spinner.value = false
            signInWithPhoneAuthCredential(credential,activity);

        }

        override fun onVerificationFailed(e: FirebaseException) {

            _spinner.value = false
            Log.w(TAG, "onVerificationFailed", e)
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                _toast.value = e.toString();
            } else if (e is FirebaseTooManyRequestsException) {
                _toast.value = e.toString();
            }


        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.e(TAG, "onCodeSent:$verificationId $token")
            //val credential = PhoneAuthProvider.getCredential(verificationId!!, "123456")
            _spinner.value = false
            vId = verificationId;
            reToken = token;
            _toast.value = "The SMS verification code has been sent to the provided phone number"
            startOtpActivity(activity);
        }
    }

        return callbacks;
    }

    fun loginUserviaMobile(phoneNumber:String,activity: Activity)
    {
        _spinner.value = true
        mobileNumber = phoneNumber;
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(getCallBack(activity))
            // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOtp(phoneNumber:String,activity: Activity)
    {
        _spinner.value = true
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(getCallBack(activity))
            .setForceResendingToken(reToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    //for sign In
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,activity: Activity)
    {
        _spinner.value = true
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->

                _spinner.value = false
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    startMainActivitiy(activity);

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        _toast.value = "The verification code entered was invalid"
                    }
                    // Update UI
                }
            }
    }

    private fun startOtpActivity(activity: Activity) {
        val intent = Intent(activity, OtpActvity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}
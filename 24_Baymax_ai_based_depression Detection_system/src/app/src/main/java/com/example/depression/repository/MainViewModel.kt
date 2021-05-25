package com.example.depression.repository

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.depression.R
import com.example.depression.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class MainViewModel(var userRepository: UserRepository): ViewModel() {

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _currentUserMLD = MutableLiveData<User>(User())
    val currentUserLD: LiveData<User>
        get() = _currentUserMLD

    fun getUserFromFirestore(activity: Activity) {
        viewModelScope.launch {
           val _firebaseUser = userRepository.checkUserLoggedIn()
            _firebaseUser.let {
                if (it != null) {
                    when (val result = userRepository.getUserFromFirestore(it.uid)) {
                        is com.example.depression.utils.Result.Success -> {
                            val _user = result.data
                            _currentUserMLD.value = _user
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
    }


    private val _index = MutableLiveData<Int>()
    fun currentSelection(index:Int)
    {
        _index.value = index;
    }

    val moveTo: MutableLiveData<Int>
        get() = _index;
}
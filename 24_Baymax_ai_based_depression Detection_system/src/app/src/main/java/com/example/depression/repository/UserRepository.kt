package com.example.depression.repository

import android.content.Context
import com.example.depression.model.User
import com.google.firebase.auth.FirebaseUser


interface UserRepository {
    suspend fun logInUserFromAuthWithEmailAndPassword(
        email: String,
        password: String
    ): com.example.depression.utils.Result<FirebaseUser?>

    suspend fun getUserFromFirestore(userId: String): com.example.depression.utils.Result<User>?

    suspend fun registerUserFromAuthWithEmailAndPassword(
        email: String,
        password: String,
        context: Context
    ): com.example.depression.utils.Result<FirebaseUser?>

    suspend fun createUserInFirestore(user: User): com.example.depression.utils.Result<Void?>

    suspend fun checkUserLoggedIn(): FirebaseUser?
}
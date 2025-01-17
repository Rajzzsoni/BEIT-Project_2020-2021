package com.example.depression.repository.implementation

import android.content.Context
import android.util.Log
import com.example.depression.extension.await
import com.example.depression.model.User
import com.example.depression.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.example.depression.utils.Result


private val TAG = "UserRepositoryImpl"

class UserRepositoryImpl : UserRepository {
    //CONST
    private val USER_COLLECTION_NAME = "users"

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val userCollection = firestoreInstance.collection(USER_COLLECTION_NAME)
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun logInUserFromAuthWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser?> {
        try {
            return when (val resultDocumentSnapshot = firebaseAuth.signInWithEmailAndPassword(email, password).await()) {
                is Result.Success -> {
                    Log.i(TAG, "Result.Success")
                    val firebaseUser = resultDocumentSnapshot.data.user
                    Result.Success(firebaseUser)
                }
                is Result.Error -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Error(resultDocumentSnapshot.exception)
                }
                is Result.Canceled -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Canceled(resultDocumentSnapshot.exception)
                }
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }

    override suspend fun registerUserFromAuthWithEmailAndPassword(
        email: String,
        password: String,
        context: Context
    ): Result<FirebaseUser?> {
        try {
            return when (val resultDocumentSnapshot = firebaseAuth.createUserWithEmailAndPassword(email, password).await()) {
                is Result.Success -> {
                    Log.i(TAG, "Result.Success")
                    val firebaseUser = resultDocumentSnapshot.data.user
                    Result.Success(firebaseUser)
                }
                is Result.Error -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Error(resultDocumentSnapshot.exception)
                }
                is Result.Canceled -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Canceled(resultDocumentSnapshot.exception)
                }
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }

    override suspend fun checkUserLoggedIn(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun createUserInFirestore(user: User): Result<Void?> {
        return try {
            userCollection.document(user.id).set(user).await()
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    override suspend fun getUserFromFirestore(userId: String): Result<User>? {
        try {
            return when (val resultDocumentSnapshot =
                userCollection.document(userId).get().await()) {
                is Result.Success -> {
                    val user = resultDocumentSnapshot.data.toObject(User::class.java)!!
                    Result.Success(user)
                }
                is Result.Error -> Result.Error(resultDocumentSnapshot.exception)
                is Result.Canceled -> Result.Canceled(
                    resultDocumentSnapshot.exception
                )
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }
}
package com.ewidencjaczasupracy

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

object DatabaseController {
    private var auth: FirebaseAuth = Firebase.auth
    fun sendVerificationEmail(user: FirebaseUser, callback: (Task<Void>) -> Unit)
    {
        user.sendEmailVerification().addOnCompleteListener { task ->
            callback(task)
        }
    }
    fun registerUser(email: String, password: String, name: String, surname: String, callback: (Task<Void>?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                updateUsername(name, surname){ usernameTask ->
                    callback(usernameTask)
                }
            }
    }
    fun loginUser(email: String, password: String, callback: (Task<AuthResult>) -> Unit)
    {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                callback(task)
            }
    }
    fun getCurrentUser(): FirebaseUser?
    {
        return auth.currentUser
    }
    fun signOut()
    {
        auth.signOut()
    }

    private fun updateUsername(name: String, surname: String, callback: (Task<Void>?) -> Unit)
    {
        val user = getCurrentUser()
        if(user == null)
        {
            callback(null)
        }

        val profileUpdates = userProfileChangeRequest {
            displayName = "$name $surname"
        }
        user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                callback(task)
        }
    }
    fun <T> GetErrorMessage(context : Context, task : Task<T>?) : String
    {
        if(task == null)
            return context.getString(R.string.error_unknown)

        if(task.isSuccessful)
        {
            return context.getString(R.string.monit_correct)
        }
        else
        {
            val exception = task.exception
            if(exception is FirebaseAuthWeakPasswordException)
                return context.getString(R.string.error_weak_password)
            if(exception is FirebaseAuthInvalidUserException || exception is FirebaseAuthInvalidCredentialsException)
                return context.getString(R.string.error_user_not_found)
            if(exception is FirebaseNetworkException)
                return context.getString(R.string.error_no_connection)
        }
        return context.getString(R.string.error_unknown)
    }
}
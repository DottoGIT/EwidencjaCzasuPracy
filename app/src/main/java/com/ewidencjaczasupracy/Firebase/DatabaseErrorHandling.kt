package com.ewidencjaczasupracy.Firebase

import android.content.Context
import com.ewidencjaczasupracy.R
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

object DatabaseErrorHandling {
    fun <T> getErrorMessage(context : Context, task : Task<T>?) : String
    {
        if(task == null)
            return context.getString(R.string.error_unknown)

        return if(task.isSuccessful) {
            context.getString(R.string.monit_correct)
        } else {
            val exception = task.exception
            when (exception) {
                is FirebaseAuthWeakPasswordException -> context.getString(R.string.error_weak_password)
                is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.error_user_not_found)
                is FirebaseNetworkException -> context.getString(R.string.error_no_connection)
                is FirebaseAuthUserCollisionException -> context.getString(R.string.error_user_collision) // Handle user already registered
                else -> context.getString(R.string.error_unknown)
            }
        }
    }
}
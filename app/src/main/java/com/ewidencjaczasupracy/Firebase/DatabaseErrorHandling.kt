package com.ewidencjaczasupracy.Firebase

import android.content.Context
import com.ewidencjaczasupracy.R
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

object DatabaseErrorHandling {
    fun <T> getErrorMessage(context : Context, task : Task<T>?) : String
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
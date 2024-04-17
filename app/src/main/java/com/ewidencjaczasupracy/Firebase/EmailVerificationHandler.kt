package com.ewidencjaczasupracy.Firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

object EmailVerificationHandler {
    fun sendVerificationEmail(user: FirebaseUser, callback: (Task<Void>) -> Unit)
    {
        user.sendEmailVerification().addOnCompleteListener { task ->
            callback(task)
        }
    }
}
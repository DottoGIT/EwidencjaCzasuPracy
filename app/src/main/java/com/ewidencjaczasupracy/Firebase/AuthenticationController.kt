package com.ewidencjaczasupracy.Firebase

import android.content.Context
import android.content.Intent
import android.util.Log
import com.ewidencjaczasupracy.Activities.EmailVerificationActivity
import com.ewidencjaczasupracy.Activities.LoginActivity
import com.ewidencjaczasupracy.Constants
import com.ewidencjaczasupracy.R
import com.ewidencjaczasupracy.interfaces.ILoginObserver
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

object AuthenticationController {
    private val observers = mutableListOf<ILoginObserver>()

    fun loginUser(email: String, password: String, callback: (Task<AuthResult>) -> Unit)
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful)
                {
                    notifyObserversLogIn()
                }
                callback(task)
            }
    }
    fun registerUser(email: String, password: String, callback: (Task<AuthResult>) -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful)
            {
                notifyObserversLogIn()
            }
            callback(task)
        }
    }

    fun registerLoginObserver(observer: ILoginObserver)
    {
        observers.add(observer)
    }

    private fun notifyObserversLogIn() {
        for (observer in observers) {
            observer.notifyLogIn()
        }
    }
}
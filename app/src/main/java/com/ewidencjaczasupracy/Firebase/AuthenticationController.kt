package com.ewidencjaczasupracy.Firebase

import android.content.Context
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
    private val loginObservers : MutableList<ILoginObserver> = ArrayList()
    private var lastLogged : FirebaseUser? = null
    fun loginUser(email: String, password: String, callback: (Task<AuthResult>) -> Unit)
    {
        DatabaseController.firebase.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful)
                {
                    lastLogged = DatabaseController.firebase.currentUser
                    notifyLoginObservers()
                }
                callback(task)
            }
    }
    fun registerUser(email: String, password: String, name: String, surname: String, callback: (Task<AuthResult>) -> Unit) {
        DatabaseController.firebase.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                callback(task)
        }
    }

    fun signOut()
    {
        notifyLogoutObservers()
        DatabaseController.firebase.signOut()
    }

    fun registerLoginObserver(observer: ILoginObserver)
    {
        loginObservers.add(observer)
    }
    private fun notifyLoginObservers()
    {
        for(observer in loginObservers)
        {
            observer.loggedIn(lastLogged!!)
        }
    }
    private fun notifyLogoutObservers()
    {
        for(observer in loginObservers)
        {
            observer.loggedOut(lastLogged)
        }
    }
}
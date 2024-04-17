package com.ewidencjaczasupracy.interfaces

import com.google.firebase.auth.FirebaseUser

interface ILoginObserver {
    fun loggedIn(user : FirebaseUser)
    fun loggedOut(user : FirebaseUser?)
}
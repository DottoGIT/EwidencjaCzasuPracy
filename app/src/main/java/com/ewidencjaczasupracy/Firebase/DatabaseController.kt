package com.ewidencjaczasupracy.Firebase

import android.util.Log
import com.ewidencjaczasupracy.Firebase.DAOs.UserAccountType
import com.ewidencjaczasupracy.Firebase.DAOs.UserDAO
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

object DatabaseController{
    val initalize: Unit = Unit
    private var userDAO = UserDAO()

    fun getCurrentUser() : FirebaseUser?
    {
        return FirebaseAuth.getInstance().currentUser
    }

    fun getCurrentUserAccountType() : UserAccountType
    {
        return userDAO.getCurrentUserAccountType()
    }

    fun getCurrentUserName() : String
    {
        return userDAO.getCurrentUserName()
    }

    fun getCurrentUserEmail() : String
    {
        return userDAO.getCurrentUserEmail()
    }
}
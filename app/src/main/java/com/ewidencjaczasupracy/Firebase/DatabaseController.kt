package com.ewidencjaczasupracy.Firebase

import com.ewidencjaczasupracy.Constants
import com.ewidencjaczasupracy.Firebase.DAOs.UserAccountType
import com.ewidencjaczasupracy.Firebase.DAOs.UserDAO
import com.ewidencjaczasupracy.interfaces.ILoginObserver
import com.google.android.gms.common.internal.AccountType
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object DatabaseController : ILoginObserver {
    public val firebase: FirebaseAuth = Firebase.auth
    private var currentUser : FirebaseUser? = null
    val userDAO = UserDAO()

    init {
        AuthenticationController.registerLoginObserver(this)
    }

    ///                  USER FUNCTIONS              ///

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

    fun getCurrentUser() : FirebaseUser
    {
        return currentUser!!
    }

    fun isAllSetUp() : Boolean
    {
        return userDAO.isDAOReady()
    }


    ///                     PRIVATE                     ///

    override fun loggedIn(user: FirebaseUser) {
        currentUser = user
        userDAO.setCurrentUserDocument(user)
    }

    override fun loggedOut(user: FirebaseUser?) {
        currentUser = null
        userDAO.deleteCurrentUserDocument()
    }
}
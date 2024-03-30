package com.ewidencjaczasupracy

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

enum class QueryResult
{
    Success,
    Failure
}
object DatabaseController {
    private var auth: FirebaseAuth = Firebase.auth
    fun sendVerificationEmail(user: FirebaseUser, callback: (QueryResult) -> Unit)
    {
        lateinit var result: QueryResult
        user.sendEmailVerification().addOnCompleteListener { task ->
            result = if( task.isSuccessful ) QueryResult.Success else QueryResult.Failure
            callback(result)
        }
    }
    fun registerUser(email: String, password: String, name: String, surname: String, callback: (QueryResult) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful)
            {
                updateUsername(name, surname){result ->
                    callback(result)
                }
            }
            else
            {
                callback(QueryResult.Failure)
            }
        }
    }
    fun loginUser(email: String, password: String, callback: (QueryResult) -> Unit)
    {
        lateinit var result: QueryResult
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                result = if (task.isSuccessful) QueryResult.Success else QueryResult.Failure
                callback(result)
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

    private fun updateUsername(name: String, surname: String, callback: (QueryResult) -> Unit)
    {
        val user = getCurrentUser()
        var result : QueryResult;
        val profileUpdates = userProfileChangeRequest {
            displayName = "$name $surname"
        }
        user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                result = if(task.isSuccessful) QueryResult.Success else QueryResult.Failure
                callback(result)
        }
    }
}
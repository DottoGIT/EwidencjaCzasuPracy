package com.ewidencjaczasupracy.Firebase.DAOs

import android.util.Log
import com.ewidencjaczasupracy.Constants
import com.ewidencjaczasupracy.Firebase.AuthenticationController
import com.ewidencjaczasupracy.Firebase.DatabaseController
import com.ewidencjaczasupracy.interfaces.ILoginObserver
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.CountDownLatch

enum class UserAccountType
{
    Boss,
    Worker,
    Undefined
}

class UserDAO : ILoginObserver {
    private var currentUserDocument : DocumentSnapshot? = null
    init {
        AuthenticationController.registerLoginObserver(this)
    }

    fun getCurrentUserAccountType(): UserAccountType
    {
        return UserAccountType.Undefined
        //return currentUserDocument!!.get("account_type") as UserAccountType
    }

    fun getCurrentUserName() : String
    {
        if(currentUserDocument == null)
        {
            return ": ("
        }
        return currentUserDocument!!.getString("name")!!
    }

    fun getCurrentUserEmail() : String
    {
        return "nieznom@wp.pl"
        //return currentUserDocument!!.getString("email")!!
    }

    private fun setCurrentUserDocument(callback: (Boolean) -> Unit)
    {
        val user = DatabaseController.getCurrentUser()
        if(user == null)
        {
            callback(false)
            return
        }
        FirebaseFirestore.getInstance().collection("users").document("siema")
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful)
                {
                    currentUserDocument = task.result
                    callback(true)
                }
                else
                {
                    createUserDocument(user){ reference ->
                        currentUserDocument = reference
                        callback(reference != null)
                }
            }
        }
    }

    private fun createUserDocument(user: FirebaseUser, callback: (DocumentSnapshot?) -> Unit)
    {

        val usersCollection = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
        val userData = hashMapOf(
            "userId" to user.uid,
            "email" to user.email,
            "name" to separateName(user),
            "surname" to separateSurname(user),
            "account_type" to UserAccountType.Undefined
        )

        usersCollection.document(user.uid).set(userData).addOnSuccessListener { _ ->
            usersCollection.document(user.uid).get().addOnSuccessListener{snapshot ->
                callback(snapshot)
            }
        }.addOnFailureListener{
            callback(null)
        }
    }

    fun deleteCurrentUserDocument()
    {
        currentUserDocument = null
    }

    private fun separateName(user: FirebaseUser): String {
        return user.displayName!!.split(" ")[0]
    }
    private fun separateSurname(user: FirebaseUser): String {
        return user.displayName!!.split(" ")[1]
    }

    override fun notifyLogIn() {
        val latch = CountDownLatch(1)
        setCurrentUserDocument{
            latch.countDown()
        }
        latch.await()
    }
}
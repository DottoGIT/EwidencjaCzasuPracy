package com.ewidencjaczasupracy.Firebase.DAOs

import android.util.Log
import com.ewidencjaczasupracy.Constants
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

enum class UserAccountType
{
    Boss,
    Worker,
    Undefined
}


class UserDAO {
    private var currentUserDocument : DocumentSnapshot? = null

    fun getCurrentUserAccountType(): UserAccountType
    {
        return currentUserDocument!!.get("account_type") as UserAccountType
    }

    fun getCurrentUserName() : String
    {
        return currentUserDocument!!.getString("name")!!
    }

    fun getCurrentUserEmail() : String
    {
        return currentUserDocument!!.getString("email")!!
    }

    fun setCurrentUserDocument(user : FirebaseUser)
    {
        Log.d("Yahoo", " Start")
        val docRef = FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION).document(user.uid)
        Log.d("Yahoo", " Got")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if(documentSnapshot.exists())
            {
                Log.d("Yahoo", "Exists")
                currentUserDocument = documentSnapshot
            }
            else
            {
                Log.d("Yahoo", "Not Exist")
                createUserDocument(user){ reference ->
                    currentUserDocument = reference
                }
            }
        }.addOnFailureListener{_ ->
            Log.d("Yahoo", "Fail")
        }
        Log.d("Yahoo", " Git")
        while(currentUserDocument == null) {}
        TODO("Function doesnt go into Listeners")
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

    fun isDAOReady() : Boolean
    {
        return currentUserDocument != null
    }


    private fun separateName(user: FirebaseUser): String {
        return user.displayName!!.split(" ")[0]
    }
    private fun separateSurname(user: FirebaseUser): String {
        return user.displayName!!.split(" ")[1]
    }

}
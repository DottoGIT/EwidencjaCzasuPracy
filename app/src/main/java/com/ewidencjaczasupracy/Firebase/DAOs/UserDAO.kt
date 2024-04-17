package com.ewidencjaczasupracy.Firebase.DAOs

import com.ewidencjaczasupracy.Constants
import com.ewidencjaczasupracy.Firebase.DatabaseController
import com.ewidencjaczasupracy.interfaces.ILoginObserver
import com.google.android.gms.common.internal.AccountType
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.IllegalStateException

enum class UserAccountType
{
    Boss,
    Worker,
    Undefined
}


class UserDAO {
    private var currentUserDocument : DocumentReference? = null

    fun getCurrentUserAccountType() : UserAccountType
    {
        return UserAccountType.Undefined
        TODO("DODAĆ ZBIERANIE TYPU KONTA UŻYTKOWNIKA")
    }

    fun getCurrentUserName() : String
    {
        return "NAME"
        TODO("DODAĆ ZBIERANIE NAZWY UŻYKOTWNIKA")
    }

    fun getCurrentUserEmail() : String
    {
        return "MAIL"
        TODO("DODAĆ ZBIERANIE MAILA UŻYKOTWNIKA")
    }

    fun setCurrentUserDocument(user : FirebaseUser)
    {
        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION).document(user.uid)
            .get().addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.exists())
                {
                    currentUserDocument = documentSnapshot.reference
                }
                else
                {
                    createUserDocument(user){ reference -> currentUserDocument = reference}
                }
            }
    }

    fun deleteCurrentUserDocument()
    {
        currentUserDocument = null
    }

    private fun createUserDocument(user: FirebaseUser, callback: (DocumentReference?) -> Unit)
    {
        callback(null)
        TODO("DODAĆ TWORZENIE DOKUMENTU UŻYKOTWNIKA")
    }
}
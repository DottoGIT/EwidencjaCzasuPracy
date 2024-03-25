package com.ewidencjaczasupracy

import android.app.Activity
import android.app.SharedElementCallback
import android.media.MediaCodec.QueueRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.util.TimerTask

enum class QueryResult
{
    Success,
    Failure
}
class DatabaseController {
    companion object{
        private var auth: FirebaseAuth = Firebase.auth
        fun registerUser(email: String, password: String, name: String, surname: String, callback: (QueryResult) -> Unit)
        {
            lateinit var result: QueryResult
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    result = if (task.isSuccessful) QueryResult.Success else QueryResult.Failure
                    callback(result)
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
    }
}
package com.ewidencjaczasupracy.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ewidencjaczasupracy.Firebase.AuthenticationController
import com.ewidencjaczasupracy.Firebase.DatabaseController
import com.ewidencjaczasupracy.Firebase.DatabaseErrorHandling
import com.ewidencjaczasupracy.Firebase.EmailVerificationHandler
import com.ewidencjaczasupracy.R
import com.google.firebase.auth.FirebaseUser

class EmailVerificationActivity : AppCompatActivity() {
    private lateinit var txtHello : TextView
    private lateinit var txtEmail : TextView
    private lateinit var btnBack : Button
    private lateinit var btnResend : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_email_verification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        initializeUI()
        assignButtons()
        setTexts()
        resendVerification()
    }
    private fun initializeUI() {
        txtHello = findViewById(R.id.textView_greeting)
        txtEmail = findViewById(R.id.textView_email)
        btnBack = findViewById(R.id.button_back)
        btnResend = findViewById(R.id.button_resend)
    }
    private fun assignButtons() {
        btnBack.setOnClickListener{backToLogin()}
        btnResend.setOnClickListener{resendVerification()}
    }
    private fun setTexts()
    {
        val greeting = getString(R.string.greeting) + " " + DatabaseController.getCurrentUserName()
        txtHello.text = greeting
        val email = DatabaseController.getCurrentUserEmail()
        txtEmail.text = email
    }
    private fun backToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun resendVerification(){
        val user = DatabaseController.getCurrentUser()!!
        EmailVerificationHandler.sendVerificationEmail(user) { result ->
            if (result.isSuccessful) {
                val text = getString(R.string.monit_email_sent)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            } else {
                val text = DatabaseErrorHandling.getErrorMessage(this, result)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
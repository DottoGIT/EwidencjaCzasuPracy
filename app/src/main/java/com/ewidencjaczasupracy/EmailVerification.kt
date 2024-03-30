package com.ewidencjaczasupracy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseUser

class EmailVerification : AppCompatActivity() {
    private lateinit var user : FirebaseUser
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
        if (DatabaseController.getCurrentUser() == null)
        {
            backToLogin()
        }
        user = DatabaseController.getCurrentUser()!!

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
        val greeting = getString(R.string.greeting) + user.displayName
        txtHello.text = greeting
        val email = user.email
        txtEmail.text = email
    }

    private fun backToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun resendVerification(){
        DatabaseController.sendVerificationEmail(user){ result ->
            when(result)
            {
                QueryResult.Success -> sendMonit("Email weryfikacyjny wysłany")
                QueryResult.Failure -> sendMonit("Nie udało się wysłać maila")
            }
        }
    }

    private fun sendMonit(text: String)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}
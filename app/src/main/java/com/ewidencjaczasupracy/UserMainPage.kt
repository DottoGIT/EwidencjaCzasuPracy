package com.ewidencjaczasupracy

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseUser

class UserMainPage : AppCompatActivity() {
    lateinit var user : FirebaseUser
    private lateinit var helloText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_main_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (DatabaseController.getCurrentUser() == null)
        {
            logout()
        }
        user = DatabaseController.getCurrentUser()!!

        initializeUI()
        UpdateUserName()
    }

    private fun UpdateUserName()
    {
        val user = DatabaseController.getCurrentUser()
        val greeting = getString(R.string.greeting) + user?.displayName
        helloText.text = greeting
    }

    private fun initializeUI()
    {
        helloText = findViewById(R.id.hello_text)
    }
    private fun logout()
    {
        val intent = Intent(this, MainActivity::class.java)
        DatabaseController.signOut()
        startActivity(intent)
    }
}

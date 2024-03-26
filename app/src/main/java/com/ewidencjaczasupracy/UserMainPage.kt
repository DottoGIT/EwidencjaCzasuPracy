package com.ewidencjaczasupracy

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserMainPage : AppCompatActivity() {
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

        initializeUI()
        UpdateUserName()
    }

    private fun UpdateUserName()
    {
        val user = DatabaseController.getCurrentUser()
        val greeting = getString(R.string.greeting) + (user?.email ?: "NULL")
        helloText.text = greeting
    }

    private fun initializeUI()
    {
        helloText = findViewById(R.id.hello_text)
    }
}

package com.ewidencjaczasupracy.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ewidencjaczasupracy.Firebase.AuthenticationController
import com.ewidencjaczasupracy.Firebase.DatabaseController
import com.ewidencjaczasupracy.R

class UserConfigureInitActivity : AppCompatActivity() {

    private lateinit var helloText: TextView
    private lateinit var createCompanyBtn : Button
    private lateinit var joinCompanyBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_configure_init)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeUI()
        assignButtons()
        updateUserName()
    }
    private fun updateUserName()
    {
        val greeting = getString(R.string.greeting) + " " + DatabaseController.getCurrentUserName()
        helloText.text = greeting
    }
    private fun initializeUI()
    {
        helloText = findViewById(R.id.hello_text)
        createCompanyBtn = findViewById(R.id.button_create_company)
        joinCompanyBtn = findViewById(R.id.button_join_company)
    }
    private fun createButtonClick()
    {
        val intent = Intent(this, UserConfigureGetCompanyCodeActivity::class.java)
        startActivity(intent)
    }
    private fun joinButtonClick()
    {
        val intent = Intent(this, UserConfigureInputCompanyCodeActivity::class.java)
        startActivity(intent)
    }
    private fun assignButtons()
    {
        createCompanyBtn.setOnClickListener{createButtonClick()}
        joinCompanyBtn.setOnClickListener{joinButtonClick()}
    }
}
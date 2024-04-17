package com.ewidencjaczasupracy.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ewidencjaczasupracy.Firebase.DatabaseController
import com.ewidencjaczasupracy.R

class UserConfigureGetCompanyCodeActivity : AppCompatActivity() {
    private lateinit var helloText: TextView
    private lateinit var buttonProceed : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_configure_get_company_code)
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
        buttonProceed = findViewById(R.id.button_proceed)
        helloText = findViewById(R.id.hello_text)
    }
    private fun proceedButtonClick()
    {
        val intent = Intent(this, BossMainActivity::class.java)
        startActivity(intent)
    }
    private fun assignButtons()
    {
        buttonProceed.setOnClickListener{proceedButtonClick()}
    }
}
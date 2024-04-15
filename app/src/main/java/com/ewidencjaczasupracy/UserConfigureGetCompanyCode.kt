package com.ewidencjaczasupracy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserConfigureGetCompanyCode : AppCompatActivity() {
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
        UpdateUserName()
    }

    private fun initializeUI()
    {
        buttonProceed = findViewById(R.id.button_proceed)
        helloText = findViewById(R.id.hello_text)
    }

    private fun proceedButtonClick()
    {
        val intent = Intent(this, AdminMainPage::class.java)
        startActivity(intent)
    }

    private fun assignButtons()
    {
        buttonProceed.setOnClickListener{proceedButtonClick()}
    }

    private fun UpdateUserName()
    {

        val greeting = getString(R.string.greeting) + " " + DatabaseController.getCurrentUser()!!.displayName!!.split(" ")[0] + "."
        helloText.text = greeting
    }

}
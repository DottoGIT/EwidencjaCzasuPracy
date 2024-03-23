package com.ewidencjaczasupracy

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private enum class FormMode{
    Login,
    Register
}

class MainActivity : AppCompatActivity() {
    private lateinit var loginTab: LoginTab
    private lateinit var registerTab: RegisterTab
    private var currentFormMode : FormMode = FormMode.Login
    // UI Variables
    private lateinit var btnConfirm: Button
    private lateinit var btnSwitch: Button
    private lateinit var mainText: TextView
    private lateinit var inEmail: EditText
    private lateinit var inName: EditText
    private lateinit var inSurname: EditText
    private lateinit var inPassword: EditText
    private lateinit var inPasswordAgain: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Init UI
        mainText = findViewById(R.id.mainText)
        inEmail = findViewById(R.id.input_email)
        inName = findViewById(R.id.input_name)
        inSurname = findViewById(R.id.input_surname)
        inPassword = findViewById(R.id.input_password)
        inPasswordAgain = findViewById(R.id.input_password_again)
        btnSwitch = findViewById(R.id.button_switch)
        btnConfirm = findViewById(R.id.button_login)

        btnSwitch.setOnClickListener{
            switchView()
        }
        btnConfirm.setOnClickListener{
            confirmForm()
        }

        // Init variables
        loginTab = LoginTab()
        registerTab = RegisterTab()
    }

    private fun switchView()
    {
        if(currentFormMode == FormMode.Login)
        {
            switchToRegisterMode()
        }
        else
        {
            switchToLoginMode()
        }
        currentFormMode = if (currentFormMode == FormMode.Login) FormMode.Register else FormMode.Login
    }

    private fun confirmForm()
    {

    }

    private fun switchToLoginMode()
    {
        mainText.text = getString(R.string.text_login)
        btnConfirm.text = getString(R.string.button_login)
        btnSwitch.text = getString(R.string.button_switch_register)
        inEmail.visibility = View.VISIBLE
        inName.visibility = View.GONE
        inSurname.visibility = View.GONE
        inPassword.visibility = View.VISIBLE
        inPasswordAgain.visibility = View.GONE
    }

    private fun switchToRegisterMode()
    {
        mainText.text = getString(R.string.text_register)
        btnConfirm.text = getString(R.string.button_register)
        btnSwitch.text = getString(R.string.button_switch_login)
        inEmail.visibility = View.VISIBLE
        inName.visibility = View.VISIBLE
        inSurname.visibility = View.VISIBLE
        inPassword.visibility = View.VISIBLE
        inPasswordAgain.visibility = View.VISIBLE
    }
}
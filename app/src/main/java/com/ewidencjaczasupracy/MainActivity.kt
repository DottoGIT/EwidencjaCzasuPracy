package com.ewidencjaczasupracy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private enum class FormMode{
    Login,
    Register
}

class MainActivity : AppCompatActivity() {
    private var currentFormMode : FormMode = FormMode.Login
    // UI Variables
    private lateinit var mainText: TextView
    private lateinit var btnConfirm: Button
    private lateinit var btnSwitch: Button
    private lateinit var inEmail: EditText
    private lateinit var inName: EditText
    private lateinit var inSurname: EditText
    private lateinit var inPassword: EditText
    private lateinit var inPasswordAgain: EditText
    // UI Form
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var surname: String
    private lateinit var password: String
    private lateinit var passwordAgain: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeUI()
        assignButtons()
    }

    private fun confirmForm()
    {
        if(currentFormMode == FormMode.Login) {
            DatabaseController.loginUser(email, password) { result ->
                if (result.isSuccessful) {
                    confirmLoggingAttempt()
                } else {
                    sendMonit(DatabaseController.getErrorMessage(this, result))
                }
            }
        }
        else if(currentFormMode == FormMode.Register) {
            DatabaseController.registerUser(email, password, name, surname) { result ->
                if (result != null && result.isSuccessful) {
                    confirmAccountRegistration()
                } else {
                    sendMonit(DatabaseController.getErrorMessage(this, result))
                }
            }
        }
    }

    private fun confirmLoggingAttempt()
    {
        val user = DatabaseController.getCurrentUser()
        if(user == null)
        {
            sendMonit(getString(R.string.error_unknown))
            return
        }
        else if(user.isEmailVerified)
        {
            DatabaseController.GetUserAccountType { result ->
                val intent = when (result) {
                    Constants.ADMIN_TYPE -> {
                        Intent(this, AdminMainPage::class.java)
                    }
                    Constants.WORKER_TYPE -> {
                        Intent(this, WorkerMainPage::class.java)
                    }
                    else -> {
                        Intent(this, UserConfigureInit::class.java)
                    }
                }
                startActivity(intent)
            }
        }
        else
        {
            val intent = Intent(this, EmailVerification::class.java)
            startActivity(intent)
        }
    }

    private fun confirmAccountRegistration()
    {
        val intent = Intent(this, EmailVerification::class.java)
        startActivity(intent)
    }

    private fun switchView()
    {
        if(currentFormMode == FormMode.Login)
            switchToRegisterMode()
        else
            switchToLoginMode()

        currentFormMode = if (currentFormMode == FormMode.Login) FormMode.Register else FormMode.Login
    }

    private fun initializeUI()
    {
        mainText = findViewById(R.id.main_text)
        inEmail = findViewById(R.id.input_email)
        inName = findViewById(R.id.input_name)
        inSurname = findViewById(R.id.input_surname)
        inPassword = findViewById(R.id.input_password)
        inPasswordAgain = findViewById(R.id.input_password_again)
        btnSwitch = findViewById(R.id.button_switch)
        btnConfirm = findViewById(R.id.button_login)
    }

    private fun switchButtonClick()
    {
        switchView()
    }

    private fun confirmButtonClick()
    {
        if(checkFormValidity())
            confirmForm()
    }

    private fun assignButtons()
    {
        btnSwitch.setOnClickListener{switchButtonClick()}
        btnConfirm.setOnClickListener{confirmButtonClick()}
    }

    private fun gatherInput()
    {
        email = inEmail.text.toString()
        name = inName.text.toString()
        surname = inSurname.text.toString()
        password = inPassword.text.toString()
        passwordAgain = inPasswordAgain.text.toString()
    }


    private fun checkFormValidity(): Boolean
    {
        gatherInput()
        if (currentFormMode == FormMode.Login)
        {
            if(!DataVerifier.isEmailValid(email))
            {
                sendMonit(getString(R.string.error_invalid_email))
                return false
            }
            if(!DataVerifier.isPasswordValid(password))
            {
                sendMonit(getString(R.string.error_invalid_password))
                return false
            }
        }
        if(currentFormMode == FormMode.Register)
        {
            if(!DataVerifier.isEmailValid(email))
            {
                sendMonit(getString(R.string.error_invalid_email))
                return false
            }
            if(!DataVerifier.isNameValid(name))
            {
                sendMonit(getString(R.string.error_invalid_name))
                return false
            }
            if(!DataVerifier.isSurnameValid(surname))
            {
                sendMonit(getString(R.string.error_invalid_surname))
                return false
            }
            if(!DataVerifier.isPasswordValid(password))
            {
                sendMonit(getString(R.string.error_invalid_password))
                return false
            }
            if(!DataVerifier.isPasswordAgainValid(password, passwordAgain))
            {
                sendMonit(getString(R.string.error_invalid_password_again))
                return false
            }
        }
        return true
    }
    private fun sendMonit(text: String)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun switchToLoginMode()
    {
        mainText.text = getString(R.string.text_login)
        btnConfirm.text = getString(R.string.button_login)
        btnSwitch.text = getString(R.string.text_register)
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
        btnSwitch.text = getString(R.string.text_login)
        inEmail.visibility = View.VISIBLE
        inName.visibility = View.VISIBLE
        inSurname.visibility = View.VISIBLE
        inPassword.visibility = View.VISIBLE
        inPasswordAgain.visibility = View.VISIBLE
    }
}
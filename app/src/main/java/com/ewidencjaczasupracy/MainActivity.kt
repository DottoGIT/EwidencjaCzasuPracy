package com.ewidencjaczasupracy

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
    private lateinit var loginTab: LoginTab
    private lateinit var registerTab: RegisterTab
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
        // Init UI
        mainText = findViewById(R.id.mainText)
        inEmail = findViewById(R.id.input_email)
        inName = findViewById(R.id.input_name)
        inSurname = findViewById(R.id.input_surname)
        inPassword = findViewById(R.id.input_password)
        inPasswordAgain = findViewById(R.id.input_password_again)
        btnSwitch = findViewById(R.id.button_switch)
        btnConfirm = findViewById(R.id.button_login)

        btnSwitch.setOnClickListener{switchButtonClick()}
        btnConfirm.setOnClickListener{confirmButtonClick()}

        // Init variables
        loginTab = LoginTab()
        registerTab = RegisterTab()
    }

    private fun switchButtonClick()
    {
        switchView()
    }

    private fun confirmButtonClick()
    {
        if(checkFormValidity() && checkDatabaseConnection())
            confirmForm()
    }


    private fun switchView()
    {
        if(currentFormMode == FormMode.Login)
            switchToRegisterMode()
        else
            switchToLoginMode()

        currentFormMode = if (currentFormMode == FormMode.Login) FormMode.Register else FormMode.Login
    }

    private fun confirmForm()
    {
        if(currentFormMode == FormMode.Login)
        {
            val result = registerTab.registerUser(email, name, surname, password)
        }
        if(currentFormMode == FormMode.Register)
        {
            val result = loginTab.login(email, password)
        }
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
                sendMonit(getString(R.string.invalid_email))
                return false
            }
            if(!DataVerifier.isPasswordValid(password))
            {
                sendMonit(getString(R.string.invalid_password))
                return false
            }
        }
        if(currentFormMode == FormMode.Register)
        {
            if(!DataVerifier.isEmailValid(email))
            {
                sendMonit(getString(R.string.invalid_email))
                return false
            }
            if(!DataVerifier.isNameValid(name))
            {
                sendMonit(getString(R.string.invalid_name))
                return false
            }
            if(!DataVerifier.isSurnameValid(surname))
            {
                sendMonit(getString(R.string.invalid_surname))
                return false
            }
            if(!DataVerifier.isPasswordValid(password))
            {
                sendMonit(getString(R.string.invalid_password))
                return false
            }
            if(!DataVerifier.isPasswordAgainValid(password, passwordAgain))
            {
                sendMonit(getString(R.string.invalid_password_again))
                return false
            }
        }
        return true
    }

    private fun checkDatabaseConnection(): Boolean
    {
        if(!DatabaseController.isConnected())
        {
            sendMonit(getString(R.string.unable_to_connect_to_database))
            return false
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
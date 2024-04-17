package com.ewidencjaczasupracy.Activities

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
import com.ewidencjaczasupracy.Firebase.AuthenticationController
import com.ewidencjaczasupracy.Constants
import com.ewidencjaczasupracy.Firebase.DAOs.UserAccountType
import com.ewidencjaczasupracy.Firebase.DatabaseController
import com.ewidencjaczasupracy.Firebase.DatabaseErrorHandling
import com.ewidencjaczasupracy.Utils.DataVerifier
import com.ewidencjaczasupracy.R

private enum class FormMode{
    Login,
    Register
}
class LoginActivity : AppCompatActivity() {
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
            AuthenticationController.loginUser(email, password) { result ->
                if (result.isSuccessful) {
                    confirmLoggingAttempt()
                } else {
                    val message = DatabaseErrorHandling.getErrorMessage(this, result)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if(currentFormMode == FormMode.Register) {
            AuthenticationController.registerUser(email, password, name, surname) { result ->
                if (result.isSuccessful) {
                    val intent = Intent(this, EmailVerificationActivity::class.java)
                    startActivity(intent)
                } else {
                    val message = DatabaseErrorHandling.getErrorMessage(this, result)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun confirmLoggingAttempt()
    {
        val user = DatabaseController.getCurrentUser()
        if(!user.isEmailVerified)
        {
            val intent = Intent(this, EmailVerificationActivity::class.java)
            startActivity(intent)
        }

        val intent = when ( DatabaseController.getCurrentUserAccountType() ) {
            UserAccountType.Boss -> {
                Intent(this, BossMainActivity::class.java)
            }
            UserAccountType.Worker -> {
                Intent(this, WorkerMainActivity::class.java)
            }
            else -> {
                Intent(this, UserConfigureInitActivity::class.java)
            }
        }
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


    private fun checkFormValidity(): Boolean
    {
        gatherInput()
        if (currentFormMode == FormMode.Login)
        {
            if(!DataVerifier.isEmailValid(email))
            {
                val message = getString(R.string.error_invalid_email)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return false
            }
            if(!DataVerifier.isPasswordValid(password))
            {
                val message = getString(R.string.error_invalid_password)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return false
            }
        }
        else if(currentFormMode == FormMode.Register)
        {
            if(!DataVerifier.isEmailValid(email))
            {
                val message = getString(R.string.error_invalid_email)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return false
            }
            if(!DataVerifier.isNameValid(name))
            {
                val message = getString(R.string.error_invalid_name)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return false
            }
            if(!DataVerifier.isSurnameValid(surname))
            {
                val message = getString(R.string.error_invalid_surname)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return false
            }
            if(!DataVerifier.isPasswordValid(password))
            {
                val message = getString(R.string.error_invalid_password)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return false
            }
            if(!DataVerifier.isPasswordAgainValid(password, passwordAgain))
            {
                val message = getString(R.string.error_invalid_password_again)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
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
}
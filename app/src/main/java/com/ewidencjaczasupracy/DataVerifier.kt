package com.ewidencjaczasupracy

class DataVerifier {
    companion object{
        fun isNameValid(name: String): Boolean
        {
            return name.isNotEmpty()
        }

        fun isSurnameValid(surname: String): Boolean
        {
            return surname.isNotEmpty()
        }

        fun isEmailValid(email: String): Boolean
        {
            if (email.isEmpty())
                return false
            val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
            return email.matches(emailRegex)
        }

        fun isPasswordValid(password: String): Boolean
        {
            return password.isNotEmpty()
        }

        fun isPasswordAgainValid(password: String, passwordAgain: String): Boolean
        {
            if(password.isEmpty() || passwordAgain.isEmpty())
                return false
            if(password != passwordAgain)
                return false
            return true
        }
    }
}
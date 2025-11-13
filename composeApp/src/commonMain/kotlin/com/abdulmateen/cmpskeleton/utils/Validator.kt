package com.abdulmateen.cmpskeleton.utils
object Validator {
    fun validateNonEmpty(text: String): ValidationResult {
        return if (text.isNotEmpty()){
            ValidationResult(isValid = true, errorMessage = "")
        } else {
            ValidationResult(isValid = false, errorMessage = "Field cannot be empty")
        }
    }


    fun validatePassword(text: String): ValidationResult {
        return if (text.isEmpty()){
            ValidationResult(isValid = false, errorMessage = "Please Fill Field")
        }else{
            ValidationResult(isValid =  true)
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        return if (password.isEmpty()){
            ValidationResult(isValid = false, errorMessage = "Please Fill Field")
        }else if (password != confirmPassword){
            ValidationResult(isValid = false, errorMessage = "Password did not match")
        }else{
            ValidationResult(isValid = true)
        }
    }
    fun validateEmail(email: String): ValidationResult {
        return if (email.isEmpty()){
            ValidationResult(isValid = false, errorMessage = "Please Fill Field")
        }else if (!emailAddressRegex.matches(email)){
            ValidationResult(isValid = false, errorMessage = "Please Enter valid Email")
        }else{
            ValidationResult(isValid = true)
        }
    }

    private val emailAddressRegex = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
}

data class ValidationResult(
    val isValid: Boolean = false,
    val errorMessage: String = ""
)
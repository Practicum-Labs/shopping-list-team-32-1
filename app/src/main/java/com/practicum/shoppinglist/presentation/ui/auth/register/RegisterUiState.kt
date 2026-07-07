package com.practicum.shoppinglist.presentation.ui.auth.register

import com.practicum.shoppinglist.presentation.ui.auth.AuthMessage
import com.practicum.shoppinglist.presentation.ui.auth.AuthValidation
import com.practicum.shoppinglist.presentation.ui.auth.PasswordRequirement

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isRepeatedPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val message: AuthMessage? = null,
) {
    val passwordErrors: Set<PasswordRequirement> = AuthValidation.getPasswordErrors(password)
    val isEmailErrorVisible = email.isNotEmpty() && !AuthValidation.isEmailValid(email)
    val isPasswordErrorVisible = password.isNotEmpty() && passwordErrors.isNotEmpty()
    val isRepeatedPasswordErrorVisible = repeatedPassword.isNotEmpty() && repeatedPassword != password
    val canSubmit = AuthValidation.isEmailValid(email) &&
        passwordErrors.isEmpty() &&
        repeatedPassword == password &&
        repeatedPassword.isNotEmpty() &&
        !isLoading
}

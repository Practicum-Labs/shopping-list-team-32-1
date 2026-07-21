package com.practicum.shoppinglist.presentation.ui.auth.login

import com.practicum.shoppinglist.presentation.ui.auth.AuthMessage
import com.practicum.shoppinglist.presentation.ui.auth.AuthValidation

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val message: AuthMessage? = null,
) {
    val isEmailErrorVisible = email.isNotEmpty() && !AuthValidation.isEmailValid(email)
    val isPasswordErrorVisible = password.isNotEmpty() && password.length < AuthValidation.MIN_PASSWORD_LENGTH
    val canSubmit = AuthValidation.isEmailValid(email) &&
        password.length >= AuthValidation.MIN_PASSWORD_LENGTH &&
        !isLoading
}

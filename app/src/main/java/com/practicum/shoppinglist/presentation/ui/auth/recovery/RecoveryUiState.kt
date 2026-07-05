package com.practicum.shoppinglist.presentation.ui.auth.recovery

import com.practicum.shoppinglist.presentation.ui.auth.AuthMessage
import com.practicum.shoppinglist.presentation.ui.auth.AuthValidation

data class RecoveryUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val message: AuthMessage? = null,
) {
    val isEmailErrorVisible = email.isNotEmpty() && !AuthValidation.isEmailValid(email)
    val canSubmit = AuthValidation.isEmailValid(email) && !isLoading
}

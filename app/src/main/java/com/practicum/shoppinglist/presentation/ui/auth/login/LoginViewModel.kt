package com.practicum.shoppinglist.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.shoppinglist.domain.usecase.auth.LoginUseCase
import com.practicum.shoppinglist.presentation.ui.auth.toAuthMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val screenState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = screenState

    fun onEmailChange(email: String) {
        screenState.update { state -> state.copy(email = email.trim(), message = null) }
    }

    fun onPasswordChange(password: String) {
        screenState.update { state -> state.copy(password = password, message = null) }
    }

    fun onPasswordVisibilityClick() {
        screenState.update { state -> state.copy(isPasswordVisible = !state.isPasswordVisible) }
    }

    fun onLoginClick(onSuccess: () -> Unit) {
        val state = screenState.value
        if (!state.canSubmit) {
            return
        }
        screenState.update { currentState -> currentState.copy(isLoading = true, message = null) }
        viewModelScope.launch {
            runCatching {
                loginUseCase(email = state.email, password = state.password)
            }.onSuccess {
                screenState.update { currentState -> currentState.copy(isLoading = false) }
                onSuccess()
            }.onFailure { throwable ->
                screenState.update { currentState ->
                    currentState.copy(isLoading = false, message = throwable.toAuthMessage())
                }
            }
        }
    }
}

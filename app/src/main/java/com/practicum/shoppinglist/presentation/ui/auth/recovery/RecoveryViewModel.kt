package com.practicum.shoppinglist.presentation.ui.auth.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.shoppinglist.domain.usecase.auth.RecoverPasswordUseCase
import com.practicum.shoppinglist.presentation.ui.auth.AuthMessage
import com.practicum.shoppinglist.presentation.ui.auth.toAuthMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecoveryViewModel(
    private val recoverPasswordUseCase: RecoverPasswordUseCase,
) : ViewModel() {
    private val screenState = MutableStateFlow(RecoveryUiState())
    val uiState: StateFlow<RecoveryUiState> = screenState

    fun onEmailChange(email: String) {
        screenState.update { state -> state.copy(email = email.trim(), message = null) }
    }

    fun onRecoverClick() {
        val state = screenState.value
        if (!state.canSubmit) {
            return
        }
        screenState.update { currentState -> currentState.copy(isLoading = true, message = null) }
        viewModelScope.launch {
            runCatching {
                recoverPasswordUseCase(email = state.email)
            }.onSuccess {
                screenState.update { currentState ->
                    currentState.copy(isLoading = false, message = AuthMessage.RecoverySuccess)
                }
            }.onFailure { throwable ->
                screenState.update { currentState ->
                    currentState.copy(isLoading = false, message = throwable.toAuthMessage())
                }
            }
        }
    }
}

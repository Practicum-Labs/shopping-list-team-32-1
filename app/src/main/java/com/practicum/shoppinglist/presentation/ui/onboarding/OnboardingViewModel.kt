package com.practicum.shoppinglist.presentation.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.shoppinglist.domain.usecase.auth.ObserveAuthSessionUseCase
import com.practicum.shoppinglist.presentation.theme.Motion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val observeAuthSessionUseCase: ObserveAuthSessionUseCase,
) : ViewModel() {
    private val screenState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = screenState.asStateFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val hasLocalSession = observeAuthSessionUseCase().first() != null
            delay(Motion.Navigation.loadingScreenDelayMillis)
            screenState.value = OnboardingUiState(
                destination = if (hasLocalSession) {
                    OnboardingDestination.Main
                } else {
                    OnboardingDestination.Login
                },
            )
        }
    }
}

data class OnboardingUiState(
    val destination: OnboardingDestination? = null,
)

enum class OnboardingDestination {
    Main,
    Login,
}

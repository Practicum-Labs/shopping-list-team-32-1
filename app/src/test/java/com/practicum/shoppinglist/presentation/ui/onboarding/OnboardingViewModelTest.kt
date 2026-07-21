package com.practicum.shoppinglist.presentation.ui.onboarding

import com.practicum.shoppinglist.MainDispatcherRule
import com.practicum.shoppinglist.domain.model.AuthSession
import com.practicum.shoppinglist.domain.usecase.auth.FakeAuthRepository
import com.practicum.shoppinglist.domain.usecase.auth.ObserveAuthSessionUseCase
import com.practicum.shoppinglist.presentation.theme.Motion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val authRepository = FakeAuthRepository()

    @Test
    fun `сначала не выбирает экран для перехода`() = runTest(testDispatcher) {
        authRepository.emitSession(null)
        val viewModel = createViewModel()

        runCurrent()

        assertEquals(null, viewModel.uiState.value.destination)
    }

    @Test
    fun `переходит на экран авторизации если локальной сессии нет`() = runTest(testDispatcher) {
        authRepository.emitSession(null)
        val viewModel = createViewModel()

        runCurrent()
        advanceTimeBy(Motion.Navigation.loadingScreenDelayMillis)
        runCurrent()

        assertEquals(OnboardingDestination.Login, viewModel.uiState.value.destination)
    }

    @Test
    fun `переходит на главный экран если локальная сессия есть`() = runTest(testDispatcher) {
        authRepository.emitSession(AUTH_SESSION)
        val viewModel = createViewModel()

        runCurrent()
        advanceTimeBy(Motion.Navigation.loadingScreenDelayMillis)
        runCurrent()

        assertEquals(OnboardingDestination.Main, viewModel.uiState.value.destination)
    }

    private fun createViewModel(): OnboardingViewModel {
        return OnboardingViewModel(
            observeAuthSessionUseCase = ObserveAuthSessionUseCase(authRepository),
        )
    }

    private companion object {
        val AUTH_SESSION = AuthSession(
            userId = 1L,
            email = "student@example.com",
            accessToken = "access-token",
            refreshToken = "refresh-token",
        )
    }
}

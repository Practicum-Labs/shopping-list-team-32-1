package com.practicum.shoppinglist.presentation.ui.auth.recovery

import com.practicum.shoppinglist.MainDispatcherRule
import com.practicum.shoppinglist.domain.model.AuthError
import com.practicum.shoppinglist.domain.usecase.auth.FakeAuthRepository
import com.practicum.shoppinglist.domain.usecase.auth.RecoverPasswordUseCase
import com.practicum.shoppinglist.presentation.ui.auth.AuthMessage
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RecoveryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = FakeAuthRepository()
    private val viewModel = RecoveryViewModel(RecoverPasswordUseCase(authRepository))

    @Test
    fun `обрезает пробелы в email при вводе`() {
        viewModel.onEmailChange("  $EMAIL  ")

        assertEquals(EMAIL, viewModel.uiState.value.email)
    }

    @Test
    fun `не отправляет запрос если email невалидный`() = runTest {
        viewModel.onEmailChange(INVALID_EMAIL)

        viewModel.onRecoverClick()

        assertTrue(authRepository.recoveryEmails.isEmpty())
    }

    @Test
    fun `показывает сообщение об успехе после восстановления пароля`() = runTest {
        viewModel.onEmailChange(EMAIL)

        viewModel.onRecoverClick()

        assertEquals(EMAIL, authRepository.recoveryEmails.single())
        assertEquals(AuthMessage.RecoverySuccess, viewModel.uiState.value.message)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `показывает ошибку сети если восстановление не удалось`() = runTest {
        authRepository.recoverPasswordResult = Result.failure(AuthError.Network())
        viewModel.onEmailChange(EMAIL)

        viewModel.onRecoverClick()

        assertEquals(AuthMessage.Network, viewModel.uiState.value.message)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    private companion object {
        const val EMAIL = "student@example.com"
        const val INVALID_EMAIL = "student"
    }
}

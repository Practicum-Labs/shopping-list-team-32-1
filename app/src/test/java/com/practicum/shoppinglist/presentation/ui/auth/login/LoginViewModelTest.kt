package com.practicum.shoppinglist.presentation.ui.auth.login

import com.practicum.shoppinglist.MainDispatcherRule
import com.practicum.shoppinglist.domain.model.AuthError
import com.practicum.shoppinglist.domain.usecase.auth.FakeAuthRepository
import com.practicum.shoppinglist.domain.usecase.auth.LoginUseCase
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
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = FakeAuthRepository()
    private val viewModel = LoginViewModel(LoginUseCase(authRepository))

    @Test
    fun `обрезает пробелы в email при вводе`() {
        viewModel.onEmailChange("  $EMAIL  ")

        assertEquals(EMAIL, viewModel.uiState.value.email)
    }

    @Test
    fun `меняет видимость пароля`() {
        viewModel.onPasswordVisibilityClick()

        assertTrue(viewModel.uiState.value.isPasswordVisible)
    }

    @Test
    fun `не отправляет запрос если форма невалидна`() = runTest {
        var isSuccessCalled = false

        viewModel.onLoginClick { isSuccessCalled = true }

        assertFalse(isSuccessCalled)
        assertTrue(authRepository.loginRequests.isEmpty())
    }

    @Test
    fun `отправляет email и пароль при успешном входе`() = runTest {
        var isSuccessCalled = false
        viewModel.onEmailChange(EMAIL)
        viewModel.onPasswordChange(PASSWORD)

        viewModel.onLoginClick { isSuccessCalled = true }

        assertTrue(isSuccessCalled)
        assertEquals(FakeAuthRepository.AuthRequest(EMAIL, PASSWORD), authRepository.loginRequests.single())
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `показывает понятную ошибку если данные для входа неверные`() = runTest {
        authRepository.loginResult = Result.failure(AuthError.InvalidCredentials())
        viewModel.onEmailChange(EMAIL)
        viewModel.onPasswordChange(PASSWORD)

        viewModel.onLoginClick {}

        assertEquals(AuthMessage.InvalidCredentials, viewModel.uiState.value.message)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    private companion object {
        const val EMAIL = "student@example.com"
        const val PASSWORD = "Password1!"
    }
}

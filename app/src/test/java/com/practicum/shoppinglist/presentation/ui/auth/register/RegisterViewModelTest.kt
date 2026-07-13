package com.practicum.shoppinglist.presentation.ui.auth.register

import com.practicum.shoppinglist.MainDispatcherRule
import com.practicum.shoppinglist.domain.model.AuthError
import com.practicum.shoppinglist.domain.usecase.auth.FakeAuthRepository
import com.practicum.shoppinglist.domain.usecase.auth.RegisterUseCase
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
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = FakeAuthRepository()
    private val viewModel = RegisterViewModel(RegisterUseCase(authRepository))

    @Test
    fun `обрезает пробелы в email при вводе`() {
        viewModel.onEmailChange("  $EMAIL  ")

        assertEquals(EMAIL, viewModel.uiState.value.email)
    }

    @Test
    fun `меняет видимость обоих полей пароля`() {
        viewModel.onPasswordVisibilityClick()
        viewModel.onRepeatedPasswordVisibilityClick()

        assertTrue(viewModel.uiState.value.isPasswordVisible)
        assertTrue(viewModel.uiState.value.isRepeatedPasswordVisible)
    }

    @Test
    fun `не отправляет запрос если пароли не совпадают`() = runTest {
        viewModel.onEmailChange(EMAIL)
        viewModel.onPasswordChange(PASSWORD)
        viewModel.onRepeatedPasswordChange(OTHER_PASSWORD)

        viewModel.onRegisterClick {}

        assertTrue(authRepository.registerRequests.isEmpty())
    }

    @Test
    fun `отправляет email и пароль при успешной регистрации`() = runTest {
        var isSuccessCalled = false
        viewModel.onEmailChange(EMAIL)
        viewModel.onPasswordChange(PASSWORD)
        viewModel.onRepeatedPasswordChange(PASSWORD)

        viewModel.onRegisterClick { isSuccessCalled = true }

        assertTrue(isSuccessCalled)
        assertEquals(FakeAuthRepository.AuthRequest(EMAIL, PASSWORD), authRepository.registerRequests.single())
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `показывает ошибку если пользователь уже существует`() = runTest {
        authRepository.registerResult = Result.failure(AuthError.UserAlreadyExists())
        viewModel.onEmailChange(EMAIL)
        viewModel.onPasswordChange(PASSWORD)
        viewModel.onRepeatedPasswordChange(PASSWORD)

        viewModel.onRegisterClick {}

        assertEquals(AuthMessage.UserAlreadyExists, viewModel.uiState.value.message)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    private companion object {
        const val EMAIL = "student@example.com"
        const val PASSWORD = "Password1!"
        const val OTHER_PASSWORD = "Password2!"
    }
}

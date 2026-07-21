package com.practicum.shoppinglist.domain.usecase.auth

import app.cash.turbine.test
import com.practicum.shoppinglist.domain.model.AuthSession
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthUseCasesTest {

    private val authRepository = FakeAuthRepository()

    @Test
    fun `выполняет вход через репозиторий`() = runTest {
        val useCase = LoginUseCase(authRepository)

        val result = useCase(email = EMAIL, password = PASSWORD)

        assertEquals(AUTH_SESSION, result)
        assertEquals(FakeAuthRepository.AuthRequest(EMAIL, PASSWORD), authRepository.loginRequests.single())
    }

    @Test
    fun `выполняет регистрацию через репозиторий`() = runTest {
        val useCase = RegisterUseCase(authRepository)

        val result = useCase(email = EMAIL, password = PASSWORD)

        assertEquals(AUTH_SESSION, result)
        assertEquals(FakeAuthRepository.AuthRequest(EMAIL, PASSWORD), authRepository.registerRequests.single())
    }

    @Test
    fun `запускает восстановление пароля через репозиторий`() = runTest {
        val useCase = RecoverPasswordUseCase(authRepository)

        useCase(email = EMAIL)

        assertEquals(EMAIL, authRepository.recoveryEmails.single())
    }

    @Test
    fun `проверяет активность сессии через репозиторий`() = runTest {
        val useCase = CheckAuthUseCase(authRepository)
        authRepository.checkSessionResult = true

        val result = useCase()

        assertTrue(result)
    }

    @Test
    fun `возвращает false если сессия невалидна`() = runTest {
        val useCase = CheckAuthUseCase(authRepository)
        authRepository.checkSessionResult = false

        val result = useCase()

        assertFalse(result)
    }

    @Test
    fun `выполняет выход через репозиторий`() = runTest {
        val useCase = LogoutUseCase(authRepository)

        useCase()

        assertEquals(1, authRepository.logoutCallCount)
    }

    @Test
    fun `наблюдает за локальной auth сессией`() = runTest {
        val useCase = ObserveAuthSessionUseCase(authRepository)

        useCase().test {
            assertEquals(null, awaitItem())
            authRepository.emitSession(AUTH_SESSION)

            assertEquals(AUTH_SESSION, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        const val EMAIL = "student@example.com"
        const val PASSWORD = "Password1!"
        val AUTH_SESSION = AuthSession(
            userId = 1L,
            email = EMAIL,
            accessToken = "access-token",
            refreshToken = "refresh-token",
        )
    }
}

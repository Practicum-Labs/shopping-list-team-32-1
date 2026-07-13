package com.practicum.shoppinglist.domain.usecase.auth

import com.practicum.shoppinglist.domain.model.AuthSession
import com.practicum.shoppinglist.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FakeAuthRepository : AuthRepository {
    private val authSessionState = MutableStateFlow<AuthSession?>(null)
    private val currentUserIdState = MutableStateFlow<Long?>(null)

    override val authSession: Flow<AuthSession?> = authSessionState
    override val currentUserId: Flow<Long?> = currentUserIdState

    var loginResult: Result<AuthSession> = Result.success(AUTH_SESSION)
    var registerResult: Result<AuthSession> = Result.success(AUTH_SESSION)
    var recoverPasswordResult: Result<Unit> = Result.success(Unit)
    var checkSessionResult = false
    var refreshSessionResult = false
    var requiredCurrentUserId = AUTH_SESSION.userId

    val loginRequests = mutableListOf<AuthRequest>()
    val registerRequests = mutableListOf<AuthRequest>()
    val recoveryEmails = mutableListOf<String>()
    var logoutCallCount = 0

    override suspend fun login(email: String, password: String): AuthSession {
        loginRequests.add(AuthRequest(email = email, password = password))
        return loginResult.getOrThrow()
    }

    override suspend fun register(email: String, password: String): AuthSession {
        registerRequests.add(AuthRequest(email = email, password = password))
        return registerResult.getOrThrow()
    }

    override suspend fun recoverPassword(email: String) {
        recoveryEmails.add(email)
        recoverPasswordResult.getOrThrow()
    }

    override suspend fun checkSession(): Boolean = checkSessionResult

    override suspend fun refreshSession(): Boolean = refreshSessionResult

    override suspend fun logout() {
        logoutCallCount++
    }

    override suspend fun requireCurrentUserId(): Long = requiredCurrentUserId

    fun emitSession(session: AuthSession?) {
        authSessionState.value = session
        currentUserIdState.value = session?.userId
    }

    data class AuthRequest(
        val email: String,
        val password: String,
    )

    private companion object {
        val AUTH_SESSION = AuthSession(
            userId = 1L,
            email = "student@example.com",
            accessToken = "access-token",
            refreshToken = "refresh-token",
        )
    }
}

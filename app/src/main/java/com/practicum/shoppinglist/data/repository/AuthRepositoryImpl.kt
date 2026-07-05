package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.datasource.AuthTokenDataSource
import com.practicum.shoppinglist.data.mapper.accessTokenOrThrow
import com.practicum.shoppinglist.data.mapper.errorToAuthError
import com.practicum.shoppinglist.data.mapper.refreshTokenOrThrow
import com.practicum.shoppinglist.data.mapper.toAuthError
import com.practicum.shoppinglist.data.mapper.toDomain
import com.practicum.shoppinglist.data.remote.HTTP_BAD_REQUEST
import com.practicum.shoppinglist.data.remote.HTTP_UNAUTHORIZED
import com.practicum.shoppinglist.data.remote.auth.AuthApi
import com.practicum.shoppinglist.data.remote.auth.dto.LoginRequestDto
import com.practicum.shoppinglist.data.remote.auth.dto.RefreshTokenRequestDto
import com.practicum.shoppinglist.data.remote.auth.dto.RegisterRequestDto
import com.practicum.shoppinglist.domain.model.AuthError
import com.practicum.shoppinglist.domain.model.AuthSession
import com.practicum.shoppinglist.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authTokenDataSource: AuthTokenDataSource,
) : AuthRepository {
    override val authSession: Flow<AuthSession?> = authTokenDataSource.authSession
    override val currentUserId: Flow<Long?> = authSession.map { session -> session?.userId }

    override suspend fun login(email: String, password: String): AuthSession {
        return runCatching {
            val response = authApi.login(LoginRequestDto(email = email, password = password))
            if (!response.isSuccessful) {
                throw response.errorToAuthError(unauthorizedAsInvalidCredentials = true)
            }
            val session = response.body()?.toDomain(email = email) ?: throw AuthError.Unknown()
            authTokenDataSource.saveSession(session)
            session
        }.getOrElse { throwable ->
            throw throwable.toAuthError()
        }
    }

    override suspend fun register(email: String, password: String): AuthSession {
        return runCatching {
            val response = authApi.register(RegisterRequestDto(email = email, password = password))
            if (!response.isSuccessful) {
                throw response.errorToAuthError()
            }
            val session = response.body()?.toDomain(email = email) ?: throw AuthError.Unknown()
            authTokenDataSource.saveSession(session)
            session
        }.getOrElse { throwable ->
            throw throwable.toAuthError()
        }
    }

    override suspend fun recoverPassword(email: String) {
        runCatching {
            val response = authApi.recoverPassword(email = email)
            if (!response.isSuccessful) {
                throw response.errorToAuthError()
            }
        }.getOrElse { throwable ->
            throw throwable.toAuthError()
        }
    }

    override suspend fun checkSession(): Boolean {
        val session = authSession.first() ?: return false
        return runCatching {
            val response = authApi.checkToken(authorization = "Bearer ${session.accessToken}")
            if (response.isSuccessful && response.body()?.isTokenValid() == true) {
                true
            } else {
                refreshSession()
            }
        }.getOrElse {
            refreshSession()
        }
    }

    override suspend fun refreshSession(): Boolean {
        val session = authSession.first() ?: return false
        return runCatching {
            val response = authApi.refresh(
                RefreshTokenRequestDto(refreshToken = session.refreshToken),
            )
            if (response.isSuccessful) {
                val body = response.body() ?: throw AuthError.Unknown()
                authTokenDataSource.updateTokens(
                    accessToken = body.accessTokenOrThrow(),
                    refreshToken = body.refreshTokenOrThrow(),
                )
                true
            } else if (response.code().isRefreshTokenRejected()) {
                authTokenDataSource.clearSession()
                false
            } else {
                true
            }
        }.getOrElse {
            true
        }
    }

    override suspend fun logout() {
        authTokenDataSource.clearSession()
    }

    override suspend fun requireCurrentUserId(): Long {
        return authSession.first()?.userId ?: throw AuthError.Unauthorized()
    }

    private fun com.practicum.shoppinglist.data.remote.auth.dto.CheckTokenResponseDto.isTokenValid(): Boolean {
        return success == true || isValid == true
    }

    private fun Int.isRefreshTokenRejected(): Boolean {
        return this == HTTP_BAD_REQUEST || this == HTTP_UNAUTHORIZED
    }
}

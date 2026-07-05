package com.practicum.shoppinglist.data.mapper

import com.practicum.shoppinglist.data.remote.HTTP_BAD_REQUEST
import com.practicum.shoppinglist.data.remote.HTTP_CONFLICT
import com.practicum.shoppinglist.data.remote.HTTP_SERVER_ERROR_END
import com.practicum.shoppinglist.data.remote.HTTP_SERVER_ERROR_START
import com.practicum.shoppinglist.data.remote.HTTP_UNAUTHORIZED
import com.practicum.shoppinglist.data.remote.auth.dto.AuthResponseDto
import com.practicum.shoppinglist.data.remote.auth.dto.RefreshTokenResponseDto
import com.practicum.shoppinglist.domain.model.AuthError
import com.practicum.shoppinglist.domain.model.AuthSession
import retrofit2.Response
import java.io.IOException

fun AuthResponseDto.toDomain(email: String): AuthSession {
    return AuthSession(
        userId = requiredUserId(),
        email = email,
        accessToken = requiredAccessToken(),
        refreshToken = requiredRefreshToken(),
    )
}

private fun AuthResponseDto.requiredUserId(): Long {
    return userId ?: userIdSnakeCase ?: throw AuthError.Unknown()
}

private fun AuthResponseDto.requiredAccessToken(): String {
    return accessToken ?: accessTokenSnakeCase ?: throw AuthError.Unknown()
}

private fun AuthResponseDto.requiredRefreshToken(): String {
    return refreshToken ?: refreshTokenSnakeCase ?: throw AuthError.Unknown()
}

fun RefreshTokenResponseDto.accessTokenOrThrow(): String {
    return accessToken ?: accessTokenSnakeCase ?: throw AuthError.Unknown()
}

fun RefreshTokenResponseDto.refreshTokenOrThrow(): String {
    return refreshToken ?: refreshTokenSnakeCase ?: throw AuthError.Unknown()
}

fun Throwable.toAuthError(): AuthError {
    return when (this) {
        is AuthError -> this
        is IOException -> AuthError.Network()
        else -> AuthError.Unknown()
    }
}

fun <T> Response<T>.errorToAuthError(unauthorizedAsInvalidCredentials: Boolean = false): AuthError {
    return when (code()) {
        HTTP_BAD_REQUEST -> AuthError.InvalidCredentials()
        HTTP_UNAUTHORIZED -> if (unauthorizedAsInvalidCredentials) {
            AuthError.InvalidCredentials()
        } else {
            AuthError.Unauthorized()
        }
        HTTP_CONFLICT -> AuthError.UserAlreadyExists()
        in HTTP_SERVER_ERROR_START..HTTP_SERVER_ERROR_END -> AuthError.Server()
        else -> AuthError.Unknown()
    }
}

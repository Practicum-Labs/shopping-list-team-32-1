package com.practicum.shoppinglist.data.mapper

import com.practicum.shoppinglist.data.remote.auth.dto.AuthResponseDto
import com.practicum.shoppinglist.data.remote.auth.dto.RefreshTokenResponseDto
import com.practicum.shoppinglist.domain.model.AuthError
import com.practicum.shoppinglist.domain.model.AuthSession
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class AuthMapperTest {

    @Test
    fun `маппит auth response camelCase в доменную сессию`() {
        val dto = AuthResponseDto(
            userId = USER_ID,
            userIdSnakeCase = null,
            accessToken = ACCESS_TOKEN,
            accessTokenSnakeCase = null,
            refreshToken = REFRESH_TOKEN,
            refreshTokenSnakeCase = null,
        )

        val result = dto.toDomain(email = EMAIL)

        assertEquals(authSession(), result)
    }

    @Test
    fun `маппит auth response snake_case в доменную сессию`() {
        val dto = AuthResponseDto(
            userId = null,
            userIdSnakeCase = USER_ID,
            accessToken = null,
            accessTokenSnakeCase = ACCESS_TOKEN,
            refreshToken = null,
            refreshTokenSnakeCase = REFRESH_TOKEN,
        )

        val result = dto.toDomain(email = EMAIL)

        assertEquals(authSession(), result)
    }

    @Test
    fun `достает access token из refresh response`() {
        val dto = RefreshTokenResponseDto(
            accessToken = ACCESS_TOKEN,
            accessTokenSnakeCase = null,
            refreshToken = REFRESH_TOKEN,
            refreshTokenSnakeCase = null,
        )

        assertEquals(ACCESS_TOKEN, dto.accessTokenOrThrow())
    }

    @Test
    fun `достает refresh token из refresh response`() {
        val dto = RefreshTokenResponseDto(
            accessToken = ACCESS_TOKEN,
            accessTokenSnakeCase = null,
            refreshToken = REFRESH_TOKEN,
            refreshTokenSnakeCase = null,
        )

        assertEquals(REFRESH_TOKEN, dto.refreshTokenOrThrow())
    }

    @Test
    fun `маппит IOException в ошибку сети`() {
        val result = IOException().toAuthError()

        assertTrue(result is AuthError.Network)
    }

    @Test
    fun `маппит 401 в неверные данные для входа когда это login`() {
        val result = errorResponse<Any>(HTTP_UNAUTHORIZED).errorToAuthError(
            unauthorizedAsInvalidCredentials = true,
        )

        assertTrue(result is AuthError.InvalidCredentials)
    }

    @Test
    fun `маппит 401 в устаревшую сессию`() {
        val result = errorResponse<Any>(HTTP_UNAUTHORIZED).errorToAuthError()

        assertTrue(result is AuthError.Unauthorized)
    }

    @Test
    fun `маппит 409 в уже существующего пользователя`() {
        val result = errorResponse<Any>(HTTP_CONFLICT).errorToAuthError()

        assertTrue(result is AuthError.UserAlreadyExists)
    }

    @Test
    fun `маппит 500 в ошибку сервера`() {
        val result = errorResponse<Any>(HTTP_INTERNAL_SERVER_ERROR).errorToAuthError()

        assertTrue(result is AuthError.Server)
    }

    private fun authSession(): AuthSession {
        return AuthSession(
            userId = USER_ID,
            email = EMAIL,
            accessToken = ACCESS_TOKEN,
            refreshToken = REFRESH_TOKEN,
        )
    }

    private fun <T> errorResponse(code: Int): Response<T> {
        return Response.error(code, ERROR_BODY.toResponseBody())
    }

    private companion object {
        const val USER_ID = 1L
        const val EMAIL = "student@example.com"
        const val ACCESS_TOKEN = "access-token"
        const val REFRESH_TOKEN = "refresh-token"
        const val ERROR_BODY = "{}"
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_CONFLICT = 409
        const val HTTP_INTERNAL_SERVER_ERROR = 500
    }
}

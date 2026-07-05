package com.practicum.shoppinglist.data.remote.auth

import com.practicum.shoppinglist.data.remote.auth.dto.AuthResponseDto
import com.practicum.shoppinglist.data.remote.auth.dto.CheckTokenResponseDto
import com.practicum.shoppinglist.data.remote.auth.dto.LoginRequestDto
import com.practicum.shoppinglist.data.remote.auth.dto.RefreshTokenRequestDto
import com.practicum.shoppinglist.data.remote.auth.dto.RefreshTokenResponseDto
import com.practicum.shoppinglist.data.remote.auth.dto.RegisterRequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/registration")
    suspend fun register(@Body request: RegisterRequestDto): Response<AuthResponseDto>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequestDto): Response<RefreshTokenResponseDto>

    @GET("auth/check")
    suspend fun checkToken(@Header("Authorization") authorization: String): Response<CheckTokenResponseDto>

    @POST("auth/recovery")
    suspend fun recoverPassword(@Header("email") email: String): Response<ResponseBody>
}

package com.practicum.shoppinglist.data.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("userId")
    val userId: Long?,
    @SerializedName("user_id")
    val userIdSnakeCase: Long?,
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("access_token")
    val accessTokenSnakeCase: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("refresh_token")
    val refreshTokenSnakeCase: String?,
)

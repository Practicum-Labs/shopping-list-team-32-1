package com.practicum.shoppinglist.data.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponseDto(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("access_token")
    val accessTokenSnakeCase: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("refresh_token")
    val refreshTokenSnakeCase: String?,
)

package com.practicum.shoppinglist.data.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestDto(
    @SerializedName("refresh_token")
    val refreshToken: String,
)

package com.practicum.shoppinglist.data.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class CheckTokenResponseDto(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("refresh")
    val refresh: Boolean?,
    @SerializedName("is_valid")
    val isValid: Boolean?,
)

package com.practicum.shoppinglist.domain.model

data class AuthSession(
    val userId: Long,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
)

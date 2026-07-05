package com.practicum.shoppinglist.domain.repository

import com.practicum.shoppinglist.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authSession: Flow<AuthSession?>
    val currentUserId: Flow<Long?>

    suspend fun login(email: String, password: String): AuthSession

    suspend fun register(email: String, password: String): AuthSession

    suspend fun recoverPassword(email: String)

    suspend fun checkSession(): Boolean

    suspend fun refreshSession(): Boolean

    suspend fun logout()

    suspend fun requireCurrentUserId(): Long
}

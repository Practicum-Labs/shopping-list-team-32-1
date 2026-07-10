package com.practicum.shoppinglist.domain.usecase.auth

import com.practicum.shoppinglist.domain.model.AuthSession
import com.practicum.shoppinglist.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): AuthSession {
        return authRepository.register(email = email, password = password)
    }
}

package com.practicum.shoppinglist.domain.usecase.auth

import com.practicum.shoppinglist.domain.repository.AuthRepository

class RecoverPasswordUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String) {
        authRepository.recoverPassword(email = email)
    }
}

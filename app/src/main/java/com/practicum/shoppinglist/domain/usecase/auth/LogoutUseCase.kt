package com.practicum.shoppinglist.domain.usecase.auth

import com.practicum.shoppinglist.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}

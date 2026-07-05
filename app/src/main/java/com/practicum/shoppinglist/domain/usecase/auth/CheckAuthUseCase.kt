package com.practicum.shoppinglist.domain.usecase.auth

import com.practicum.shoppinglist.domain.repository.AuthRepository

class CheckAuthUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.checkSession()
    }
}

package com.practicum.shoppinglist.domain.usecase.auth

import com.practicum.shoppinglist.domain.repository.AuthRepository

class ObserveAuthSessionUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = authRepository.authSession
}

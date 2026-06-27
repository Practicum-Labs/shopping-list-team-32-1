package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ThemeRepository

class SetDarkThemeUseCase(
    private val themeRepository: ThemeRepository,
) {
    suspend operator fun invoke(isDarkTheme: Boolean) {
        themeRepository.setDarkTheme(isDarkTheme)
    }
}

package com.practicum.shoppinglist.domain.usecase

import com.practicum.shoppinglist.domain.repository.ThemeRepository

class ObserveDarkThemeUseCase(
    private val themeRepository: ThemeRepository,
) {
    operator fun invoke() = themeRepository.isDarkTheme
}

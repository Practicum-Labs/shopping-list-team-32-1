package com.practicum.shoppinglist.domain.theme

class SetDarkThemeUseCase(
    private val themeRepository: ThemeRepository,
) {
    suspend operator fun invoke(isDarkTheme: Boolean) {
        themeRepository.setDarkTheme(isDarkTheme)
    }
}

package com.practicum.shoppinglist.domain.theme

class ObserveDarkThemeUseCase(
    private val themeRepository: ThemeRepository,
) {
    operator fun invoke() = themeRepository.isDarkTheme
}

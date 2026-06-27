package com.practicum.shoppinglist.presentation.theme

import com.practicum.shoppinglist.domain.theme.ObserveDarkThemeUseCase
import com.practicum.shoppinglist.domain.theme.SetDarkThemeUseCase
import kotlinx.coroutines.flow.Flow

class ThemeController(
    observeDarkThemeUseCase: ObserveDarkThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase,
) {
    val savedDarkTheme: Flow<Boolean?> = observeDarkThemeUseCase()

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        setDarkThemeUseCase(isDarkTheme)
    }
}

package com.practicum.shoppinglist.presentation.theme

import com.practicum.shoppinglist.domain.usecase.ObserveDarkThemeUseCase
import com.practicum.shoppinglist.domain.usecase.SetDarkThemeUseCase
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

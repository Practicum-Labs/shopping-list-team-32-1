package com.practicum.shoppinglist.data.theme

import com.practicum.shoppinglist.domain.theme.ThemeRepository

class ThemeRepositoryImpl(
    private val themePreferencesDataSource: ThemePreferencesDataSource,
) : ThemeRepository {
    override val isDarkTheme = themePreferencesDataSource.isDarkTheme

    override suspend fun setDarkTheme(isDarkTheme: Boolean) {
        themePreferencesDataSource.setDarkTheme(isDarkTheme)
    }
}

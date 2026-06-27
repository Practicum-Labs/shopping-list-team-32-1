package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.datasource.ThemePreferencesDataSource
import com.practicum.shoppinglist.domain.repository.ThemeRepository

class ThemeRepositoryImpl(
    private val themePreferencesDataSource: ThemePreferencesDataSource,
) : ThemeRepository {
    override val isDarkTheme = themePreferencesDataSource.isDarkTheme

    override suspend fun setDarkTheme(isDarkTheme: Boolean) {
        themePreferencesDataSource.setDarkTheme(isDarkTheme)
    }
}

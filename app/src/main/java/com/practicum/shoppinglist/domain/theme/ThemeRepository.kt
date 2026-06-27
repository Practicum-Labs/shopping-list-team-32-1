package com.practicum.shoppinglist.domain.theme

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val isDarkTheme: Flow<Boolean?>

    suspend fun setDarkTheme(isDarkTheme: Boolean)
}

package com.practicum.shoppinglist.data.local.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "theme_preferences",
)

class ThemePreferencesDataSource(
    context: Context,
) {
    private val dataStore = context.themeDataStore

    val isDarkTheme: Flow<Boolean?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[IS_DARK_THEME_KEY]
        }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME_KEY] = isDarkTheme
        }
    }

    private companion object {
        val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")
    }
}

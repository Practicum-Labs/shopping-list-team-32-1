package com.practicum.shoppinglist.data.local.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.practicum.shoppinglist.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "auth_preferences",
)

class AuthTokenDataSource(
    context: Context,
) {
    private val dataStore = context.authDataStore

    val authSession: Flow<AuthSession?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences.toAuthSession()
        }

    suspend fun saveSession(authSession: AuthSession) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = authSession.userId
            preferences[EMAIL_KEY] = authSession.email
            preferences[ACCESS_TOKEN_KEY] = authSession.accessToken
            preferences[REFRESH_TOKEN_KEY] = authSession.refreshToken
        }
    }

    suspend fun updateTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
}

private val USER_ID_KEY = longPreferencesKey("auth_user_id")
private val EMAIL_KEY = stringPreferencesKey("auth_email")
private val ACCESS_TOKEN_KEY = stringPreferencesKey("auth_access_token")
private val REFRESH_TOKEN_KEY = stringPreferencesKey("auth_refresh_token")

private fun Preferences.toAuthSession(): AuthSession? {
    val userId = this[USER_ID_KEY] ?: return null
    val email = this[EMAIL_KEY].takeIfNotBlank()
    val accessToken = this[ACCESS_TOKEN_KEY].takeIfNotBlank()
    val refreshToken = this[REFRESH_TOKEN_KEY].takeIfNotBlank()
    val hasSessionTokens = email != null && accessToken != null && refreshToken != null

    return if (hasSessionTokens) {
        AuthSession(
            userId = userId,
            email = email,
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    } else {
        null
    }
}

private fun String?.takeIfNotBlank(): String? {
    return takeIf { value -> !value.isNullOrBlank() }
}

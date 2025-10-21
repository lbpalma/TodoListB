package com.example.todolistb.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object PrefKeys {
    val USERNAME = stringPreferencesKey("username")
    val THEME = stringPreferencesKey("theme") // "light" | "dark"
}

class UserPreferences(private val context: Context) {
    val username: Flow<String?> = context.dataStore.data.map { it[PrefKeys.USERNAME] }
    val theme: Flow<String> = context.dataStore.data.map { it[PrefKeys.THEME] ?: "light" }

    suspend fun setUsername(name: String) {
        context.dataStore.edit { it[PrefKeys.USERNAME] = name }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { it[PrefKeys.THEME] = theme }
    }
}

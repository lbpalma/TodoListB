package com.example.todolistb.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistb.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsState(
    val username: String? = null,
    val theme: String = "light"
) {
    val darkTheme: Boolean get() = theme == "dark"
    val hasUsername: Boolean get() = !username.isNullOrBlank()
}

class SettingsViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = UserPreferences(app.applicationContext)

    val state: StateFlow<SettingsState> =
        prefs.username.combine(prefs.theme) { u, t -> SettingsState(u, t) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsState())

    fun updateUsername(name: String) {
        viewModelScope.launch { prefs.setUsername(name.trim()) }
    }

    fun updateTheme(dark: Boolean) {
        viewModelScope.launch { prefs.setTheme(if (dark) "dark" else "light") }
    }
}

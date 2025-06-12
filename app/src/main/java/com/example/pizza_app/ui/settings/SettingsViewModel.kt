// SettingsViewModel.kt
package com.example.pizza_app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val language: String = "en",
    val showLanguageDialog: Boolean = false
)

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun toggleDarkMode() {
        _uiState.value = _uiState.value.copy(
            isDarkMode = !_uiState.value.isDarkMode
        )
        saveThemePreference(_uiState.value.isDarkMode)
    }

    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(
            language = language
        )
        saveLanguagePreference(language)
    }

    fun showLanguageDialog() {
        _uiState.value = _uiState.value.copy(
            showLanguageDialog = true
        )
    }

    fun dismissLanguageDialog() {
        _uiState.value = _uiState.value.copy(
            showLanguageDialog = false
        )
    }

    private fun saveThemePreference(isDarkMode: Boolean) {
        viewModelScope.launch {

        }
    }

    private fun saveLanguagePreference(language: String) {
        viewModelScope.launch {

        }
    }

    fun loadSavedPreferences() {
        viewModelScope.launch {

        }
    }
}
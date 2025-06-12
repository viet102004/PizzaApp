package com.example.pizza_app.data.repository

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isDarkMode = MutableStateFlow(getDarkMode())
    val isDarkMode: Flow<Boolean> = _isDarkMode.asStateFlow()

    private val _language = MutableStateFlow(getLanguage())
    val language: Flow<String> = _language.asStateFlow()

    fun saveDarkMode(isDarkMode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_DARK_MODE, isDarkMode)
            .apply()
        _isDarkMode.value = isDarkMode
    }

    fun getDarkMode(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false)
    }

    fun saveLanguage(language: String) {
        sharedPreferences.edit()
            .putString(KEY_LANGUAGE, language)
            .apply()
        _language.value = language
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    companion object {
        private const val PREFS_NAME = "pizza_app_preferences"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_LANGUAGE = "language"
    }
}
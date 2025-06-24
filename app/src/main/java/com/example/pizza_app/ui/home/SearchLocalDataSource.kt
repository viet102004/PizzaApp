package com.example.pizza_app.ui.home

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchLocalDataSource(context: Context? = null) {

    private val sharedPreferences: SharedPreferences? = context?.getSharedPreferences(
        "pizza_app_search_prefs",
        Context.MODE_PRIVATE
    )

    private val recentSearchesKey = "recent_searches"
    private val maxRecentSearches = 10

    suspend fun getRecentSearches(): List<String> = withContext(Dispatchers.IO) {
        val searchesString = sharedPreferences?.getString(recentSearchesKey, "") ?: ""
        if (searchesString.isEmpty()) {
            emptyList()
        } else {
            searchesString.split(",").filter { it.isNotEmpty() }
        }
    }

    suspend fun addToRecentSearches(query: String) = withContext(Dispatchers.IO) {
        val currentSearches = getRecentSearches().toMutableList()

        // Remove if already exists
        currentSearches.remove(query)

        // Add to beginning
        currentSearches.add(0, query)

        // Keep only max recent searches
        if (currentSearches.size > maxRecentSearches) {
            currentSearches.removeAt(currentSearches.size - 1)
        }

        // Save back to SharedPreferences
        val searchesString = currentSearches.joinToString(",")
        sharedPreferences?.edit()?.putString(recentSearchesKey, searchesString)?.apply()
    }

    suspend fun removeFromRecentSearches(query: String) = withContext(Dispatchers.IO) {
        val currentSearches = getRecentSearches().toMutableList()
        currentSearches.remove(query)

        val searchesString = currentSearches.joinToString(",")
        sharedPreferences?.edit()?.putString(recentSearchesKey, searchesString)?.apply()
    }

    suspend fun clearRecentSearches() = withContext(Dispatchers.IO) {
        sharedPreferences?.edit()?.remove(recentSearchesKey)?.apply()
    }
}
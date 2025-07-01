package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val REFRESH_INTERVAL = 30_000L // 30 giây

    init {
        fetchDanhMuc()
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(REFRESH_INTERVAL)
                fetchDanhMuc(isRefresh = true)
            }
        }
    }

    fun refreshData() {
        fetchDanhMuc(isRefresh = true)
    }

    fun onResume() {
        fetchDanhMuc(isRefresh = true)
    }

    private fun fetchDanhMuc(isRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                if (!isRefresh) _isLoading.value = true
                val result = RetrofitInstance.api.getDanhMuc()
                _categories.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

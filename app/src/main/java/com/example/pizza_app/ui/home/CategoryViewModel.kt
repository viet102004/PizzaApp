package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        fetchDanhMuc()
    }

    private fun fetchDanhMuc() {
        viewModelScope.launch {
            try {
                _categories.value = RetrofitInstance.api.getDanhMuc()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

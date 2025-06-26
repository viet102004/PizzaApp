package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class SearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()

    private var searchJob: Job? = null
    private var allProducts: List<Product> = emptyList()

    init {
        loadAllProducts()
    }

    private fun loadAllProducts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                allProducts = RetrofitInstance.api.getSanPham()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun search(query: String) {
        // Hủy tìm kiếm trước đó nếu có
        searchJob?.cancel()

        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _hasSearched.value = false
            return
        }

        searchJob = viewModelScope.launch {
            _isLoading.value = true
            _hasSearched.value = true

            // Thêm delay nhỏ để tránh tìm kiếm quá nhanh khi user đang gõ
            delay(100)

            try {
                // Tìm kiếm trong danh sách đã load
                val results = allProducts.filter { product ->
                    product.ten_san_pham.contains(query, ignoreCase = true) ||
                            product.mo_ta?.contains(query, ignoreCase = true) == true
                }

                _searchResults.value = results
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearResults() {
        _searchResults.value = emptyList()
        _hasSearched.value = false
    }
}
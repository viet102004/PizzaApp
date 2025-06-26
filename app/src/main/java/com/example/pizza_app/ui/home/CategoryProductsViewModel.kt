package com.example.pizza_app.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryProductsViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    fun loadProductsByCategory(maDanhMuc: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = RetrofitInstance.api.getSanPhamTheoDanhMuc(maDanhMuc)

                _products.value = response.danh_sach_san_pham

            } catch (e: Exception) {
                _error.value = e.message ?: "Lỗi không xác định"
                _products.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh(maDanhMuc: Int) {
        loadProductsByCategory(maDanhMuc)
    }

    fun clearError() {
        _error.value = null
    }
}

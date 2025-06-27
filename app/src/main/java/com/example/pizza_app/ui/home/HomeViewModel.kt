package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class HomeViewModel : ViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Thời gian refresh (milliseconds) - 30 giây
    private val REFRESH_INTERVAL = 30_000L

    init {
        // Load dữ liệu lần đầu
        fetchSanPham()
        // Bắt đầu auto refresh
        startAutoRefresh()
    }

    // CÁCH 1: Auto Refresh với Timer
    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(REFRESH_INTERVAL)
                fetchSanPham(isRefresh = true)
            }
        }
    }

    // CÁCH 2: Manual refresh function cho Pull-to-Refresh
    fun refreshData() {
        fetchSanPham(isRefresh = true)
    }

    // CÁCH 3: Load khi user quay lại màn hình
    fun onResume() {
        fetchSanPham(isRefresh = true)
    }

    private fun fetchSanPham(isRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _isLoading.value = true
                }
                _error.value = null

                val result = RetrofitInstance.api.getSanPham()
                println("Sản phẩm API: $result")
                _productList.value = result

            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Lỗi tải dữ liệu: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Dừng auto refresh khi không cần thiết
    override fun onCleared() {
        super.onCleared()
        // Coroutines sẽ tự động cancel khi ViewModel bị destroy
    }
}
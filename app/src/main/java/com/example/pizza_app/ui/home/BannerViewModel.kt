package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Banner
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BannerViewModel : ViewModel() {

    private val _bannerList = MutableStateFlow<List<Banner>>(emptyList())
    val bannerList: StateFlow<List<Banner>> = _bannerList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Thời gian refresh tự động: 30 giây
    private val REFRESH_INTERVAL = 30_000L

    init {
        fetchBanners()
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(REFRESH_INTERVAL)
                fetchBanners(isRefresh = true)
            }
        }
    }

    fun refreshData() {
        fetchBanners(isRefresh = true)
    }

    fun onResume() {
        fetchBanners(isRefresh = true)
    }

    private fun fetchBanners(isRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                if (!isRefresh) _isLoading.value = true
                _error.value = null

                val banners = RetrofitInstance.api.getActiveBanners()
                _bannerList.value = banners
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Không thể tải banner: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

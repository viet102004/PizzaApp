package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Banner
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BannerViewModel : ViewModel() {

    private val _bannerList = MutableStateFlow<List<Banner>>(emptyList())
    val bannerList: StateFlow<List<Banner>> = _bannerList

    init {
        fetchBanners()
    }

    private fun fetchBanners() {
        viewModelScope.launch {
            try {
                _bannerList.value = RetrofitInstance.api.getActiveBanners()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

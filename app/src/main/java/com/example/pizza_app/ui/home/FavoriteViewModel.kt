package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.FavoriteProduct
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {
    private val _favoriteProducts = MutableStateFlow<List<FavoriteProduct>>(emptyList())
    val favoriteProducts: StateFlow<List<FavoriteProduct>> = _favoriteProducts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchFavorites() {
        viewModelScope.launch {
            val user = UserManager.currentUser.firstOrNull()
            val userId = user?.ma_nguoi_dung ?: return@launch

            _isLoading.value = true
            _error.value = null

            try {
                val response = RetrofitInstance.api.getFavoriteProductsWithDetails(userId)
                _favoriteProducts.value = response.data
            } catch (e: Exception) {
                _error.value = e.message
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFromFavorites(maSanPham: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = UserManager.currentUser.firstOrNull()
            val userId = user?.ma_nguoi_dung ?: return@launch

            try {
                val response = RetrofitInstance.api.removeFromFavorites(userId, maSanPham)
                if (response.isSuccessful) {
                    // Remove from local list
                    _favoriteProducts.value = _favoriteProducts.value.filter {
                        it.ma_san_pham != maSanPham
                    }
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun addToFavorites(maSanPham: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = UserManager.currentUser.firstOrNull()
            val userId = user?.ma_nguoi_dung ?: return@launch

            try {
                val response = RetrofitInstance.api.addToFavorites(userId, maSanPham)
                if (response.isSuccessful) {
                    // Refresh the list
                    fetchFavorites()
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun checkIsFavorite(maSanPham: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = UserManager.currentUser.firstOrNull()
            val userId = user?.ma_nguoi_dung ?: return@launch

            try {
                val response = RetrofitInstance.api.checkIsFavorite(userId, maSanPham)
                onResult(response.isFavorite)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun refreshFavorites() {
        fetchFavorites()
    }
}
package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.model.ProductImage
import com.example.pizza_app.data.model.ProductOption
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _images = MutableStateFlow<List<ProductImage>>(emptyList())
    val images: StateFlow<List<ProductImage>> = _images

    private val _options = MutableStateFlow<List<ProductOption>>(emptyList())
    val options: StateFlow<List<ProductOption>> = _options

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchProductDetail(maSanPham: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _product.value = RetrofitInstance.api.getProductById(maSanPham)
                _images.value = RetrofitInstance.api.getHinhAnhSanPham(maSanPham)

                val response = RetrofitInstance.api.getProductOptions(maSanPham)
                println("OPTIONS RESPONSE = ${response.tuy_chon}")
                _options.value = response.tuy_chon
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(maSanPham: Int, isCurrentlyFavorite: Boolean) {
        val userId = UserManager.currentUser.value?.ma_nguoi_dung ?: return

        viewModelScope.launch {
            try {
                val response = if (isCurrentlyFavorite) {
                    RetrofitInstance.api.removeFromFavorites(userId, maSanPham)
                } else {
                    RetrofitInstance.api.addToFavorites(userId, maSanPham)
                }

                if (response.isSuccessful) {
                    _isFavorite.value = !isCurrentlyFavorite
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkIsFavorite(maSanPham: Int) {
        val userId = UserManager.currentUser.value?.ma_nguoi_dung ?: return
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.checkIsFavorite(userId, maSanPham)
                _isFavorite.value = response.isFavorite
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback: check from favorite list
                try {
                    val favoriteList = RetrofitInstance.api.getFavoriteProducts(userId)
                    val isFavorited = favoriteList.any { it.ma_san_pham == maSanPham.toLong() }
                    _isFavorite.value = isFavorited
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
}
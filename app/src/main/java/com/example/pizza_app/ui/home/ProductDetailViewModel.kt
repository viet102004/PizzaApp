package com.example.pizza_app.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.model.ProductImage
import com.example.pizza_app.data.model.ProductOption
import com.example.pizza_app.data.model.ReviewResponse
import com.example.pizza_app.data.model.ReviewStatsResponse
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // Thêm StateFlow cho cart
    private val _addToCartState = MutableStateFlow<AddToCartState>(AddToCartState.Idle)
    val addToCartState: StateFlow<AddToCartState> = _addToCartState

    private val _reviews = MutableStateFlow<List<ReviewResponse>>(emptyList())
    val reviews: StateFlow<List<ReviewResponse>> = _reviews.asStateFlow()

    private val _reviewStats = MutableStateFlow<ReviewStatsResponse?>(null)
    val reviewStats: StateFlow<ReviewStatsResponse?> = _reviewStats.asStateFlow()

    private val _isLoadingReviews = MutableStateFlow(false)
    val isLoadingReviews: StateFlow<Boolean> = _isLoadingReviews.asStateFlow()

    // Sealed class để quản lý trạng thái thêm vào giỏ hàng
    sealed class AddToCartState {
        object Idle : AddToCartState()
        object Loading : AddToCartState()
        object Success : AddToCartState()
        data class Error(val message: String) : AddToCartState()
    }

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

    fun fetchProductReviews(maSanPham: Int, page: Int = 1) {
        viewModelScope.launch {
            try {
                _isLoadingReviews.value = true
                val response = RetrofitInstance.api.getProductReviews(maSanPham, page, 5) // Lấy 5 review đầu tiên
                _reviews.value = response.danh_sach_danh_gia
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error fetching reviews", e)
                _reviews.value = emptyList()
            } finally {
                _isLoadingReviews.value = false
            }
        }
    }

    fun fetchReviewStats(maSanPham: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getReviewStats(maSanPham)
                _reviewStats.value = response
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error fetching review stats", e)
                _reviewStats.value = null
            }
        }
    }
}
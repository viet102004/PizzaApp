package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.model.ProductImage
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _images = MutableStateFlow<List<ProductImage>>(emptyList())
    val images: StateFlow<List<ProductImage>> = _images

    fun fetchProductDetail(maSanPham: Int) {
        viewModelScope.launch {
            try {
                _product.value = RetrofitInstance.api.getProductById(maSanPham)
                _images.value = RetrofitInstance.api.getHinhAnhSanPham(maSanPham)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

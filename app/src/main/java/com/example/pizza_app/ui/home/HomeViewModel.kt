package com.example.pizza_app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    init {
        fetchSanPham()
    }

    private fun fetchSanPham() {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getSanPham()
                println("Sản phẩm API: $result")
                _productList.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

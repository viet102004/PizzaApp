@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.cart

import android.util.Log
import androidx.compose.material3.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.data.model.*
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import com.example.pizza_app.ui.components.AuthDialog
import com.example.pizza_app.ui.cart.CartItemCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()

    private val _itemCount = MutableStateFlow(0)
    val itemCount: StateFlow<Int> = _itemCount.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    fun resetState() {
        _message.value = ""
        _success.value = false
    }

    init {
        fetchCartItems()
    }

    fun fetchCartItems() {
        val user = UserManager.getUser() ?: return
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getGioHang(user.ma_nguoi_dung)
                val items = response.mat_hang?.map { it.toCartItem() } ?: emptyList()
                _cartItems.value = items
                updateSummary()
                _message.value = response.message ?: ""
            } catch (e: Exception) {
                Log.e("CartViewModel", "Lỗi tải giỏ hàng", e)
                _message.value = "Lỗi tải giỏ hàng"
            }
        }
    }

    fun addToCart(
        product: Product,
        selectedOptions: Map<Int, Int>,
        quantity: Int,
        imageUrl: String,
        options: List<ProductOption>
    ) {
        val user = UserManager.getUser()
        if (user == null) {
            _message.value = "Người dùng chưa đăng nhập"
            return
        }

        viewModelScope.launch {
            try {
                val tuyChonList = selectedOptions.mapNotNull { (maLoaiTuyChon, maGiaTri) ->
                    val giaThem = options.find { it.ma_loai_tuy_chon == maLoaiTuyChon }
                        ?.gia_tri?.find { it.ma_gia_tri == maGiaTri }?.gia_them
                    giaThem?.let {
                        TuyChonRequest(ma_gia_tri = maGiaTri, gia_them = it)
                    }
                }

                val request = ThemVaoGioHangRequest(
                    ma_nguoi_dung = user.ma_nguoi_dung,
                    ma_san_pham = product.ma_san_pham.toInt(),
                    ma_combo = null,
                    loai_mat_hang = "san_pham",
                    so_luong = quantity,
                    ghi_chu = null,
                    tuy_chon = tuyChonList,
                    chi_tiet_combo = emptyList()
                )

                val response = RetrofitInstance.api.themVaoGioHang(request)
                if (response.data != null) {
                    _success.value = true
                    _message.value = response.message ?: "Đã thêm vào giỏ hàng"
                    fetchCartItems()
                } else {
                    _success.value = false
                    _message.value = response.message ?: "Không thể thêm vào giỏ hàng"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối máy chủ"
                _success.value = false
                Log.e("CartViewModel", "Lỗi thêm giỏ hàng", e)
            }
        }
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        val updatedItems = _cartItems.value.map { item ->
            if (item.id == itemId) item.copy(quantity = newQuantity, totalPrice = item.basePrice * newQuantity)
            else item
        }
        _cartItems.value = updatedItems
        updateSummary()
    }

    fun removeFromCart(itemId: String) {
        viewModelScope.launch {
            try {
                val maMatHang = itemId.split("_").last().toIntOrNull() ?: return@launch
                RetrofitInstance.api.xoaMatHangGioHang(maMatHang)
                fetchCartItems()
            } catch (e: Exception) {
                Log.e("CartViewModel", "Lỗi xóa mặt hàng", e)
            }
        }
    }

    private fun updateSummary() {
        val items = _cartItems.value
        _itemCount.value = items.sumOf { it.quantity }
        _totalAmount.value = items.sumOf { it.totalPrice }
    }

}



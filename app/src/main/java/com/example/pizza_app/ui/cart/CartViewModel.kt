@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.cart

import android.util.Log
import androidx.compose.material3.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.*
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _options = MutableStateFlow<List<ProductOption>>(emptyList())
    val options: StateFlow<List<ProductOption>> = _options

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

    // Thêm function để clear cart khi logout
    fun clearCart() {
        _cartItems.value = emptyList()
        _totalAmount.value = 0.0
        _itemCount.value = 0
        _message.value = ""
        _success.value = false
    }

    init {
        fetchCartItems()
    }



    fun fetchCartItems() {
        val user = UserManager.getUser()

        // Nếu user null (đã logout), clear cart
        if (user == null) {
            clearCart()
            return
        }

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
                // Clear cart nếu có lỗi (có thể do session hết hạn)
                clearCart()
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
            clearCart() // Clear cart nếu user không tồn tại
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

    fun updateCartItem(maMatHangGioHang: Int, newQuantity: Int, currentItem: CartItem) {
        val user = UserManager.getUser()
        if (user == null) {
            _message.value = "Người dùng chưa đăng nhập"
            clearCart()
            return
        }

        Log.d("CartViewModel", "=== UPDATE CART ITEM API CALL ===")
        Log.d("CartViewModel", "API URL will be: /capNhatGioHang/$maMatHangGioHang")
        Log.d("CartViewModel", "Request payload - so_luong: $newQuantity")
        Log.d("CartViewModel", "Request payload - ghi_chu: ${currentItem.note}")
        Log.d("CartViewModel", "Request payload - tuy_chon count: ${currentItem.extraOptions.size}")

        viewModelScope.launch {
            try {
                val request = CapNhatGioHangRequest(
                    so_luong = newQuantity,
                    ghi_chu = currentItem.note,
                    tuy_chon = currentItem.extraOptions,
                    chi_tiet_combo = emptyList()
                )

                Log.d("CartViewModel", "Making API call...")
                val response = RetrofitInstance.api.capNhatGioHang(maMatHangGioHang, request)
                Log.d("CartViewModel", "API Response - success: ${response.success}")
                Log.d("CartViewModel", "API Response - message: ${response.message}")

                if (response.success) {
                    _success.value = true
                    _message.value = response.message ?: "Đã cập nhật giỏ hàng thành công"
                    fetchCartItems()
                } else {
                    _success.value = false
                    _message.value = response.message ?: "Cập nhật thất bại"
                }

            } catch (e: Exception) {
                _success.value = false
                _message.value = "Lỗi cập nhật giỏ hàng: ${e.message}"
                Log.e("CartViewModel", "API Error details: ${e.message}")
                Log.e("CartViewModel", "Exception type: ${e::class.java.simpleName}")
                if (e is retrofit2.HttpException) {
                    Log.e("CartViewModel", "HTTP Status Code: ${e.code()}")
                    Log.e("CartViewModel", "HTTP Response: ${e.response()?.errorBody()?.string()}")
                }
            }
        }
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        val user = UserManager.getUser()
        if (user == null) {
            clearCart()
            return
        }

        val currentItem = _cartItems.value.find { it.id == itemId }
        if (currentItem == null) {
            _message.value = "Không tìm thấy mặt hàng trong giỏ hàng"
            Log.e("CartViewModel", "Item not found with ID: $itemId")
            return
        }

        // Enhanced debugging
        Log.d("CartViewModel", "=== UPDATE QUANTITY DEBUG ===")
        Log.d("CartViewModel", "Item ID: $itemId")
        Log.d("CartViewModel", "maMatHangGioHang: ${currentItem.maMatHangGioHang}")
        Log.d("CartViewModel", "New quantity: $newQuantity")
        Log.d("CartViewModel", "Current quantity: ${currentItem.quantity}")
        Log.d("CartViewModel", "Product name: ${currentItem.product.ten_san_pham}")
        Log.d("CartViewModel", "Extra options count: ${currentItem.extraOptions.size}")

        // Log the full cart items to see the structure
        Log.d("CartViewModel", "All cart items:")
        _cartItems.value.forEach { item ->
            Log.d("CartViewModel", "  - ID: ${item.id}, maMatHangGioHang: ${item.maMatHangGioHang}, Product: ${item.product.ten_san_pham}")
        }

        // Cập nhật UI ngay
        val updatedItem = currentItem.copy(
            quantity = newQuantity,
            totalPrice = calculateItemTotal(currentItem, newQuantity)
        )

        _cartItems.value = _cartItems.value.map {
            if (it.id == itemId) updatedItem else it
        }
        updateSummary()

        // Gọi API với thêm debug
        Log.d("CartViewModel", "Calling updateCartItem with maMatHangGioHang: ${currentItem.maMatHangGioHang}")
        updateCartItem(currentItem.maMatHangGioHang, newQuantity, currentItem)
    }

    // Also add debugging to updateCartItem

    fun removeFromCart(itemId: String) {
        val user = UserManager.getUser()
        if (user == null) {
            clearCart()
            return
        }

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

    fun clearAllItems() {
        val user = UserManager.getUser()
        if (user == null) {
            clearCart()
            return
        }

        viewModelScope.launch {
            try {
                val apiResponse = RetrofitInstance.api.xoaToanBoGioHang(user.ma_nguoi_dung)

                if (apiResponse.success) {
                    clearCart()
                    _message.value = "Đã xóa tất cả sản phẩm khỏi giỏ hàng"
                    // Hoặc sử dụng message từ API: _message.value = apiResponse.message
                } else {
                    clearAllItemsOneByOne()
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Lỗi xóa tất cả sản phẩm", e)
                // Fallback: xóa từng item một
                clearAllItemsOneByOne()
            }
        }
    }

    private fun clearAllItemsOneByOne() {
        viewModelScope.launch {
            try {
                val currentItems = _cartItems.value

                // Xóa từng item một cách tuần tự
                currentItems.forEach { item ->
                    try {
                        val maMatHang = item.id.split("_").last().toIntOrNull()
                        if (maMatHang != null) {
                            RetrofitInstance.api.xoaMatHangGioHang(maMatHang)
                        }
                    } catch (e: Exception) {
                        Log.e("CartViewModel", "Lỗi xóa item ${item.id}", e)
                    }
                }

                // Refresh cart sau khi xóa tất cả
                fetchCartItems()
                _message.value = "Đã xóa tất cả sản phẩm khỏi giỏ hàng"

            } catch (e: Exception) {
                Log.e("CartViewModel", "Lỗi xóa tất cả items", e)
                _message.value = "Có lỗi xảy ra khi xóa sản phẩm"
            }
        }
    }

    private fun calculateItemTotal(item: CartItem, newQuantity: Int): Double {
        val basePrice = item.product.gia_co_ban
        val optionsPrice = item.selectedOptions.sumOf { it.giaThem }
        return (basePrice + optionsPrice) * newQuantity
    }

    private fun updateSummary() {
        val items = _cartItems.value
        _itemCount.value = items.sumOf { it.quantity }
        _totalAmount.value = items.sumOf { it.totalPrice }
    }

}
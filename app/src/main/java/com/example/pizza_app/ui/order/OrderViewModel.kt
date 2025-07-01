package com.example.pizza_app.ui.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.Order
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.mapDatabaseStatusToUI
import com.example.pizza_app.data.source.mapUIStatusToDatabase
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _filteredOrders = MutableStateFlow<List<Order>>(emptyList())
    val filteredOrders: StateFlow<List<Order>> = _filteredOrders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadOrders() {
        val user = UserManager.getUser()
        if (user == null) {
            _errorMessage.value = "Không tìm thấy thông tin người dùng"
            return
        }

        val userId = try {
            user.ma_nguoi_dung
        } catch (e: Exception) {
            _errorMessage.value = "ID người dùng không hợp lệ"
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getOrders(maNguoiDung = userId)
                _orders.value = response
                _filteredOrders.value = response // mặc định hiển thị tất cả
                _errorMessage.value = null

                Log.d("OrderViewModel", "Tổng số đơn: ${response.size}")
                response.forEach { order ->
                    Log.d("OrderViewModel", "Mã đơn: ${order.ma_don_hang}, Trạng thái: ${order.trang_thai}")
                }

            } catch (e: Exception) {
                Log.e("OrderViewModel", "Lỗi khi lấy đơn hàng: ${e.message}", e)
                _errorMessage.value = "Lỗi khi tải đơn hàng: ${e.message ?: "Không xác định"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterOrdersByStatus(uiStatus: String) {
        val validStatuses = mapDatabaseStatusToUI(uiStatus)
        _filteredOrders.value = _orders.value.filter { it.trang_thai in validStatuses }
    }

    fun resetError() {
        _errorMessage.value = null
    }
}
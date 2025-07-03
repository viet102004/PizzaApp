package com.example.pizza_app.ui.cart

import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.DatHangRequest
import com.example.pizza_app.data.model.ResultResponse
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.ViewModel

class PayViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _orderResult = MutableStateFlow<ResultResponse?>(null)
    val orderResult: StateFlow<ResultResponse?> = _orderResult

    fun datHang(
        maThongTinGiaoHang: Int,
        phuongThucThanhToan: String,
        maGiamGia: Int? = null,
        ghiChu: String? = null,
        thoiGianGiaoDuKien: String? = null
    ) {
        val user = UserManager.getUser()
        if (user == null) {
            _message.value = "Không tìm thấy thông tin người dùng"
            return
        }

        val userId = try {
            // Kiểm tra tên field chính xác trong User model
            user.ma_nguoi_dung // hoặc user.maNguoiDung tùy thuộc vào model
        } catch (e: Exception) {
            _message.value = "ID người dùng không hợp lệ"
            Log.e("PayViewModel", "Lỗi lấy user ID", e)
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.datHang(
                    DatHangRequest(
                        ma_nguoi_dung = userId,
                        ma_thong_tin_giao_hang = maThongTinGiaoHang,
                        phuong_thuc_thanh_toan = phuongThucThanhToan,
                        ma_giam_gia = maGiamGia,
                        ghi_chu = ghiChu,
                        thoi_gian_giao_du_kien = thoiGianGiaoDuKien
                    )
                )
                _orderResult.value = response
                _message.value = "Đặt hàng thành công"
                Log.d("PayViewModel", "Đặt hàng thành công: $response")
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.message ?: "Không xác định"}"
                Log.e("PayViewModel", "Lỗi đặt hàng", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _message.value = null
        _orderResult.value = null
    }
}
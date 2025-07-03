
package com.example.pizza_app.ui.vouchers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.MaGiamGia
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VoucherViewModel : ViewModel() {
    private val _voucherList = MutableStateFlow<List<MaGiamGia>>(emptyList())
    val voucherList: StateFlow<List<MaGiamGia>> = _voucherList

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun fetchVouchers() {
        val user = UserManager.getUser()
        if (user == null) {
            _message.value = "Người dùng chưa đăng nhập"
            _voucherList.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.layMaGiamGiaNguoiDung(user.ma_nguoi_dung)
                _voucherList.value = response.danh_sach_ma_giam_gia
                _message.value = response.message ?: ""
            } catch (e: Exception) {
                _message.value = "Lỗi tải mã giảm giá: ${e.message}"
            }
        }
    }
}

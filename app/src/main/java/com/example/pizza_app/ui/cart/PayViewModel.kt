package com.example.pizza_app.ui.cart

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.DatHangRequest
import com.example.pizza_app.data.model.ResultResponse
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PayViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _orderResult = MutableStateFlow<ResultResponse?>(null)
    val orderResult: StateFlow<ResultResponse?> = _orderResult

    private val _paymentUrl = MutableStateFlow<String?>(null)
    val paymentUrl: StateFlow<String?> = _paymentUrl

    // Thêm state cho payment callback
    private val _paymentCallbackResult = MutableStateFlow<PaymentCallbackResult?>(null)
    val paymentCallbackResult: StateFlow<PaymentCallbackResult?> = _paymentCallbackResult

    // Thêm state để track pending payment
    private val _pendingPayment = MutableStateFlow<PendingPayment?>(null)
    val pendingPayment: StateFlow<PendingPayment?> = _pendingPayment

    // Thêm state cho dialog thanh toán thất bại
    private val _showPaymentFailureDialog = MutableStateFlow(false)
    val showPaymentFailureDialog: StateFlow<Boolean> = _showPaymentFailureDialog

    data class PaymentCallbackResult(
        val orderId: String,
        val resultCode: String,
        val isSuccess: Boolean,
        val message: String
    )

    data class PendingPayment(
        val orderId: String,
        val paymentMethod: String,
        val timestamp: Long,
        val timeoutDuration: Long = 300000L // 5 phút timeout
    )

    fun datHang(
        context: Context,
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
            user.ma_nguoi_dung
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

                if (response.phuong_thuc_thanh_toan == "momo" && !response.payment_url.isNullOrEmpty()) {
                    // Lưu thông tin pending payment
                    val orderId = response.ma_don_hang?.toString() ?: "unknown"
                    _pendingPayment.value = PendingPayment(
                        orderId = orderId,
                        paymentMethod = "momo",
                        timestamp = System.currentTimeMillis()
                    )

                    // Bắt đầu timeout timer
                    startPaymentTimeout()

                    // Thêm returnUrl và notifyUrl với deeplink
                    val modifiedUrl = addDeeplinkToPaymentUrl(response.payment_url)
                    _paymentUrl.value = modifiedUrl

                    // Mở MoMo app hoặc browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(modifiedUrl))
                    context.startActivity(intent)
                }

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

    private fun startPaymentTimeout() {
        viewModelScope.launch {
            delay(300000L) // 5 phút timeout

            // Kiểm tra xem thanh toán có được xử lý chưa
            val pending = _pendingPayment.value
            if (pending != null && _paymentCallbackResult.value == null) {
                // Timeout - hiển thị dialog thanh toán thất bại
                _showPaymentFailureDialog.value = true
                _paymentCallbackResult.value = PaymentCallbackResult(
                    orderId = pending.orderId,
                    resultCode = "timeout",
                    isSuccess = false,
                    message = "Thanh toán MoMo không thành công. Đơn hàng đã được đặt, bạn có thể thanh toán sau."
                )
            }
        }
    }

    fun onAppResumed() {
        // Gọi khi app resume và có pending payment
        val pending = _pendingPayment.value
        if (pending != null && _paymentCallbackResult.value == null) {
            val currentTime = System.currentTimeMillis()
            val timeDiff = currentTime - pending.timestamp

            // Nếu đã quá 30 giây và chưa có callback, coi như thanh toán thất bại
            if (timeDiff > 30000L) {
                _showPaymentFailureDialog.value = true
                _paymentCallbackResult.value = PaymentCallbackResult(
                    orderId = pending.orderId,
                    resultCode = "cancelled",
                    isSuccess = false,
                    message = "Thanh toán MoMo không thành công. Đơn hàng đã được đặt, bạn có thể thanh toán sau."
                )
            }
        }
    }

    private fun addDeeplinkToPaymentUrl(originalUrl: String): String {
        // Thêm deeplink callback vào URL thanh toán
        val uri = Uri.parse(originalUrl)
        val builder = uri.buildUpon()

        // Thêm returnUrl (deeplink về app)
        builder.appendQueryParameter("returnUrl", "pizzaapp://payment")

        return builder.build().toString()
    }

    fun handlePaymentReturn(orderId: String, resultCode: String) {
        val isSuccess = resultCode == "0"
        val message = if (isSuccess) {
            "Thanh toán MoMo thành công cho đơn hàng #$orderId"
        } else {
            "Đơn hàng #$orderId đã được đặt thành công nhưng thanh toán MoMo thất bại. Bạn có thể thanh toán sau hoặc chọn phương thức khác."
        }

        _paymentCallbackResult.value = PaymentCallbackResult(
            orderId = orderId,
            resultCode = resultCode,
            isSuccess = isSuccess,
            message = message
        )

        _message.value = message

        // Clear pending payment
        _pendingPayment.value = null

        // Luôn verify order status vì đơn hàng đã được tạo
        verifyOrderStatus(orderId)
    }

    private fun verifyOrderStatus(orderId: String) {
        viewModelScope.launch {
            try {
                // Gọi API để verify trạng thái đơn hàng
                // val orderStatus = RetrofitInstance.api.getOrderStatus(orderId)
                // _orderResult.value = orderStatus

                Log.d("PayViewModel", "Verifying order status for: $orderId")
            } catch (e: Exception) {
                Log.e("PayViewModel", "Error verifying order status", e)
            }
        }
    }

    fun dismissPaymentFailureDialog() {
        _showPaymentFailureDialog.value = false
        _pendingPayment.value = null
    }

    fun resetState() {
        _message.value = null
        _orderResult.value = null
        _paymentUrl.value = null
        _paymentCallbackResult.value = null
        _pendingPayment.value = null
        _showPaymentFailureDialog.value = false
    }
}
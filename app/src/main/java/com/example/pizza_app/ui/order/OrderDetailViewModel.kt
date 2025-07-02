package com.example.pizza_app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.OrderDetail
import com.example.pizza_app.data.model.ReviewRequest
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel : ViewModel() {

    private val _orderDetail = MutableStateFlow<OrderDetail?>(null)
    val orderDetail: StateFlow<OrderDetail?> = _orderDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // State cho việc hủy đơn hàng
    private val _isCancelling = MutableStateFlow(false)
    val isCancelling: StateFlow<Boolean> = _isCancelling

    private val _cancelMessage = MutableStateFlow<String?>(null)
    val cancelMessage: StateFlow<String?> = _cancelMessage

    fun getOrderDetail(orderId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getOrderDetail(orderId)
                if (response.isSuccessful) {
                    _orderDetail.value = response.body()
                } else {
                    _errorMessage.value = response.errorBody()?.string() ?: "Đã xảy ra lỗi"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Không thể kết nối đến máy chủ"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cancelOrder(orderId: Int) {
        _isCancelling.value = true
        _cancelMessage.value = null

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.cancelOrder(orderId)

                if (response.isSuccessful) {
                    _cancelMessage.value = "Đơn hàng đã được hủy thành công"
                    // Load lại chi tiết đơn hàng
                    getOrderDetail(orderId)
                } else {
                    val code = response.code()
                    val error = response.errorBody()?.string() ?: ""

                    _cancelMessage.value = when (code) {
                        400 -> "Không thể hủy đơn hàng này"
                        404 -> "Không tìm thấy đơn hàng"
                        409 -> "Đơn hàng đã được xử lý, không thể hủy"
                        else -> "Lỗi $code: $error"
                    }
                }
            } catch (e: Exception) {
                _cancelMessage.value = "Không thể kết nối đến máy chủ"
            } finally {
                _isCancelling.value = false
            }
        }
    }


    // Xóa thông báo hủy đơn hàng
    fun clearCancelMessage() {
        _cancelMessage.value = null
    }

    // Xóa thông báo lỗi
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    private val _isSubmittingReview = MutableStateFlow(false)
    val isSubmittingReview: StateFlow<Boolean> = _isSubmittingReview.asStateFlow()

    fun submitReview(orderId: Int, productId: Long, rating: Int, comment: String, imageUri: String?) {
        viewModelScope.launch {
            try {
                _isSubmittingReview.value = true

                // If image is provided, upload it first
                var imageUrl: String? = null
                if (imageUri != null) {
                    // Upload image and get URL
                    imageUrl = uploadImage(imageUri) // You need to implement this
                }

                val reviewRequest = ReviewRequest(
                    ma_nguoi_dung = getCurrentUserId(), // Implement this method
                    ma_san_pham = productId,
                    ma_don_hang = orderId,
                    diem_so = rating,
                    binh_luan = comment.takeIf { it.isNotBlank() },
                    hinh_anh_danh_gia = imageUrl
                )

                // Call API to submit review
                RetrofitInstance.api.submitReview(reviewRequest)

                // Refresh order detail to show updated status
                getOrderDetail(orderId)

            } catch (e: Exception) {
                _errorMessage.value = "Lỗi khi gửi đánh giá: ${e.message}"
            } finally {
                _isSubmittingReview.value = false
            }
        }
    }

    private suspend fun uploadImage(imageUri: String): String? {
        // Implement image upload logic here
        // This should upload the image to your server and return the URL
        // For now, return the URI as placeholder
        return imageUri
    }

    private fun getCurrentUserId(): Int {
        val user = UserManager.getUser()

        return user!!.ma_nguoi_dung // Replace with actual implementation
    }
}
package com.example.pizza_app.ui.order

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.OrderDetail
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

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

    private val _isSubmittingReview = MutableStateFlow(false)
    val isSubmittingReview: StateFlow<Boolean> = _isSubmittingReview.asStateFlow()

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

    fun submitReview(orderId: Int, productId: Long, rating: Int, comment: String, imageUri: String?, context: Context) {
        viewModelScope.launch {
            try {
                _isSubmittingReview.value = true

                // Tạo RequestBody cho các field bắt buộc
                val maNguoiDung = getCurrentUserId().toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val maSanPham = productId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val maDonHang = orderId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val diemSo = rating.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                // Tạo RequestBody cho comment (có thể null)
                val binhLuan = if (comment.isNotBlank()) {
                    comment.toRequestBody("text/plain".toMediaTypeOrNull())
                } else null

                // Xử lý ảnh
                val imageParts = mutableListOf<MultipartBody.Part>()
                if (imageUri != null) {
                    val imageFile = uriToFile(imageUri, context)
                    if (imageFile != null) {
                        val resizedFile = resizeImageIfNeeded(imageFile, context = context)
                        val requestFile = resizedFile.asRequestBody("image/*".toMediaTypeOrNull())
                        val imagePart = MultipartBody.Part.createFormData("images", resizedFile.name, requestFile)
                        imageParts.add(imagePart)
                    }
                }

                // Gọi API
                val response = RetrofitInstance.api.submitReview(
                    maNguoiDung = maNguoiDung,
                    maSanPham = maSanPham,
                    maDonHang = maDonHang,
                    diemSo = diemSo,
                    binhLuan = binhLuan,
                    images = imageParts.takeIf { it.isNotEmpty() }
                )

                if (response.isSuccessful) {
                    // Refresh order detail to show updated status
                    getOrderDetail(orderId)
                    _errorMessage.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Lỗi khi gửi đánh giá: $errorBody"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Lỗi khi gửi đánh giá: ${e.message}"
            } finally {
                _isSubmittingReview.value = false
            }
        }
    }

    // Chuyển đổi Uri thành File
    private fun uriToFile(imageUri: String, context: Context): File? {
        return try {
            val uri = Uri.parse(imageUri)
            val inputStream = context.contentResolver.openInputStream(uri)

            if (inputStream != null) {
                val file = File.createTempFile("review_image_", ".jpg", context.cacheDir)
                val outputStream = FileOutputStream(file)

                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()

                file
            } else {
                null
            }
        } catch (e: Exception) {
            _errorMessage.value = "Lỗi xử lý ảnh: ${e.message}"
            null
        }
    }

    // Helper function để resize ảnh nếu cần
    private fun resizeImageIfNeeded(file: File, maxWidth: Int = 800, maxHeight: Int = 600, context: Context): File {
        return try {
            val bitmap = android.graphics.BitmapFactory.decodeFile(file.path)

            // Kiểm tra nếu ảnh đã đủ nhỏ
            if (bitmap.width <= maxWidth && bitmap.height <= maxHeight) {
                return file
            }

            val ratio = minOf(
                maxWidth.toFloat() / bitmap.width,
                maxHeight.toFloat() / bitmap.height
            )

            val newWidth = (bitmap.width * ratio).toInt()
            val newHeight = (bitmap.height * ratio).toInt()

            val resizedBitmap = android.graphics.Bitmap.createScaledBitmap(
                bitmap, newWidth, newHeight, true
            )

            val resizedFile = File.createTempFile("resized_", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(resizedFile)

            resizedBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.close()

            bitmap.recycle()
            resizedBitmap.recycle()

            resizedFile
        } catch (e: Exception) {
            file // Trả về file gốc nếu resize thất bại
        }
    }

    private fun getCurrentUserId(): Int {
        val user = UserManager.getUser()
        return user!!.ma_nguoi_dung
    }
}
package com.example.pizza_app.ui.order

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.CancelOrderRequest
import com.example.pizza_app.data.model.OrderDetail
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

    // Thêm state cho auto cancel timer
    private val _remainingTime = MutableStateFlow<Long?>(null)
    val remainingTime: StateFlow<Long?> = _remainingTime.asStateFlow()

    private val _isAutoCancel = MutableStateFlow(false)
    val isAutoCancel: StateFlow<Boolean> = _isAutoCancel.asStateFlow()

    // Thêm state để track các sản phẩm đã được đánh giá
    private val _reviewedProductIds = MutableStateFlow<Set<Long>>(emptySet())
    val reviewedProductIds: StateFlow<Set<Long>> = _reviewedProductIds.asStateFlow()

    // Thêm state để hiển thị thông báo thành công
    private val _reviewSuccessMessage = MutableStateFlow<String?>(null)
    val reviewSuccessMessage: StateFlow<String?> = _reviewSuccessMessage.asStateFlow()

    // Job để quản lý countdown
    private var countdownJob: Job? = null

    fun getOrderDetail(orderId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getOrderDetail(orderId)
                if (response.isSuccessful) {
                    val orderDetail = response.body()
                    if (orderDetail != null) {
                        _orderDetail.value = orderDetail
                        val order = orderDetail.don_hang

                        // Load danh sách sản phẩm đã được đánh giá
                        loadReviewedProducts(orderId)

                        // Bắt đầu countdown nếu đơn hàng đang chờ xác nhận
                        if (order.trang_thai == "cho_xac_nhan") {
                            startAutoCancelCountdown(order.ngay_dat, orderId)
                        } else {
                            stopCountdown()
                        }
                    } else {
                        _errorMessage.value = "Không có dữ liệu đơn hàng"
                    }
                } else {
                    // Xử lý lỗi response
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = when (response.code()) {
                        404 -> "Không tìm thấy đơn hàng"
                        403 -> "Bạn không có quyền xem đơn hàng này"
                        401 -> "Phiên đăng nhập đã hết hạn"
                        else -> errorBody ?: "Đã xảy ra lỗi (${response.code()})"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = when (e) {
                    is java.net.UnknownHostException -> "Không có kết nối internet"
                    is java.net.SocketTimeoutException -> "Kết nối quá chậm, vui lòng thử lại"
                    is java.net.ConnectException -> "Không thể kết nối đến máy chủ"
                    else -> "Lỗi không xác định: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadReviewedProducts(orderId: Int) {
        viewModelScope.launch {
            try {
                // Gọi API để lấy danh sách ID sản phẩm đã được đánh giá cho đơn hàng này
                val response = RetrofitInstance.api.getReviewedProducts(orderId)
                if (response.isSuccessful) {
                    val reviewedProducts = response.body()
                    if (reviewedProducts != null) {
                        // Chuyển đổi List<Int> thành Set<Long> để phù hợp với _reviewedProductIds
                        val reviewedIds = reviewedProducts.danh_sach_ma_san_pham_da_danh_gia
                            .map { it.toLong() }
                            .toSet()
                        _reviewedProductIds.value = reviewedIds
                    }
                }
            } catch (e: Exception) {
                // Không cần hiển thị lỗi cho việc load reviewed products
                // Chỉ log hoặc xử lý silent
                e.printStackTrace()
            }
        }
    }

    // Hàm bắt đầu countdown tự động hủy
    private fun startAutoCancelCountdown(orderDate: String, orderId: Int) {
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch {
            try {
                // Parse ngày đặt hàng (giả sử format: "2024-01-15 14:30:00")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val orderTime = dateFormat.parse(orderDate)?.time ?: return@launch

                val timeLimit = TimeUnit.MINUTES.toMillis(30) // 30 phút
                val endTime = orderTime + timeLimit

                while (true) {
                    val currentTime = System.currentTimeMillis()
                    val remaining = endTime - currentTime

                    if (remaining <= 0) {
                        // Hết thời gian, tự động hủy
                        performAutoCancel(orderId)
                        break
                    }

                    _remainingTime.value = remaining
                    delay(1000) // Cập nhật mỗi giây
                }

            } catch (e: Exception) {
                _errorMessage.value = "Lỗi xử lý thời gian: ${e.message}"
            }
        }
    }

    // Hàm thực hiện tự động hủy đơn hàng
    private suspend fun performAutoCancel(orderId: Int) {
        _isAutoCancel.value = true
        _remainingTime.value = null

        try {
            val cancelRequest = CancelOrderRequest(
                ly_do = "Hệ thống tự động hủy do quá thời gian xác nhận (30 phút)"
            )
            val response = RetrofitInstance.api.cancelOrder(orderId, cancelRequest)

            if (response.isSuccessful) {
                _cancelMessage.value = "Đơn hàng đã được hủy tự động do quá thời gian xác nhận"
                // Tải lại chi tiết đơn hàng để cập nhật trạng thái
                getOrderDetail(orderId)
            } else {
                val code = response.code()
                val error = response.errorBody()?.string() ?: ""
                _errorMessage.value = "Lỗi khi hủy đơn hàng tự động: $code - $error"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Không thể kết nối đến máy chủ khi hủy đơn hàng tự động"
        } finally {
            _isAutoCancel.value = false
        }
    }

    // Hàm dừng countdown
    private fun stopCountdown() {
        countdownJob?.cancel()
        countdownJob = null
        _remainingTime.value = null
        _isAutoCancel.value = false
    }

    fun cancelOrder(orderId: Int, reason: String? = null) {
        _isCancelling.value = true
        _cancelMessage.value = null

        // Dừng countdown khi người dùng hủy thủ công
        stopCountdown()

        viewModelScope.launch {
            try {
                val cancelRequest = CancelOrderRequest(ly_do = reason)
                val response = RetrofitInstance.api.cancelOrder(orderId, cancelRequest)

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

    // Hàm submitReview được cải tiến

    // Hàm kiểm tra xem tất cả sản phẩm đã được đánh giá chưa
    fun areAllProductsReviewed(): Boolean {
        val orderDetail = _orderDetail.value ?: return false
        val reviewableProducts = orderDetail.mat_hang.filter {
            it.loai_mat_hang == "san_pham" && it.ma_san_pham != null
        }
        val reviewedIds = _reviewedProductIds.value

        return reviewableProducts.all { product ->
            product.ma_san_pham?.toLong()?.let { productId ->
                reviewedIds.contains(productId)
            } ?: false
        }
    }

    // Hàm lấy danh sách sản phẩm chưa được đánh giá
    fun getUnreviewedProducts(): List<Any> {
        val orderDetail = _orderDetail.value ?: return emptyList()
        val reviewedIds = _reviewedProductIds.value

        return orderDetail.mat_hang.filter { product ->
            product.loai_mat_hang == "san_pham" &&
                    product.ma_san_pham != null &&
                    !reviewedIds.contains(product.ma_san_pham?.toLong())
        }
    }


    // Cải tiến hàm submitReview
    fun submitReview(orderId: Int, productId: Long, rating: Int, comment: String, imageUri: String?, context: Context) {
        viewModelScope.launch {
            try {
                _isSubmittingReview.value = true
                _reviewSuccessMessage.value = null

                // Tạo RequestBody cho các field bắt buộc
                val maNguoiDung = getCurrentUserId().toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val maSanPham = productId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val maDonHang = orderId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val diemSo = rating.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                // Tạo RequestBody cho comment (có thể null)
                val binhLuan = if (comment.isNotBlank()) {
                    comment.toRequestBody("text/plain".toMediaTypeOrNull())
                } else null

                // Xử lý ảnh được cải tiến
                val imageParts = mutableListOf<MultipartBody.Part>()
                if (!imageUri.isNullOrBlank()) {
                    val imageFile = uriToFileImproved(imageUri, context)
                    if (imageFile != null && imageFile.exists()) {
                        val resizedFile = resizeImageSafely(imageFile, context)
                        val requestFile = resizedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imagePart = MultipartBody.Part.createFormData("images", resizedFile.name, requestFile)
                        imageParts.add(imagePart)
                    }
                }

                // Gọi API với xử lý null safety
                val response = RetrofitInstance.api.submitReview(
                    maNguoiDung = maNguoiDung,
                    maSanPham = maSanPham,
                    maDonHang = maDonHang,
                    diemSo = diemSo,
                    binhLuan = binhLuan,
                    images = if (imageParts.isNotEmpty()) imageParts else null
                )

                if (response.isSuccessful) {
                    // Thêm sản phẩm vào danh sách đã được đánh giá
                    val currentReviewedIds = _reviewedProductIds.value.toMutableSet()
                    currentReviewedIds.add(productId)
                    _reviewedProductIds.value = currentReviewedIds

                    // Tìm tên sản phẩm để hiển thị thông báo
                    val productName = _orderDetail.value?.mat_hang?.find {
                        it.ma_san_pham?.toLong() == productId
                    }?.ten_san_pham ?: "Sản phẩm"

                    _reviewSuccessMessage.value = "Đánh giá cho $productName đã được gửi thành công!"
                    _errorMessage.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Lỗi khi gửi đánh giá: ${response.code()} - $errorBody"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Lỗi khi gửi đánh giá: ${e.message}"
            } finally {
                _isSubmittingReview.value = false
            }
        }
    }

    // Cải tiến hàm uriToFile
    private fun uriToFileImproved(imageUri: String, context: Context): File? {
        return try {
            val uri = Uri.parse(imageUri)

            // Kiểm tra quyền truy cập
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                _errorMessage.value = "Không thể truy cập ảnh đã chọn"
                return null
            }

            // Tạo file tạm với tên unique
            val timestamp = System.currentTimeMillis()
            val file = File.createTempFile("review_image_${timestamp}_", ".jpg", context.cacheDir)

            inputStream.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            // Kiểm tra file đã được tạo thành công
            if (file.exists() && file.length() > 0) {
                file
            } else {
                _errorMessage.value = "Không thể tạo file ảnh"
                null
            }
        } catch (e: SecurityException) {
            _errorMessage.value = "Không có quyền truy cập ảnh"
            null
        } catch (e: Exception) {
            _errorMessage.value = "Lỗi xử lý ảnh: ${e.message}"
            null
        }
    }

    // Cải tiến hàm resize image với xử lý memory-safe
    private fun resizeImageSafely(file: File, context: Context, maxWidth: Int = 800, maxHeight: Int = 600, quality: Int = 85): File {
        return try {
            // Đọc kích thước ảnh trước khi decode
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.path, options)

            // Tính toán sample size để tránh OOM
            val sampleSize = calculateInSampleSize(options, maxWidth, maxHeight)

            // Decode ảnh với sample size
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inJustDecodeBounds = false
            }

            val bitmap = BitmapFactory.decodeFile(file.path, decodeOptions)
            if (bitmap == null) {
                _errorMessage.value = "Không thể đọc ảnh"
                return file
            }

            // Kiểm tra nếu ảnh đã đủ nhỏ
            if (bitmap.width <= maxWidth && bitmap.height <= maxHeight) {
                bitmap.recycle()
                return file
            }

            // Tính toán kích thước mới
            val ratio = minOf(
                maxWidth.toFloat() / bitmap.width,
                maxHeight.toFloat() / bitmap.height
            )

            val newWidth = (bitmap.width * ratio).toInt()
            val newHeight = (bitmap.height * ratio).toInt()

            // Tạo bitmap mới với kích thước đã resize
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)

            // Tạo file mới cho ảnh đã resize
            val resizedFile = File.createTempFile("resized_${System.currentTimeMillis()}_", ".jpg", context.cacheDir)

            FileOutputStream(resizedFile).use { outputStream ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            }

            // Giải phóng memory
            bitmap.recycle()
            resizedBitmap.recycle()

            resizedFile
        } catch (e: OutOfMemoryError) {
            _errorMessage.value = "Ảnh quá lớn, vui lòng chọn ảnh khác"
            file
        } catch (e: Exception) {
            _errorMessage.value = "Lỗi xử lý ảnh: ${e.message}"
            file
        }
    }

    // Helper function để tính sample size
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    // Thêm hàm validation cho file ảnh
    private fun validateImageFile(file: File): Boolean {
        return try {
            // Kiểm tra kích thước file (max 10MB)
            val maxFileSize = 10 * 1024 * 1024 // 10MB
            if (file.length() > maxFileSize) {
                _errorMessage.value = "Ảnh quá lớn (tối đa 10MB)"
                return false
            }

            // Kiểm tra format ảnh
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.path, options)

            if (options.outWidth <= 0 || options.outHeight <= 0) {
                _errorMessage.value = "File không phải là ảnh hợp lệ"
                return false
            }

            true
        } catch (e: Exception) {
            _errorMessage.value = "Không thể xác thực file ảnh"
            false
        }
    }

    private fun getCurrentUserId(): Int {
        val user = UserManager.getUser()
        return user!!.ma_nguoi_dung
    }

    // Xóa thông báo hủy đơn hàng
    fun clearCancelMessage() {
        _cancelMessage.value = null
    }

    // Xóa thông báo lỗi
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Xóa thông báo thành công của review
    fun clearReviewSuccessMessage() {
        _reviewSuccessMessage.value = null
    }

    // Hủy countdown khi ViewModel bị destroy
    override fun onCleared() {
        super.onCleared()
        stopCountdown()
    }
}
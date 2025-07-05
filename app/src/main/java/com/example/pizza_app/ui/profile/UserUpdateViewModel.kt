package com.example.pizza_app.ui.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.ApiResponse
import com.example.pizza_app.data.model.User
import com.example.pizza_app.data.model.UserPreferences
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.ApiService
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UserUpdateViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _isUploadingAvatar = MutableStateFlow(false)
    val isUploadingAvatar: StateFlow<Boolean> = _isUploadingAvatar

    private val _shouldRestartApp = MutableStateFlow(false)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    fun resetState() {
        _message.value = ""
        _success.value = false
    }

    fun updateEmail(newEmail: String, context: Context) {
        viewModelScope.launch {
            val user = UserManager.currentUser.value ?: return@launch  // Dùng .value của StateFlow
            _isLoading.value = true

            try {
                val response = RetrofitInstance.api.updateEmail(user.ma_nguoi_dung, newEmail)

                if (response.success) {
                    val updatedUser = user.copy(email = newEmail)
                    UserManager.setUser(updatedUser)  // Này sẽ update StateFlow
                    UserPreferences(context).saveUser(updatedUser)
                    _success.value = true
                    println("SUCCESS SET TO TRUE")
                } else {
                    _message.value = response.message
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối máy chủ"
                println("ERROR: ${e.message}") // Debug log
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateName(newName: String, context: Context) {
        val user = UserManager.getUser() ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updateName(user.ma_nguoi_dung, newName)
                if (response.success) {
                    val updatedUser = user.copy(ho_ten = newName)
                    UserManager.setUser(updatedUser)
                    UserPreferences(context).saveUser(updatedUser)
                    _success.value = true
                } else {
                    _message.value = response.message
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối máy chủ"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePhone(newPhone: String, context: Context) {
        val user = UserManager.getUser() ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updatePhone(user.ma_nguoi_dung, newPhone)
                if (response.success) {
                    val updatedUser = user.copy(so_dien_thoai = newPhone)
                    UserManager.setUser(updatedUser)
                    UserPreferences(context).saveUser(updatedUser)
                    _success.value = true
                } else {
                    _message.value = response.message
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối máy chủ"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePassword(currentPassword: String, newPassword: String, context: Context) {
        val user = UserManager.getUser() ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updatePassword(
                    maNguoiDung = user.ma_nguoi_dung,
                    matKhauCu = currentPassword,
                    matKhauMoi = newPassword
                )

                if (response.success) {
                    // Có thể không cần cập nhật user nếu mật khẩu không lưu trong local
                    _success.value = true
                } else {
                    _message.value = response.message
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối máy chủ"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateBirthDate(newBirthDate: String, context: Context) {
        val user = UserManager.getUser() ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updateBirthDate(user.ma_nguoi_dung, newBirthDate)
                if (response.success) {
                    val updatedUser = user.copy(ngay_sinh = newBirthDate)
                    UserManager.setUser(updatedUser)
                    UserPreferences(context).saveUser(updatedUser)
                    _success.value = true
                } else {
                    _message.value = response.message
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối máy chủ"
                Log.e("UserUpdateViewModel", "Lỗi updateBirthDate: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAvatar(imageUri: Uri, context: Context) {
        val user = UserManager.currentUser.value ?: return
        _isUploadingAvatar.value = true

        viewModelScope.launch {
            var tempFile: File? = null
            try {
                // Kiểm tra file ảnh hợp lệ
                if (!ImageUtils.isValidImageFile(context, imageUri)) {
                    _message.value = "File không phải là ảnh hợp lệ. Vui lòng chọn file JPG, PNG hoặc WebP"
                    return@launch
                }

                // Kiểm tra kích thước file gốc
                val originalSizeKB = ImageUtils.getFileSizeKB(context, imageUri)
                Log.d("UserUpdateViewModel", "Kích thước file gốc: ${originalSizeKB}KB")

                // Nén ảnh nếu cần
                tempFile = if (originalSizeKB > 500) {
                    ImageUtils.compressImage(context, imageUri, 500)
                } else {
                    // Nếu file đã đủ nhỏ, vẫn xử lý để đảm bảo orientation đúng
                    ImageUtils.compressImage(context, imageUri, originalSizeKB.toInt())
                }

                if (tempFile == null) {
                    _message.value = "Không thể xử lý file ảnh"
                    return@launch
                }

                Log.d("UserUpdateViewModel", "Kích thước file sau xử lý: ${tempFile.length() / 1024}KB")

                // Tạo MultipartBody.Part
                val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("anh_dai_dien", tempFile.name, requestFile)

                Log.d("UserUpdateViewModel", "Bắt đầu upload ảnh cho user ID: ${user.ma_nguoi_dung}")

                // Gọi API
                val response = RetrofitInstance.api.updateAvatar(user.ma_nguoi_dung, body)

                if (response.success) {
                    // Lấy đường dẫn ảnh từ response
                    val newAvatarPath = response.duong_dan ?: response.message
                    val updatedUser = user.copy(anh_dai_dien = newAvatarPath)

                    UserManager.setUser(updatedUser)
                    UserPreferences(context).saveUser(updatedUser)

                    _success.value = true
                    _message.value = "Cập nhật ảnh đại diện thành công"

                    Log.d("UserUpdateViewModel", "Upload ảnh thành công: $newAvatarPath")
                } else {
                    _message.value = response.message
                    Log.e("UserUpdateViewModel", "Upload ảnh thất bại: ${response.message}")
                }

            } catch (e: IOException) {
                _message.value = "Lỗi khi xử lý file ảnh"
                Log.e("UserUpdateViewModel", "Lỗi I/O khi upload ảnh: ${e.message}")
            } catch (e: Exception) {
                _message.value = "Lỗi khi tải ảnh lên: ${e.message}"
                Log.e("UserUpdateViewModel", "Lỗi upload ảnh: ${e.message}")
            } finally {
                tempFile?.let { file ->
                    try {
                        if (file.exists()) {
                            file.delete()
                            Log.d("UserUpdateViewModel", "Đã xóa file tạm thời")
                        }
                        else{}
                    } catch (e: Exception) {
                        Log.w("UserUpdateViewModel", "Không thể xóa file tạm thời: ${e.message}")
                    }
                }
                _isUploadingAvatar.value = false
            }
        }
    }

    fun refreshUserFromServer(context: Context) {
        val user = UserManager.getUser() ?: return

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUserById(user.ma_nguoi_dung)
                if (response.success && response.user != null) {
                    val updatedUser = response.user
                    UserManager.setUser(updatedUser)
                    UserPreferences(context).saveUser(updatedUser)
                }
            } catch (e: Exception) {
                Log.e("UserUpdateViewModel", "Lỗi refreshUser: ${e.message}")
            }
        }
    }

}
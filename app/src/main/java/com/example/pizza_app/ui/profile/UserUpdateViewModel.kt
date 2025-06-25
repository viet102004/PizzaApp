package com.example.pizza_app.ui.profile

import android.content.Context
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

class UserUpdateViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

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
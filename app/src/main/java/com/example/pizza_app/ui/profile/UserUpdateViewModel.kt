package com.example.pizza_app.ui.profile


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.User
import com.example.pizza_app.data.model.UserPreferences
import com.example.pizza_app.data.source.UserManager
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

    private fun handleSuccess(updatedUser: User, context: Context) {
        UserManager.currentUser = updatedUser
        UserPreferences(context).saveUser(updatedUser)
        _success.value = true
    }

    fun resetState() {
        _message.value = ""
        _success.value = false
    }

    fun updateEmail(newEmail: String, context: Context) {
        val user = UserManager.currentUser ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updateEmail(user.ma_nguoi_dung, newEmail)
                if (response.success) {
                    val updatedUser = user.copy(email = newEmail)
                    handleSuccess(updatedUser, context)
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

    fun updateName(newName: String, context: Context) {
        val user = UserManager.currentUser ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updateName(user.ma_nguoi_dung, newName)
                if (response.success) {
                    val updatedUser = user.copy(ho_ten = newName)
                    handleSuccess(updatedUser, context)
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
        val user = UserManager.currentUser ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updatePhone(user.ma_nguoi_dung, newPhone)
                if (response.success) {
                    val updatedUser = user.copy(so_dien_thoai = newPhone)
                    handleSuccess(updatedUser, context)
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

    fun updatePassword(newPassword: String, context: Context) {
        val user = UserManager.currentUser ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updatePassword(user.ma_nguoi_dung, newPassword)
                if (response.success) {
                    handleSuccess(user, context) // Không cần đổi gì trong user
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
}

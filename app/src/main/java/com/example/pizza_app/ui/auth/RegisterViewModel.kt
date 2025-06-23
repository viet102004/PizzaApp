package com.example.pizza_app.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.User
import com.example.pizza_app.data.model.UserResponse
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _registerSuccess = MutableStateFlow<User?>(null)
    val registerSuccess: StateFlow<User?> = _registerSuccess.asStateFlow()

    private val apiService = RetrofitInstance.api

    fun register(name: String, email: String,phone: String, password: String, confirmPassword: String, context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""

                // Validate inputs
                val validationResult = validateInputs(name, email, phone, password, confirmPassword)
                if (!validationResult.first) {
                    _errorMessage.value = validationResult.second
                    _isLoading.value = false
                    return@launch
                }

                // Gọi API đăng ký thực tế
                val registerResult = performRegister(name, email, phone, password)

                if (registerResult.success) {
                    _registerSuccess.value = registerResult.user
                    Log.d("RegisterViewModel", "Đăng ký thành công: ${registerResult.user}")
                } else {
                    _errorMessage.value = registerResult.errorMessage
                }

            } catch (e: Exception) {
                _errorMessage.value = "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại."
                Log.e("RegisterViewModel", "Registration error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun performRegister(name: String, email: String, phone: String, password: String): RegisterResult {
        return try {
            val response: UserResponse = RetrofitInstance.api.dangKy(
                email = email,
                soDienThoai = phone,
                matKhau = password,
                hoTen = name
            )
            if (response.success && response.user != null) {
                RegisterResult(true, "", response.user)
            } else {
                RegisterResult(false, response.message, null)
            }
        } catch (e: Exception) {
            RegisterResult(false, "Không thể kết nối đến server. Vui lòng thử lại", null)
        }
    }

    private fun validateInputs(name: String, email: String, phone: String, password: String, confirmPassword: String): Pair<Boolean, String> {
        if (name.isBlank()) return Pair(false, "Vui lòng nhập họ và tên")
        if (name.length < 2) return Pair(false, "Họ và tên phải có ít nhất 2 ký tự")
        if (name.length > 50) return Pair(false, "Họ và tên không được quá 50 ký tự")

        if (email.isBlank()) return Pair(false, "Vui lòng nhập email")
        if (!isValidEmail(email)) return Pair(false, "Email không hợp lệ")

        if (password.isBlank()) return Pair(false, "Vui lòng nhập mật khẩu")
        if (password.length < 6) return Pair(false, "Mật khẩu phải có ít nhất 6 ký tự")
        if (password.length > 128) return Pair(false, "Mật khẩu không được quá 128 ký tự")
        if (!isStrongPassword(password)) return Pair(false, "Mật khẩu phải chứa ít nhất 1 chữ cái và 1 số")

        if (confirmPassword.isBlank()) return Pair(false, "Vui lòng xác nhận mật khẩu")
        if (password != confirmPassword) return Pair(false, "Mật khẩu xác nhận không khớp")

        return Pair(true, "")
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailPattern.matcher(email).matches()
    }

    private fun isStrongPassword(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    fun resetState() {
        _isLoading.value = false
        _errorMessage.value = ""
        _registerSuccess.value = null
    }

    // Kết quả đăng ký
    data class RegisterResult(
        val success: Boolean,
        val errorMessage: String,
        val user: User?
    )
}

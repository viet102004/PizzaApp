package com.example.pizza_app.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
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

    fun register(name: String, email: String, password: String, confirmPassword: String, context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""

                // Validate inputs
                val validationResult = validateInputs(name, email, password, confirmPassword)
                if (!validationResult.first) {
                    _errorMessage.value = validationResult.second
                    _isLoading.value = false
                    return@launch
                }

                // Simulate API call delay
                delay(2000)

                // Mock API call - Replace with actual API implementation
                val registerResult = performRegister(name, email, password)

                if (registerResult.success) {
                    _registerSuccess.value = registerResult.user
                    Log.d("RegisterViewModel", "Registration successful: ${registerResult.user}")
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

    private fun validateInputs(name: String, email: String, password: String, confirmPassword: String): Pair<Boolean, String> {
        // Validate name
        if (name.isBlank()) {
            return Pair(false, "Vui lòng nhập họ và tên")
        }
        if (name.length < 2) {
            return Pair(false, "Họ và tên phải có ít nhất 2 ký tự")
        }
        if (name.length > 50) {
            return Pair(false, "Họ và tên không được quá 50 ký tự")
        }

        // Validate email
        if (email.isBlank()) {
            return Pair(false, "Vui lòng nhập email")
        }
        if (!isValidEmail(email)) {
            return Pair(false, "Email không hợp lệ")
        }

        // Validate password
        if (password.isBlank()) {
            return Pair(false, "Vui lòng nhập mật khẩu")
        }
        if (password.length < 6) {
            return Pair(false, "Mật khẩu phải có ít nhất 6 ký tự")
        }
        if (password.length > 128) {
            return Pair(false, "Mật khẩu không được quá 128 ký tự")
        }

        // Validate password strength
        if (!isStrongPassword(password)) {
            return Pair(false, "Mật khẩu phải chứa ít nhất 1 chữ cái và 1 số")
        }

        // Validate confirm password
        if (confirmPassword.isBlank()) {
            return Pair(false, "Vui lòng xác nhận mật khẩu")
        }
        if (password != confirmPassword) {
            return Pair(false, "Mật khẩu xác nhận không khớp")
        }

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

    private suspend fun performRegister(name: String, email: String, password: String): RegisterResult {
        // Mock API implementation
        // Replace this with actual API call using Retrofit or similar

        return try {
            // Simulate different scenarios
            when {
                email == "test@existing.com" -> {
                    RegisterResult(
                        success = false,
                        errorMessage = "Email này đã được sử dụng",
                        user = null
                    )
                }
                email == "error@test.com" -> {
                    RegisterResult(
                        success = false,
                        errorMessage = "Không thể kết nối đến server. Vui lòng thử lại.",
                        user = null
                    )
                }
                else -> {
                    // Success case
                    val user = User(
                        id = generateUserId(),
                        name = name,
                        email = email,
                        createdAt = System.currentTimeMillis()
                    )
                    RegisterResult(
                        success = true,
                        errorMessage = "",
                        user = user
                    )
                }
            }
        } catch (e: Exception) {
            RegisterResult(
                success = false,
                errorMessage = "Đã xảy ra lỗi khi đăng ký. Vui lòng thử lại.",
                user = null
            )
        }
    }

    private fun generateUserId(): String {
        return "user_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    fun resetState() {
        _isLoading.value = false
        _errorMessage.value = ""
        _registerSuccess.value = null
    }
}

// Data classes
data class User(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: Long,
    val avatarUrl: String? = null,
    val phoneNumber: String? = null
)

data class RegisterResult(
    val success: Boolean,
    val errorMessage: String,
    val user: User?
)

// Extension functions for better UX
fun String.isValidVietnamesePhoneNumber(): Boolean {
    val phonePattern = Pattern.compile("^(\\+84|84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-6|8|9]|9[0-4|6-9])[0-9]{7}$")
    return phonePattern.matcher(this).matches()
}

fun String.formatVietnameseName(): String {
    return this.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
}
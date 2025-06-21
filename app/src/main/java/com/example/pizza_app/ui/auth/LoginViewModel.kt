package com.example.pizza_app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizza_app.data.model.User
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _loginSuccess = MutableStateFlow<User?>(null)
    val loginSuccess: StateFlow<User?> = _loginSuccess

    fun login(taiKhoan: String, matKhau: String) {
        _isLoading.value = true
        _errorMessage.value = ""

        println("=== LOGIN DEBUG START ===")
        println("Attempting login with: $taiKhoan")

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.dangNhap(taiKhoan, matKhau)
                println("Raw response: $response")
                println("Response message: ${response.message}")
                println("Response user: ${response.user}")
                println("User details: ma_nguoi_dung=${response.user.ma_nguoi_dung}, email=${response.user.email}")

                _loginSuccess.value = response.user
                UserManager.currentUser = response.user
                println("_loginSuccess set to: ${_loginSuccess.value}")
            } catch (e: Exception) {
                println("Login exception: ${e.javaClass.simpleName}")
                println("Exception message: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = "Tài khoản hoặc mật khẩu không đúng"
            } finally {
                _isLoading.value = false
                println("=== LOGIN DEBUG END ===")
            }
        }
    }
}

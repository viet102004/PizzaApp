package com.example.pizza_app.data.source

import com.example.pizza_app.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun setUser(user: User?) {
        _currentUser.value = user?.copy() // Ép mỗi lần đều tạo object mới
    }

    fun getUser(): User? = _currentUser.value

    fun clearUser() {
        _currentUser.value = null
    }
}
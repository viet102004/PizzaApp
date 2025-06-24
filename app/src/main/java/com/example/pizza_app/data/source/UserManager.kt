package com.example.pizza_app.data.source

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pizza_app.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun setUser(user: User?) {
        // Nếu user khác thì set như thường
        if (_currentUser.value != user) {
            _currentUser.value = user
        } else if (user != null) {
            // Nếu giống nhau (do shallow compare), ép trigger update
            _currentUser.value = user.copy()
        }
    }

    fun getUser(): User? = _currentUser.value

    fun clearUser() {
        _currentUser.value = null
    }
}



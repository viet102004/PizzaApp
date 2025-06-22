package com.example.pizza_app.data.source

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pizza_app.data.model.User

object UserManager {
    var currentUser by mutableStateOf<User?>(null)
}


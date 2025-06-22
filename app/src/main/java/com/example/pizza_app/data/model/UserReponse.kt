package com.example.pizza_app.data.model

data class UserResponse(
    val success: Boolean,
    val user: User?,
    val message: String = ""
)

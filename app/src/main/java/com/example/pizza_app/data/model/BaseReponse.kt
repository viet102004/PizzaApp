package com.example.pizza_app.data.model

data class BaseResponse(
    val success: Boolean = true,
    val message: String,
    val status: Boolean,
)

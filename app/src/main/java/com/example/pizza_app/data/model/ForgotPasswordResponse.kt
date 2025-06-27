package com.example.pizza_app.data.model

data class ForgotPasswordResponse(
    val message: String
)

data class ApiError(
    val detail: String
)

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
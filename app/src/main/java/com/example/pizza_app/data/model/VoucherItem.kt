package com.example.pizza_app.data.model

data class VoucherItem(
    val id: String,
    val title: String,
    val description: String,
    val discount: String,
    val minOrder: String,
    val expiry: String,
    val discountValue: Int,
    val minOrderAmount: Int,
    val isPercentage: Boolean = false,
    val isUsed: Boolean = false
)
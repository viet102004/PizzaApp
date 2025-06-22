package com.example.pizza_app.data.model


data class CartItem(
    val id: String,
    val productId: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val size: String,
    val thickness: String,
    val img: String // Có thể là resource ID hoặc URL
)
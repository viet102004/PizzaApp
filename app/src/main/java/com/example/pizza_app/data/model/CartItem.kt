package com.example.pizza_app.data.model

data class CartItem(
    val name: String,
    val size: String,
    val price: Double,
    val imageRes: Int,
    var quantity: Int
)
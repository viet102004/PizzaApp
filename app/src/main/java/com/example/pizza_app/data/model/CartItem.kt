package com.example.pizza_app.data.model

data class CartItem(
    val id: String,
    val name: String,
    val size: String,
    val thickness: String,
    val price: Double,
    var quantity: Int,
    var img: Int
)
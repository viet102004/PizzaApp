package com.example.pizza_app.data.model

data class Order (
    val storeName: String,
    val orderTime: String,
    val status: String,
    val items: List<String>,
    val totalPrice: String
)
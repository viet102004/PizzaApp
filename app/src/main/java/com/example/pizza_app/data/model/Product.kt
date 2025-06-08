package com.example.pizza_app.data.model

data class Product (
    val id: Int,
    val name: String,
    val price: Int,
    val imageRes: Int,
    val description: String = ""
)
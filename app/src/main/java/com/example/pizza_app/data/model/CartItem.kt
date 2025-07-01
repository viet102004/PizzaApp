// CartItem.kt
package com.example.pizza_app.data.model

data class CartItem(
    val id: String,
    val productId: Int,
    val product: Product,
    val quantity: Int,
    val imageUrl: String,
    val basePrice: Double,
    val extraOptions: List<TuyChonRequest> = emptyList(),
    val selectedOptions: List<TuyChonHienThi> = emptyList(),
    val totalPrice: Double,
    val maMatHangGioHang: Int,
    val note: String? = null
)
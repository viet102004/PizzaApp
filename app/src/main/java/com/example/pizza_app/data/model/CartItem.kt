// CartItem.kt
package com.example.pizza_app.data.model

data class CartItem(
    val id: String = "", // Unique ID for cart item
    val product: Product,
    val selectedOptions: Map<Int, CartOption>, // ma_loai_tuy_chon -> CartOption
    val quantity: Int,
    val imageUrl: String,
    val totalPrice: Double,
    val addedAt: Long = System.currentTimeMillis()
)

data class CartOption(
    val maLoaiTuyChon: Int,
    val tenLoai: String,
    val maGiaTri: Int,
    val tenGiaTri: String,
    val giaThem: Double
)

// Extension function để tính tổng giá
fun CartItem.calculateTotalPrice(): Double {
    val basePrice = product.gia_co_ban
    val optionsPrice = selectedOptions.values.sumOf { it.giaThem }
    return (basePrice + optionsPrice) * quantity
}

// Extension function để tạo unique ID cho cart item
fun generateCartItemId(productId: Int, selectedOptions: Map<Int, Int>): String {
    val optionsString = selectedOptions.entries.sortedBy { it.key }
        .joinToString("-") { "${it.key}:${it.value}" }
    return "${productId}_${optionsString}"
}
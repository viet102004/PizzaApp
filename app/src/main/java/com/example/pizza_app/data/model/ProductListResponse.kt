package com.example.pizza_app.data.model

data class ProductListResponse(
    val ma_danh_muc: Int,
    val ten_danh_muc: String,
    val so_luong: Int,
    val danh_sach_san_pham: List<Product>
)

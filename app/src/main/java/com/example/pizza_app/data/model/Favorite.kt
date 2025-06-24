package com.example.pizza_app.data.model

data class ApiResponse(
    val success: Boolean,
    val message: String
)


data class FavoriteResponse(
    val data: List<FavoriteProduct>,
    val total: Int
)

data class FavoriteProduct(
    val ma_yeu_thich: Int,
    val ma_nguoi_dung: Int,
    val ma_san_pham: Int,
    val ten_san_pham: String,
    val gia_co_ban: Double,
    val mo_ta: String?,
    val url_hinh_anh: String?,
    val trang_thai: String,
    val ma_danh_muc: Int,
    val ten_danh_muc: String?
)

data class IsFavoriteResponse(
    val isFavorite: Boolean
)
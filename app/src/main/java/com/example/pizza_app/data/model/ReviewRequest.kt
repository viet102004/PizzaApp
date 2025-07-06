package com.example.pizza_app.data.model

data class ReviewRequest(
    val ma_nguoi_dung: Int,
    val ma_san_pham: Long,
    val ma_don_hang: Int,
    val diem_so: Int,
    val binh_luan: String? = null,
    val hinh_anh_danh_gia: String? = null
)
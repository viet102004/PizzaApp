package com.example.pizza_app.data.model

import java.util.Date

data class ReviewResponse(
    val ma_danh_gia: Int,
    val ten_nguoi_danh_gia: String?,
    val hinh_anh_nguoi_danh_gia: String?,
    val diem_so: Int,
    val binh_luan: String?,
    val hinh_anh_danh_gia: String?,
    val ngay_danh_gia: String // hoặc Date nếu bạn muốn parse
)

data class ProductReviewsResponse(
    val ten_san_pham: String,
    val tong_so_danh_gia: Int,
    val diem_trung_binh: Float,
    val danh_sach_danh_gia: List<ReviewResponse>
)

data class ReviewStatsResponse(
    val ten_san_pham: String,
    val tong_so_danh_gia: Int,
    val diem_trung_binh: Float,
    val phan_phoi_diem: Map<String, Int> // "5": 100, "4": 50, etc.
)
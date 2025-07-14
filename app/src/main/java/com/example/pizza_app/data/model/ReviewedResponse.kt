package com.example.pizza_app.data.model

// Data models cần thiết

data class OrderReviewResponse(
    val ma_danh_gia: Int,
    val ma_nguoi_dung: Int,
    val ma_san_pham: Int,
    val ma_don_hang: Int,
    val diem_so: Int,
    val binh_luan: String?,
    val hinh_anh_danh_gia: String?,
    val ngay_tao: String,
    val ten_san_pham: String,
    val hinh_anh_san_pham: String?,
    val ten_nguoi_danh_gia: String?
)

data class OrderReviewsListResponse(
    val ma_don_hang: Int,
    val tong_so_danh_gia: Int,
    val danh_sach_danh_gia: List<OrderReviewResponse>
)

data class ReviewedProductsResponse(
    val ma_don_hang: Int,
    val danh_sach_ma_san_pham_da_danh_gia: List<Int>
)

data class ProductReviewCheckResponse(
    val ma_don_hang: Int,
    val ma_san_pham: Int,
    val da_danh_gia: Boolean,
    val so_luong_danh_gia: Int
)

data class ReviewedProductResponse(
    val ma_san_pham: Int,
    val ten_san_pham: String,
    val hinh_anh_san_pham: String?,
    val so_luong: Int,
    val don_gia_co_ban: Double,
    val thanh_tien: Double,
    val ma_danh_gia: Int,
    val diem_so: Int,
    val binh_luan: String?,
    val hinh_anh_danh_gia: String?,
    val ngay_danh_gia: String
)
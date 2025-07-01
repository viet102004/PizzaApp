package com.example.pizza_app.data.model

data class Order(
    val ma_don_hang: Long,
    val ma_nguoi_dung: Long,
    val tong_tien_san_pham: Double,
    val phi_giao_hang: Double,
    val giam_gia_ma_giam_gia: Double,
    val giam_gia_combo: Double,
    val tong_tien_cuoi_cung: Double,
    val trang_thai: String,
    val phuong_thuc_thanh_toan: String,
    val trang_thai_thanh_toan: String,
    val ghi_chu: String? = null,
    val thoi_gian_giao_du_kien: String? = null,
    val ngay_tao: String,
    val ngay_cap_nhat: String,
    val items: List<MatHangDonHang> = emptyList(),
)

data class MatHangDonHang(
    val ma_mat_hang_don_hang: Long,
    val ma_don_hang: Long,
    val ma_san_pham: Long? = null,
    val ma_combo: Long? = null,
    val loai_mat_hang: String,
    val so_luong: Int,
    val don_gia_co_ban: Double,
    val tong_gia_tuy_chon: Double,
    val thanh_tien: Double,
    val ghi_chu: String? = null,

    // For backward compatibility and product/combo name
    val ten_san_pham: String? = null,
    val ten_combo: String? = null
)
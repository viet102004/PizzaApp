package com.example.pizza_app.data.model

data class AddressCreateRequest(
    val ten_nguoi_nhan: String,
    val so_dien_thoai_nguoi_nhan: String,
    val so_duong: String,
    val phuong_xa: String? = null,
    val quan_huyen: String? = null,
    val tinh_thanh_pho: String? = null,
    val la_dia_chi_mac_dinh: Int = 0,
    val ghi_chu: String? = null
)

// Cập nhật ApiResponse để hỗ trợ generic type
data class AddressResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val total: Int? = null
)

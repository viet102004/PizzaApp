package com.example.pizza_app.data.model

data class AddressInfo(
    val ma_thong_tin_giao_hang: Long? = null,
    val ten_nguoi_nhan: String,
    val so_dien_thoai_nguoi_nhan: String,
    val so_duong: String,
    val phuong_xa: String,
    val quan_huyen: String,
    val tinh_thanh_pho: String,
    var la_dia_chi_mac_dinh: Int,
    val ghi_chu: String
)
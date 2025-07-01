package com.example.pizza_app.data.model

data class DatHangRequest(
    val ma_nguoi_dung: Int,
    val ma_thong_tin_giao_hang: Int,
    val phuong_thuc_thanh_toan: String,
    val ma_giam_gia: Int? = null,
    val ghi_chu: String? = null,
    val thoi_gian_giao_du_kien: String? = null
)

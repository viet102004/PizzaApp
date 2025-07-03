package com.example.pizza_app.data.model

import java.math.BigDecimal

data class MaGiamGia(
    val ma_giam_gia: Int,
    val ma_code: String,
    val loai_giam_gia: String,
    val gia_tri_giam: BigDecimal,
    val ngay_bat_dau: String,
    val ngay_ket_thuc: String,
    val gia_tri_don_hang_toi_thieu: BigDecimal?,
    val so_lan_su_dung_toi_da: Int?,
    val da_su_dung: Int,
    val hoat_dong: Boolean,
    val ngay_tao: String
)

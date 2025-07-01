package com.example.pizza_app.data.model

data class CapNhatGioHangRequest(
    val so_luong: Int,
    val ghi_chu: String? = null,
    val tuy_chon: List<TuyChonRequest> = emptyList(),
    val chi_tiet_combo: List<ChiTietComboRequest> = emptyList()
)
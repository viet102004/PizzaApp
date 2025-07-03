package com.example.pizza_app.data.model

data class TuyChonRequest(
    val ma_loai_tuy_chon: Int,
    val ma_gia_tri: Int,
    val gia_them: Double
)


data class ChiTietComboRequest(
    val ma_chi_tiet_combo: Int,
    val tuy_chon: List<TuyChonRequest>
)

data class ThemVaoGioHangRequest(
    val ma_nguoi_dung: Int,
    val ma_san_pham: Int?,
    val ma_combo: Int?,
    val loai_mat_hang: String,
    val so_luong: Int,
    val ghi_chu: String? = null,
    val tuy_chon: List<TuyChonRequest> = emptyList(),
    val chi_tiet_combo: List<ChiTietComboRequest> = emptyList()
)

data class ThemGioHangResponse(
    val message: String,
    val ma_mat_hang_gio_hang: Int
)

data class AapiResponse<T>(
    val message: String,
    val data: T? = null
)

data class TuyChonHienThi(
    val tenLoai: String,
    val tenGiaTri: String,
    val giaThem: Double
)

data class GioHangResponse(
    val ma_gio_hang: Int,
    val ma_nguoi_dung: Int,
    val ngay_tao: String?,
    val mat_hang: List<MatHangResponse>?,
    val tong_tien: Double,
    val message: String? = null
)





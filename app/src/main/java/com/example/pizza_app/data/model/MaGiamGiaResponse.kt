package com.example.pizza_app.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class MaGiamGiaListResponse(
    @SerializedName("danh_sach_ma_giam_gia") val danh_sach_ma_giam_gia: List<MaGiamGiaResponse>
)

data class MaGiamGiaResponse(
    @SerializedName("ma_code") val ma_code: String,
    @SerializedName("loai_giam_gia") val loai_giam_gia: String,
    @SerializedName("gia_tri_giam") val gia_tri_giam: BigDecimal,
    @SerializedName("gia_tri_don_hang_toi_thieu") val gia_tri_don_hang_toi_thieu: BigDecimal?,
    @SerializedName("ngay_ket_thuc") val ngay_ket_thuc: String
)

data class MaGiamGiaNguoiDungResponse(
    val message: String,
    @SerializedName("danh_sach_ma_giam_gia")
    val danh_sach_ma_giam_gia: List<MaGiamGia>,
    @SerializedName("so_luong")
    val soLuong: Int
)


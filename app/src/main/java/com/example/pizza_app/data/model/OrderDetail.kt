package com.example.pizza_app.data.model

import com.google.gson.annotations.SerializedName

data class OrderDetail(
    val don_hang: DonHang,
    val mat_hang: List<MatHang>
) {
    data class DonHang(
        val ma_don_hang: Int,
        val tong_tien_san_pham: Double,
        val phi_giao_hang: Double,
        val giam_gia_ma_giam_gia: Double,
        val tong_tien_cuoi_cung: Double,
        val trang_thai: String,
        val phuong_thuc_thanh_toan: String?,
        val trang_thai_thanh_toan: String?,
        val ghi_chu: String?,
        val thoi_gian_giao_du_kien: String?,
        val thong_tin_giao_hang: ThongTinGiaoHang,
        val nguoi_dat: NguoiDat,
        val ma_giam_gia: String?
    )

    data class ThongTinGiaoHang(
        val ten_nguoi_nhan: String,
        val so_dien_thoai: String,
        val dia_chi: String,
        val ghi_chu: String?
    )

    data class NguoiDat(
        val ho_ten: String?,
        val email: String?
    )
}


data class TuyChon(
    @SerializedName("ma_chi_tiet")
    val ma_chi_tiet_tuy_chon_don_hang: Int,

    @SerializedName("ma_mat_hang_don_hang")
    val ma_mat_hang_don_hang: Int,

    @SerializedName("ma_gia_tri")
    val ma_tuy_chon: Int,

    @SerializedName("ten_loai_tuy_chon")
    val ten_tuy_chon: String,

    @SerializedName("ten_gia_tri")
    val gia_tri_tuy_chon: String,

    @SerializedName("gia_them")
    val gia_them: Double
)


data class MatHang(
    val ma_mat_hang_don_hang: Int,
    val ma_don_hang: Int,
    val loai_mat_hang: String, // "san_pham" hoặc "combo"
    val ma_san_pham: Int?,
    val ma_combo: Int?,
    val so_luong: Int,
    val don_gia_co_ban: Double,
    val thanh_tien: Double,
    val ten_san_pham: String?,
    val hinh_anh: String?,
    val ten_combo: String?,
    val hinh_anh_combo: String?,
    val tuy_chon: List<TuyChon>?,
    val chi_tiet_combo: List<ChiTietCombo>?
)

data class ChiTietCombo(
    val ma_chi_tiet: Int,
    val ma_mat_hang_don_hang: Int,
    val ma_san_pham: Int,
    val ten_san_pham: String,
    val so_luong: Int,
    val tuy_chon: List<TuyChonCombo>?
)

data class TuyChonCombo(
    val ma_tuy_chon_combo_don_hang: Int,
    val ma_chi_tiet_combo_don_hang: Int,
    val ma_tuy_chon: Int,
    val ten_tuy_chon: String,
    val gia_tri_tuy_chon: String
)
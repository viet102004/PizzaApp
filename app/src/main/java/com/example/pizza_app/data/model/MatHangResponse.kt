package com.example.pizza_app.data.model


data class MatHangResponse(
    val ma_mat_hang_gio_hang: Int,
    val ma_san_pham: Int?,
    val ma_combo: Int?,
    val loai_mat_hang: String,
    val so_luong: Int,
    val gia_san_pham: Double,
    val ghi_chu: String?,
    val ten_san_pham: String?,
    val hinh_anh: String?,
    val ten_combo: String?,
    val hinh_anh_combo: String?,
    val tuy_chon: List<TuyChonResponse>?,
    val chi_tiet_combo: List<ChiTietComboResponse>?,
    val thanh_tien: Double
) {
    fun toCartItem(): CartItem {
        val selectedOptions = (tuy_chon ?: emptyList()).map {
            TuyChonHienThi(
                tenGiaTri = it.ten_gia_tri,
                tenLoai = it.ten_loai,
                giaThem = it.gia_them
            )
        }

        val extraOptions = (tuy_chon ?: emptyList()).map {
            TuyChonRequest(
                ma_loai_tuy_chon = it.ma_loai_tuy_chon,
                ma_gia_tri = it.ma_gia_tri,
                gia_them = it.gia_them
            )
        }

        val id = "${loai_mat_hang}_${ma_mat_hang_gio_hang}"
        val name = ten_san_pham ?: ten_combo ?: "Không tên"
        val image = hinh_anh ?: hinh_anh_combo ?: ""

        return CartItem(
            id = id,
            productId = ma_san_pham ?: 0,
            product = Product(
                ma_san_pham = (ma_san_pham ?: 0).toLong(),
                ten_san_pham = name,
                hinh_anh = hinh_anh ?: hinh_anh_combo,
                gia_co_ban = gia_san_pham,
                mo_ta = null,
                moi = 0,
                ma_danh_muc = 0
            ),
            quantity = so_luong,
            imageUrl = image,
            basePrice = gia_san_pham,
            selectedOptions = selectedOptions,
            extraOptions = extraOptions, // <-- thêm dòng này
            totalPrice = thanh_tien,
            maMatHangGioHang = this.ma_mat_hang_gio_hang,
            note = this.ghi_chu
        )
    }
}


data class TuyChonResponse(
    val ma_loai_tuy_chon: Int,
    val ma_gia_tri: Int,
    val gia_them: Double,
    val ten_gia_tri: String,
    val ten_loai: String
)

data class ChiTietComboResponse(
    val ma_chi_tiet: Int,
    val ma_chi_tiet_combo: Int,
    val ma_san_pham: Int,
    val ten_san_pham: String,
    val so_luong_combo: Int,
    val gia_san_pham: Double,
    val tuy_chon: List<TuyChonResponse>
)

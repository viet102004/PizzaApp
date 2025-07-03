package com.example.pizza_app.ui.vouchers

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizza_app.data.model.MaGiamGia
import com.example.pizza_app.ui.cart.CartViewModel
import com.example.pizza_app.ui.cart.VoucherDialogItem

@Composable
fun VoucherDialogContent(
    vouchers: List<MaGiamGia>,
    selectedCode: String?,
    totalAmount: Double,
    onVoucherSelected: (MaGiamGia?) -> Unit
) {
    LazyColumn {
        items(vouchers) { voucher ->
            val isSelected = selectedCode == voucher.ma_code
            val minOrder = voucher.gia_tri_don_hang_toi_thieu?.toDouble() ?: 0.0
            val isEligible = totalAmount >= minOrder

            val discountText = when (voucher.loai_giam_gia.lowercase()) {
                "phan_tram" -> "-${voucher.gia_tri_giam.toInt()}%"
                "tien_mat" -> "-${voucher.gia_tri_giam.toInt()}đ"
                else -> ""
            }

            val savings = if (isEligible) {
                when (voucher.loai_giam_gia.lowercase()) {
                    "phan_tram" -> (totalAmount * (voucher.gia_tri_giam.toDouble() / 100)).toInt()
                    "tien_mat" -> voucher.gia_tri_giam.toInt()
                    else -> null
                }
            } else null

            VoucherDialogItem(
                title = voucher.ma_code,
                description = "Hết hạn: ${voucher.ngay_ket_thuc}",
                discount = discountText,
                savings = savings,
                isSelected = isSelected,
                isEligible = isEligible,
                minOrder = "Đơn tối thiểu: ${minOrder.toInt()}đ",
                onClick = {
                    onVoucherSelected(if (isSelected) null else voucher)
                }
            )
        }
    }
}


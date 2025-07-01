package com.example.pizza_app.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizza_app.data.model.VoucherItem

@Composable
fun VoucherSelectionDialog(
    voucherList: List<VoucherItem>,
    selectedVoucher: VoucherItem?,
    originalTotal: Int,
    onVoucherSelected: (VoucherItem?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chọn voucher",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Đóng",
                        tint = Color.Gray
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header thông tin
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = Color(0xFFFF6B35),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Đơn hàng: ${formatCurrency(originalTotal.toDouble())}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Không chọn voucher
                VoucherDialogItem(
                    title = "Không sử dụng voucher",
                    description = null,
                    discount = null,
                    savings = null,
                    isSelected = selectedVoucher == null,
                    isEligible = true,
                    onClick = {
                        onVoucherSelected(null)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Danh sách voucher
                voucherList.forEach { voucher ->
                    val isEligible = originalTotal >= voucher.minOrderAmount
                    VoucherDialogItem(
                        title = voucher.title,
                        description = voucher.description,
                        discount = voucher.discount,
                        savings = if (isEligible) voucher.discountValue else null,
                        isSelected = selectedVoucher?.id == voucher.id,
                        isEligible = isEligible,
                        minOrder = voucher.minOrder,
                        onClick = {
                            if (isEligible) {
                                onVoucherSelected(voucher)
                            }
                        }
                    )

                    if (voucher != voucherList.last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button nhập mã
                OutlinedButton(
                    onClick = {
                        // TODO: Navigate to voucher input screen
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF6B35)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFFF6B35)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nhập mã voucher",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
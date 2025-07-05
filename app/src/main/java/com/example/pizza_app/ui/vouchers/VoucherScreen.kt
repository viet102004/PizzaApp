@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.pizza_app.ui.vouchers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.data.model.MaGiamGia

@Composable
fun VoucherScreen(
    navController: NavController,
    viewModel: VoucherViewModel = viewModel()
) {
    val vouchers by viewModel.voucherList.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchVouchers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách mã giảm giá") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (vouchers.isEmpty()) {
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vouchers) { voucher ->
                        VoucherItem(
                            voucher = voucher,
                            onUseClick = {
                                navController.navigate("home")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoucherItem(
    voucher: MaGiamGia,
    onUseClick: () -> Unit
) {
    val isUsed = voucher.so_lan_su_dung_toi_da != null &&
            voucher.da_su_dung >= voucher.so_lan_su_dung_toi_da

    val discountText = when (voucher.loai_giam_gia) {
        "phan_tram" -> "-${voucher.gia_tri_giam.stripTrailingZeros().toPlainString()}%"
        "tien_mat" -> "-${voucher.gia_tri_giam.stripTrailingZeros().toPlainString()}₫"
        else -> "-${voucher.gia_tri_giam.stripTrailingZeros().toPlainString()}"
    }

    val minOrderText = "Đơn tối thiểu: ${voucher.gia_tri_don_hang_toi_thieu?.toInt() ?: "0"}₫"
    val expiryText = "HSD: ${voucher.ngay_ket_thuc}"

    val usageText = if (voucher.so_lan_su_dung_toi_da != null)
        "Đã dùng ${voucher.da_su_dung}/${voucher.so_lan_su_dung_toi_da}"
    else "Có thể dùng nhiều lần"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Discount badge
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isUsed) Color.Gray.copy(alpha = 0.3f)
                        else Color(0xFFFF6B35)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = discountText,
                    color = if (isUsed) Color.Gray else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = voucher.ma_code,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUsed) Color.Gray else Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = usageText, fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(2.dp))
                Text(text = minOrderText, fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(2.dp))
                Text(text = expiryText, fontSize = 12.sp, color = Color.Gray)
            }

            Button(
                onClick = onUseClick,
                enabled = !isUsed,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isUsed) Color.Gray.copy(alpha = 0.3f)
                    else Color(0xFFFFD700),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    text = if (isUsed) "Đã dùng" else "Dùng ngay",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

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
                title = {
                    Text(
                        "Danh sách mã giảm giá",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1A1A)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color(0xFF666666)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFAFAFA)
                )
            )
        },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (vouchers.isEmpty()) {
                // Empty state với style tương tự AlertDialog
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Chưa có mã giảm giá",
                        fontSize = 16.sp,
                        color = Color(0xFF666666),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Mã giảm giá sẽ xuất hiện tại đây",
                        fontSize = 12.sp,
                        color = Color(0xFF999999),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
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

    val isValid = !isUsed

    val discountText = when (voucher.loai_giam_gia) {
        "phan_tram" -> "${voucher.gia_tri_giam.stripTrailingZeros().toPlainString()}%"
        "tien_mat" -> "${voucher.gia_tri_giam.stripTrailingZeros().toPlainString()}đ"
        else -> "${voucher.gia_tri_giam.stripTrailingZeros().toPlainString()}"
    }

    val usageText = if (voucher.so_lan_su_dung_toi_da != null)
        "Đã dùng ${voucher.da_su_dung}/${voucher.so_lan_su_dung_toi_da}"
    else "Có thể dùng nhiều lần"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isValid) {
                    onUseClick()
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isUsed -> Color(0xFFF5F5F5)
                else -> Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side - Voucher icon and info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Voucher icon with gradient background
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = if (isValid) {
                                    listOf(Color(0xFFFF6B35), Color(0xFFFFB700))
                                } else {
                                    listOf(Color(0xFFCCCCCC), Color(0xFFE0E0E0))
                                }
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Voucher details
                Column {
                    Text(
                        text = voucher.ma_code,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isValid) Color(0xFF1A1A1A) else Color(0xFF999999)
                    )

                    Text(
                        text = "Giảm $discountText",
                        fontSize = 14.sp,
                        color = if (isValid) Color(0xFF4CAF50) else Color(0xFF999999),
                        fontWeight = FontWeight.Medium
                    )

                    voucher.gia_tri_don_hang_toi_thieu?.let { minValue ->
                        Text(
                            text = "Đơn từ ${minValue.toInt()}đ",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                    }

                    Text(
                        text = "HSD: ${voucher.ngay_ket_thuc}",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )

                    if (isUsed) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = "Đã hết lượt sử dụng",
                                fontSize = 11.sp,
                                color = Color(0xFFE53935),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Right side - Use button and status
            Column(
                horizontalAlignment = Alignment.End
            ) {
                if (isValid) {
                    Button(
                        onClick = onUseClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Dùng ngay",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE0E0E0)
                    ) {
                        Text(
                            text = "Đã dùng",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF999999),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = usageText,
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
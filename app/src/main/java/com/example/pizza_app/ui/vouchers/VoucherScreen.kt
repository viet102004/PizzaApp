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
import androidx.navigation.NavController

data class Voucher(
    val id: String,
    val title: String,
    val description: String,
    val discount: String,
    val minOrder: String,
    val expiry: String,
    val isUsed: Boolean = false
)

@Composable
fun VoucherScreen(navController: NavController) {
    var showVoucherDialog by remember { mutableStateOf(false) }
    var selectedVoucher by remember { mutableStateOf<Voucher?>(null) }

    // Danh sách voucher mẫu
    val vouchers = listOf(
        Voucher(
            id = "SAVE13",
            title = "Giảm 13%",
            description = "Giảm 13% cho toàn bộ đơn hàng",
            discount = "13%",
            minOrder = "Đơn tối thiểu 100.000đ",
            expiry = "HSD: 31/12/2024"
        ),
        Voucher(
            id = "SAVE20",
            title = "Giảm 20%",
            description = "Giảm 20% cho toàn bộ đơn hàng",
            discount = "20%",
            minOrder = "Đơn tối thiểu 200.000đ",
            expiry = "HSD: 30/11/2024"
        ),
        Voucher(
            id = "FLAT15K",
            title = "Giảm 15.000đ",
            description = "Giảm ngay 15.000đ",
            discount = "15K",
            minOrder = "Đơn tối thiểu 80.000đ",
            expiry = "HSD: 25/12/2024"
        ),
        Voucher(
            id = "SAVE12",
            title = "Giảm 12%",
            description = "Giảm 12% cho toàn bộ đơn hàng",
            discount = "12%",
            minOrder = "Đơn tối thiểu 120.000đ",
            expiry = "HSD: 28/12/2024"
        ),
        Voucher(
            id = "SAVE10",
            title = "Giảm 10%",
            description = "Giảm 10% cho toàn bộ đơn hàng",
            discount = "10%",
            minOrder = "Đơn tối thiểu 50.000đ",
            expiry = "HSD: 31/01/2025"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F8F8),
                        Color(0xFFF5F5F5)
                    )
                )
            )
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Mã Giảm Giá",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { /* TODO: Nhập mã */ },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = Color(0xFFFF6B35),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nhập mã",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { /* TODO: Tìm thêm */ },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Percent,
                        contentDescription = null,
                        tint = Color(0xFFFF6B35),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tìm thêm",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Voucher của bạn",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Danh sách voucher
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(vouchers) { voucher ->
                VoucherItem(
                    voucher = voucher,
                    onUseClick = {
                        selectedVoucher = voucher
                        showVoucherDialog = true
                    }
                )
            }

            // Thêm padding bottom để tránh bị che
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Dialog xác nhận sử dụng voucher
    if (showVoucherDialog && selectedVoucher != null) {
        AlertDialog(
            onDismissRequest = {
                showVoucherDialog = false
                selectedVoucher = null
            },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Sử dụng voucher",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Column {
                    // Voucher info card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFF6B35).copy(alpha = 0.1f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFF6B35)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectedVoucher!!.discount,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = selectedVoucher!!.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = selectedVoucher!!.description,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = selectedVoucher!!.minOrder,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Bạn có muốn áp dụng voucher này cho đơn hàng không?",
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // TODO: Apply voucher logic
                        showVoucherDialog = false
                        selectedVoucher = null
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B35)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Sử dụng",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showVoucherDialog = false
                        selectedVoucher = null
                    }
                ) {
                    Text(
                        text = "Hủy",
                        color = Color.Gray
                    )
                }
            }
        )
    }
}

@Composable
fun VoucherItem(
    voucher: Voucher,
    onUseClick: () -> Unit
) {
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
                        if (voucher.isUsed) Color.Gray.copy(alpha = 0.3f)
                        else Color(0xFFFF6B35)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = voucher.discount,
                    color = if (voucher.isUsed) Color.Gray else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Voucher info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = voucher.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (voucher.isUsed) Color.Gray else Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = voucher.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = voucher.minOrder,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = voucher.expiry,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Use button
            Button(
                onClick = onUseClick,
                enabled = !voucher.isUsed,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (voucher.isUsed) Color.Gray.copy(alpha = 0.3f)
                    else Color(0xFFFFD700),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(
                    text = if (voucher.isUsed) "Đã dùng" else "Dùng ngay",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
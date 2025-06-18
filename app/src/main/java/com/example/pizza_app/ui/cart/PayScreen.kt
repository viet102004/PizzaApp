package com.example.pizza_app.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

data class VoucherItem(
    val id: String,
    val title: String,
    val description: String,
    val discount: String,
    val minOrder: String,
    val expiry: String,
    val discountValue: Int,
    val isEligible: Boolean = true,
    val isUsed: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayScreen(navController: NavController) {
    var noteText by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("cash") }
    var selectedVoucher by remember { mutableStateOf<VoucherItem?>(null) }
    var showVoucherDialog by remember { mutableStateOf(false) }

    // Danh sách voucher với logic thực tế
    val voucherList = listOf(
        VoucherItem(
            id = "SAVE13",
            title = "Giảm 13%",
            description = "Giảm 13% cho toàn bộ đơn hàng",
            discount = "13%",
            minOrder = "Đơn tối thiểu 100.000đ",
            expiry = "HSD: 31/12/2024",
            discountValue = 8580, // 13% của 66000
            isEligible = false // Đơn hàng chưa đủ điều kiện
        ),
        VoucherItem(
            id = "FLAT15K",
            title = "Giảm 15.000đ",
            description = "Giảm ngay 15.000đ cho đơn hàng",
            discount = "15K",
            minOrder = "Đơn tối thiểu 50.000đ",
            expiry = "HSD: 25/12/2024",
            discountValue = 15000,
            isEligible = true
        ),
        VoucherItem(
            id = "FLAT10K",
            title = "Giảm 10.000đ",
            description = "Giảm ngay 10.000đ cho đơn hàng",
            discount = "10K",
            minOrder = "Đơn tối thiểu 30.000đ",
            expiry = "HSD: 28/12/2024",
            discountValue = 10000,
            isEligible = true
        ),
        VoucherItem(
            id = "SAVE5",
            title = "Giảm 5%",
            description = "Giảm 5% cho toàn bộ đơn hàng",
            discount = "5%",
            minOrder = "Đơn tối thiểu 50.000đ",
            expiry = "HSD: 31/01/2025",
            discountValue = 3300,
            isEligible = true
        ),
        VoucherItem(
            id = "SAVE20",
            title = "Giảm 20%",
            description = "Giảm 20% cho toàn bộ đơn hàng",
            discount = "20%",
            minOrder = "Đơn tối thiểu 200.000đ",
            expiry = "HSD: 30/11/2024",
            discountValue = 0,
            isEligible = false
        )
    )

    val originalTotal = 66000
    val discountAmount = selectedVoucher?.discountValue ?: 0
    val finalTotal = originalTotal - discountAmount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Thanh toán",
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

        Divider(color = Color.LightGray, thickness = 0.5.dp)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Địa chỉ giao hàng
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
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "100 Chiến Thắng",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "P.9, Q.Phú Nhuận, Hồ Chí Minh",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            // Thông tin món ăn
            Text(
                text = "Trà Sữa Maycha - 5A Thích Quảng Đức",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            // Item món ăn
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🧋", fontSize = 24.sp)
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "x1",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "45.000đ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Lục Trà Măng Cụt Chanh Dây",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "Chọn size: Size M",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "Chọn mức ngọt: Ngọt bình thường",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "Chọn mức đá: Không đá",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "Topping: Kem trứng khế",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "Sửa",
                            fontSize = 12.sp,
                            color = Color.Blue,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Chi tiết thanh toán
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Chi tiết thanh toán",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tạm tính (1 phần)",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "45.000đ",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Phí áp dụng",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = " ⓘ",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "21.000đ",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    // Hiển thị giảm giá nếu có
                    if (selectedVoucher != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Giảm giá (${selectedVoucher!!.title})",
                                fontSize = 14.sp,
                                color = Color.Red
                            )
                            Text(
                                text = "-${String.format("%,d", discountAmount)}đ",
                                fontSize = 14.sp,
                                color = Color.Red
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color.LightGray,
                        thickness = 0.5.dp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tổng cộng",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${String.format("%,d", finalTotal)}đ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Voucher - UI đã cải thiện
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showVoucherDialog = true },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFFFF6B35),
                        modifier = Modifier.size(20.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Voucher giảm giá",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = selectedVoucher?.title ?: "Chọn hoặc nhập mã voucher",
                            fontSize = 14.sp,
                            color = if (selectedVoucher != null) Color(0xFFFF6B35) else Color.Gray
                        )
                        if (selectedVoucher != null) {
                            Text(
                                text = "Tiết kiệm: ${String.format("%,d", discountAmount)}đ",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            // Phương thức thanh toán
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Phương thức thanh toán",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Tiền mặt
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = "cash" }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💰",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Tiền mặt",
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = selectedPaymentMethod == "cash",
                            onClick = { selectedPaymentMethod = "cash" }
                        )
                    }

                    // Thẻ tín dụng/ghi nợ
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = "card" }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Thẻ tín dụng/ghi nợ",
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = selectedPaymentMethod == "card",
                            onClick = { selectedPaymentMethod = "card" }
                        )
                    }

                    // Ví điện tử
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = "ewallet" }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Ví điện tử",
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = selectedPaymentMethod == "ewallet",
                            onClick = { selectedPaymentMethod = "ewallet" }
                        )
                    }
                }
            }
        }

        // Bottom section - Tổng tiền và nút đặt món
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tổng số tiền",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        if (selectedVoucher != null) {
                            Text(
                                text = "Đã tiết kiệm: ${String.format("%,d", discountAmount)}đ",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Text(
                        text = "${String.format("%,d", finalTotal)}đ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle order */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B35)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Đặt món",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Dialog chọn voucher - UI đã cải thiện đáng kể
    if (showVoucherDialog) {
        AlertDialog(
            onDismissRequest = { showVoucherDialog = false },
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
                        onClick = { showVoucherDialog = false },
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
                                text = "Đơn hàng: ${String.format("%,d", originalTotal)}đ",
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
                            selectedVoucher = null
                            showVoucherDialog = false
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Danh sách voucher
                    voucherList.forEach { voucher ->
                        VoucherDialogItem(
                            title = voucher.title,
                            description = voucher.description,
                            discount = voucher.discount,
                            savings = if (voucher.isEligible) voucher.discountValue else null,
                            isSelected = selectedVoucher?.id == voucher.id,
                            isEligible = voucher.isEligible,
                            minOrder = voucher.minOrder,
                            onClick = {
                                if (voucher.isEligible) {
                                    selectedVoucher = voucher
                                    showVoucherDialog = false
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
                            showVoucherDialog = false
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
}

@Composable
fun VoucherDialogItem(
    title: String,
    description: String? = null,
    discount: String? = null,
    savings: Int? = null,
    isSelected: Boolean,
    isEligible: Boolean,
    minOrder: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEligible) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> Color(0xFFFF6B35).copy(alpha = 0.1f)
                !isEligible -> Color.Gray.copy(alpha = 0.1f)
                else -> Color.White
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = when {
                isSelected -> Color(0xFFFF6B35)
                !isEligible -> Color.Gray.copy(alpha = 0.3f)
                else -> Color.Gray.copy(alpha = 0.2f)
            }
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio button hoặc discount badge
            if (discount != null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isEligible) Color(0xFFFF6B35)
                            else Color.Gray.copy(alpha = 0.5f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = discount,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    enabled = isEligible,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFFF6B35)
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isEligible) Color.Black else Color.Gray
                )

                description?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                minOrder?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = if (isEligible) Color.Gray else Color.Red,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                savings?.let {
                    Text(
                        text = "Tiết kiệm: ${String.format("%,d", it)}đ",
                        fontSize = 12.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFFFF6B35),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
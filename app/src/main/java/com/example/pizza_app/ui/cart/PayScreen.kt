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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PayScreen(navController: NavController) {
    var noteText by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("cash") }
    var selectedPromo by remember { mutableStateOf<String?>(null) }
    var showPromoDialog by remember { mutableStateOf(false) }

    // Danh sách khuyến mãi mẫu
    val promoList = listOf(
        "Giảm 10% - Tối đa 20.000đ" to 6600,
        "Giảm 15.000đ cho đơn từ 50.000đ" to 15000,
        "Giảm 5% - Không giới hạn" to 3300
    )

    val originalTotal = 66000
    val discountAmount = selectedPromo?.let { promo ->
        promoList.find { it.first == promo }?.second ?: 0
    } ?: 0
    val finalTotal = originalTotal - discountAmount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController?.popBackStack() // Quay lại màn hình trước
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại"
                )
            }
            Text(
                text = "Thanh toán",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

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
                    // Placeholder cho hình ảnh món ăn
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
                            // Info icon placeholder
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
                    if (selectedPromo != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Giảm giá",
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

            // Khuyến mãi
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPromoDialog = true },
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
                            text = "Khuyến mãi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = selectedPromo ?: "Chọn khuyến mãi",
                            fontSize = 14.sp,
                            color = if (selectedPromo != null) Color(0xFFFF6B35) else Color.Gray
                        )
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

        // Bottom section - Tổng tiền và nút đặt món (cố định ở dưới)
        Card(
            modifier = Modifier
                .fillMaxWidth(), // Thêm padding để tránh bottom navigation
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
                    Text(
                        text = "Tổng số tiền",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${String.format("%,d", finalTotal)}đ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle order */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Đặt món",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Dialog chọn khuyến mãi
    if (showPromoDialog) {
        AlertDialog(
            onDismissRequest = { showPromoDialog = false },
            title = {
                Text(
                    text = "Chọn khuyến mãi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    // Không chọn khuyến mãi
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedPromo = null
                                showPromoDialog = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPromo == null,
                            onClick = {
                                selectedPromo = null
                                showPromoDialog = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Không sử dụng khuyến mãi",
                            fontSize = 14.sp
                        )
                    }

                    Divider(color = Color.LightGray, thickness = 0.5.dp)

                    // Danh sách khuyến mãi
                    promoList.forEach { (promo, discount) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPromo = promo
                                    showPromoDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPromo == promo,
                                onClick = {
                                    selectedPromo = promo
                                    showPromoDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = promo,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Tiết kiệm: ${String.format("%,d", discount)}đ",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showPromoDialog = false }
                ) {
                    Text("Đóng")
                }
            }
        )
    }
}
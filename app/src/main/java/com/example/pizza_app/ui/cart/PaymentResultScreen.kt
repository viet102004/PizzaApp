package com.example.pizza_app.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun PaymentResultScreen(
    navController: NavController,
    payViewModel: PayViewModel = viewModel()
) {
    val paymentResult by payViewModel.paymentCallbackResult.collectAsState()

    LaunchedEffect(Unit) {
        // Auto reset state sau 5 giây
        kotlinx.coroutines.delay(5000)
        payViewModel.resetState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        paymentResult?.let { result ->
            // Icon
            Icon(
                imageVector = if (result.isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                contentDescription = null,
                tint = if (result.isSuccess) Color.Green else Color.Red,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tiêu đề
            Text(
                text = if (result.isSuccess) "Thanh toán thành công!" else "Thanh toán thất bại!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (result.isSuccess) Color.Green else Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mã đơn hàng
            Text(
                text = "Mã đơn hàng: ${result.orderId}",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Thông báo
            Text(
                text = result.message,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (result.isSuccess) {
                    Button(
                        onClick = {
                            navController.navigate("order_history")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Xem đơn hàng")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = {
                            navController.navigate("home")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Về trang chủ")
                    }
                } else {
                    Button(
                        onClick = {
                            navController.navigate("cart")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Thử lại")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = {
                            navController.navigate("home")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Về trang chủ")
                    }
                }
            }
        } ?: run {
            // Loading hoặc no data
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Đang xử lý kết quả thanh toán...")
        }
    }
}
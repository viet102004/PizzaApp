package com.example.pizza_app.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizza_app.data.model.OrderDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDialog(
    orderDetail: OrderDetail,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onSubmitReview: (productId: Long, rating: Int, comment: String) -> Unit
) {
    var selectedProductIndex by remember { mutableStateOf(0) }
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }

    // Lấy danh sách sản phẩm có thể đánh giá (loại san_pham)
    val reviewableProducts = orderDetail.mat_hang.filter { it.loai_mat_hang == "san_pham" }

    if (reviewableProducts.isEmpty()) {
        // Nếu không có sản phẩm nào có thể đánh giá
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Thông báo") },
            text = { Text("Không có sản phẩm nào trong đơn hàng này có thể đánh giá.") },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Đóng")
                }
            }
        )
        return
    }

    AlertDialog(
        onDismissRequest = if (isSubmitting) { {} } else onDismiss,
        title = {
            Text(
                text = "Đánh giá đơn hàng",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Chọn sản phẩm đánh giá
                if (reviewableProducts.size > 1) {
                    Text(
                        text = "Chọn sản phẩm để đánh giá:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = HarmoniousColors.OnSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    reviewableProducts.forEachIndexed { index, product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedProductIndex == index,
                                onClick = { selectedProductIndex = index },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = HarmoniousColors.Primary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = product.ten_san_pham ?: "Sản phẩm",
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Rating
                Text(
                    text = "Đánh giá của bạn:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HarmoniousColors.OnSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { rating = index + 1 },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Sao ${index + 1}",
                                tint = if (index < rating) HarmoniousColors.StarYellow else HarmoniousColors.OnSurfaceLight,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Comment
                Text(
                    text = "Nhận xét (tùy chọn):",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HarmoniousColors.OnSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = { Text("Chia sẻ trải nghiệm của bạn...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HarmoniousColors.Primary,
                        cursorColor = HarmoniousColors.Primary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val selectedProduct = reviewableProducts[selectedProductIndex]
                    // Giả sử có trường ma_san_pham trong MatHang
                    onSubmitReview(selectedProduct.ma_san_pham!!.toLong() ?: 0L, rating, comment)
                },
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HarmoniousColors.Primary
                )
            ) {
                if (isSubmitting) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Đang gửi...")
                    }
                } else {
                    Text("Gửi đánh giá")
                }
            }
        },
        dismissButton = {
            if (!isSubmitting) {
                TextButton(onClick = onDismiss) {
                    Text("Hủy")
                }
            }
        }
    )
}

@Composable
fun OrderDetailContent(
    orderDetail: OrderDetail,
    onCancelOrder: () -> Unit,
    isCancelling: Boolean,
    onReviewOrder: () -> Unit, // thêm callback này
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }
        item { CompactOrderHeader(orderDetail.don_hang) }

        // Hiển thị nút hủy nếu "chờ xác nhận"
        if (orderDetail.don_hang.trang_thai == "cho_xac_nhan") {
            item {
                CancelOrderButton(
                    onCancelOrder = onCancelOrder,
                    isCancelling = isCancelling
                )
            }
        }

        // ✅ Hiển thị nút đánh giá nếu đơn hàng đã hoàn thành
        if (orderDetail.don_hang.trang_thai == "hoan_thanh") {
            item {
                Button(
                    onClick = onReviewOrder,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HarmoniousColors.Success
                    )
                ) {
                    Icon(Icons.Default.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Đánh giá đơn hàng")
                }
            }
        }

        item { MatHangListCard(orderDetail.mat_hang) }
        item { CompactTotalCard(orderDetail.don_hang) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

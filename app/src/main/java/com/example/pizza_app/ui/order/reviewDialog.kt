package com.example.pizza_app.ui.order

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.OrderDetail

@Composable
fun ReviewOrderButton(onReviewOrder: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = HarmoniousColors.Surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Button(
                onClick = onReviewOrder,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HarmoniousColors.Primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Đánh giá đơn hàng",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Chia sẻ trải nghiệm của bạn về các sản phẩm trong đơn hàng này",
                fontSize = 12.sp,
                color = HarmoniousColors.OnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ReviewDialog(
    orderDetail: OrderDetail,
    reviewedProductIds: Set<Long> = emptySet(), // Thêm parameter này
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onSubmitReview: (Long, Int, String, String?) -> Unit
) {
    var selectedProductIndex by remember { mutableStateOf(0) }
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Lọc ra những sản phẩm chưa được đánh giá
    val reviewableProducts = orderDetail.mat_hang.filter {
        it.loai_mat_hang == "san_pham" &&
                it.ma_san_pham != null &&
                !reviewedProductIds.contains(it.ma_san_pham?.toLong())
    }

    if (reviewableProducts.isEmpty()) {
        // Nếu không có sản phẩm nào có thể đánh giá
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Thông báo") },
            text = {
                Text(
                    if (orderDetail.mat_hang.any { it.loai_mat_hang == "san_pham" && it.ma_san_pham != null }) {
                        "Tất cả sản phẩm trong đơn hàng này đã được đánh giá."
                    } else {
                        "Không có sản phẩm nào có thể đánh giá trong đơn hàng này."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Đóng")
                }
            }
        )
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Đánh giá sản phẩm",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Chọn sản phẩm để đánh giá
                Text(
                    text = "Chọn sản phẩm:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HarmoniousColors.OnSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Product selection (simplified - you might want to use a dropdown)
                LazyColumn(
                    modifier = Modifier.height(100.dp)
                ) {
                    itemsIndexed(reviewableProducts) { index, product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedProductIndex == index,
                                onClick = { selectedProductIndex = index }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = product.ten_san_pham ?: "Sản phẩm",
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rating
                Text(
                    text = "Đánh giá:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HarmoniousColors.OnSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { rating = index + 1 },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
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
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HarmoniousColors.Primary,
                        cursorColor = HarmoniousColors.Primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Image selection section
                Text(
                    text = "Hình ảnh (tùy chọn):",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HarmoniousColors.OnSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image preview or placeholder
                    if (selectedImageUri != null) {
                        Card(
                            modifier = Modifier.size(80.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Hình ảnh đánh giá",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        Card(
                            modifier = Modifier.size(80.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = HarmoniousColors.Background
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Image,
                                    contentDescription = null,
                                    tint = HarmoniousColors.OnSurfaceLight,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    // Action buttons
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = HarmoniousColors.Primary
                            ),
                            border = BorderStroke(1.dp, HarmoniousColors.Primary)
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (selectedImageUri != null) "Đổi ảnh" else "Chọn ảnh",
                                fontSize = 12.sp
                            )
                        }

                        if (selectedImageUri != null) {
                            TextButton(
                                onClick = { selectedImageUri = null },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = HarmoniousColors.Error
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Xóa ảnh",
                                    fontSize = 12.sp,
                                    color = HarmoniousColors.Error
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val selectedProduct = reviewableProducts[selectedProductIndex]
                    selectedProduct.ma_san_pham?.let { productId ->
                        // Convert URI to string if image is selected
                        val imageString = selectedImageUri?.toString()
                        onSubmitReview(productId.toLong(), rating, comment, imageString)
                    }
                },
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HarmoniousColors.Primary
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Gửi đánh giá")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isSubmitting
            ) {
                Text("Hủy")
            }
        }
    )
}
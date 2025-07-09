@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.CartItem
import com.example.pizza_app.data.source.getFullImageUrl

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
   // onEditClick: () -> Unit = {},
    showControls: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Hình ảnh + Thông tin cơ bản + Giá + Nút xóa
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Hình ảnh sản phẩm
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF3E0)),
                    contentAlignment = Alignment.Center
                ) {
                    DisplayImage(item)
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Thông tin sản phẩm
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Tên sản phẩm
                    Text(
                        text = item.product.ten_san_pham,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Giá tiền
                    Text(
                        text = formatCurrency(item.totalPrice),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35)
                    )
                }

                // Nút xóa (góc trên phải)
                if (showControls) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFFE4E6), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Xóa",
                            tint = Color(0xFFFF4757),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Tùy chọn sản phẩm
            if (item.selectedOptions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
              //          .clickable{onEditClick()},
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))

                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        item.selectedOptions.forEach { option ->
                            val annotatedText = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                    append(option.tenLoai)
                                }
                                append(": ${option.tenGiaTri}")

                                if (option.giaThem > 0) {
                                    append(" ")
                                    withStyle(style = SpanStyle(
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.Medium
                                    )) {
                                        append("(+${formatCurrency(option.giaThem)})")
                                    }
                                }
                            }

                            Text(
                                text = annotatedText,
                                fontSize = 14.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Footer: Controls hoặc thông tin số lượng
            Spacer(modifier = Modifier.height(16.dp))

            if (showControls) {
                // Controls tăng giảm số lượng
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Số lượng:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Nút giảm
                        IconButton(
                            onClick = {
                                if (item.quantity > 1) onQuantityChange(item.quantity - 1)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    if (item.quantity > 1) Color(0xFFF5F5F5) else Color(0xFFE0E0E0),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Giảm",
                                tint = if (item.quantity > 1) Color.Black else Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Hiển thị số lượng
                        Surface(
                            modifier = Modifier.width(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF8F9FA)
                        ) {
                            Text(
                                text = item.quantity.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // Nút tăng
                        IconButton(
                            onClick = { onQuantityChange(item.quantity + 1) },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFFF6B35), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tăng",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            } else {
                // Chỉ hiển thị thông tin số lượng
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Số lượng:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = item.quantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35)
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayImage(item: CartItem) {
    val imgStr = item.imageUrl
    val isUrl = imgStr.startsWith("http") || imgStr.contains("/")

    if (isUrl) {
        AsyncImage(
            model = getFullImageUrl(imgStr),
            contentDescription = item.product.ten_san_pham,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        val resourceId = imgStr.toIntOrNull()

        if (resourceId != null && resourceId != 0) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = item.product.ten_san_pham,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            DefaultPizzaIcon(item.product.ten_san_pham)
        }
    }
}

@Composable
fun DefaultPizzaIcon(name: String) {
    Icon(
        imageVector = Icons.Default.LocalPizza,
        contentDescription = name,
        tint = Color(0xFFFF6B35),
        modifier = Modifier.size(40.dp)
    )
}
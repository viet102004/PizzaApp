package com.example.pizza_app.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.CartItem
import com.example.pizza_app.data.source.getFullImageUrl

@Composable
fun PaymentItemCard(item: CartItem) {
    val selectedOptionsDisplay = item.selectedOptions.map {
        "${it.tenLoai}: ${it.tenGiaTri}" + if (it.giaThem > 0) " (+${formatCurrency(it.giaThem)})" else ""
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hình ảnh nhỏ gọn
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFFF3E0)),
            contentAlignment = Alignment.Center
        ) {
            DisplayPaymentImage(item)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Thông tin sản phẩm
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.ten_san_pham,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                maxLines = 1
            )

            if (selectedOptionsDisplay.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                selectedOptionsDisplay.forEach { optionDisplay ->
                    Text(
                        text = optionDisplay,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "x${item.quantity}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Giá tiền
        Text(
            text = formatCurrency(item.totalPrice),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFB700)
        )
    }
}

@Composable
fun DisplayPaymentImage(item: CartItem) {
    val imgStr = item.imageUrl
    val isUrl = imgStr.startsWith("http") || imgStr.contains("/")

    if (isUrl) {
        AsyncImage(
            model = getFullImageUrl(imgStr),
            contentDescription = item.product.ten_san_pham,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        val resourceId = imgStr.toIntOrNull()

        if (resourceId != null && resourceId != 0) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = item.product.ten_san_pham,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.LocalPizza,
                contentDescription = item.product.ten_san_pham,
                tint = Color(0xFFFF6B35),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
fun PaymentDetailRow(
    label: String,
    value: String,
    valueColor: Color = Color(0xFF1A1A1A)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Color(0xFFFF6B35).copy(alpha = 0.05f) else Color.Transparent,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(0xFFFF6B35).copy(alpha = 0.3f)
        ) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = method.icon,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = method.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = method.description,
                    fontSize = 12.sp,
                    color = Color(0xFF888888)
                )
            }
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFFF6B35)
                )
            )
        }
    }
}
@Composable
fun ModernCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    action: String? = null,
    onActionClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = iconColor.copy(alpha = 0.1f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                }

                if (action != null && onActionClick != null) {
                    TextButton(
                        onClick = onActionClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFFFF6B35)
                        )
                    ) {
                        Text(
                            action,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
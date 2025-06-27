package com.example.pizza_app.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
package com.example.pizza_app.ui.order

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.concurrent.TimeUnit

@Composable
fun CountdownTimerCard(
    remainingTime: Long,
    showWarning: Boolean,
    onDismissWarning: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime) % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)

    val isWarning = minutes < 5
    val backgroundColor = if (isWarning) {
        HarmoniousColors.Warning.copy(alpha = 0.1f)
    } else {
        HarmoniousColors.Secondary.copy(alpha = 0.1f)
    }

    val textColor = if (isWarning) {
        HarmoniousColors.Warning
    } else {
        HarmoniousColors.Secondary
    }

    val iconColor = if (isWarning) {
        HarmoniousColors.Warning
    } else {
        HarmoniousColors.Secondary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(
            1.dp,
            if (isWarning) HarmoniousColors.Warning else HarmoniousColors.Secondary
        )
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isWarning) Icons.Default.Warning else Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = if (isWarning) "Cảnh báo!" else "Thời gian còn lại",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )
                }

                // Countdown display
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isWarning) HarmoniousColors.Warning else HarmoniousColors.Secondary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = timeString,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isWarning) {
                    "Đơn hàng sẽ bị hủy tự động sau $timeString nếu không được xác nhận"
                } else {
                    "Đơn hàng sẽ được hủy tự động nếu không được xác nhận trong $timeString"
                },
                fontSize = 12.sp,
                color = textColor.copy(alpha = 0.8f),
                lineHeight = 16.sp
            )

            if (isWarning) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Vui lòng liên hệ cửa hàng nếu cần hỗ trợ",
                        fontSize = 11.sp,
                        color = textColor.copy(alpha = 0.7f),
                        modifier = Modifier.weight(1f)
                    )

                    TextButton(
                        onClick = onDismissWarning,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = textColor
                        )
                    ) {
                        Text(
                            text = "Đã hiểu",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AutoCancelStatusCard(
    isAutoCancel: Boolean,
    cancelMessage: String?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isAutoCancel || cancelMessage != null,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isAutoCancel) {
                    HarmoniousColors.Warning.copy(alpha = 0.1f)
                } else {
                    HarmoniousColors.Error.copy(alpha = 0.1f)
                }
            ),
            border = BorderStroke(
                1.dp,
                if (isAutoCancel) HarmoniousColors.Warning else HarmoniousColors.Error
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAutoCancel) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = HarmoniousColors.Warning,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = HarmoniousColors.Error,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isAutoCancel) {
                            "Đang hủy đơn hàng tự động..."
                        } else {
                            "Đơn hàng đã bị hủy"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isAutoCancel) HarmoniousColors.Warning else HarmoniousColors.Error
                    )

                    if (cancelMessage != null) {
                        Text(
                            text = cancelMessage,
                            fontSize = 12.sp,
                            color = HarmoniousColors.OnSurfaceVariant,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
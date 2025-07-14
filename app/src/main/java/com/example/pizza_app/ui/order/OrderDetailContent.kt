package com.example.pizza_app.ui.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pizza_app.data.model.OrderDetail

@Composable
fun OrderDetailContent(
    orderDetail: OrderDetail,
    onCancelOrder: () -> Unit,
    onReviewOrder: () -> Unit,
    isCancelling: Boolean,
    // Thêm các parameter mới cho countdown timer
    remainingTime: Long? = null,
    isAutoCancel: Boolean = false,
    cancelMessage: String? = null
) {
    // State để hiển thị/ẩn cảnh báo
    var showWarning by remember { mutableStateOf(false) }

    // Kiểm tra nếu còn ít hơn 5 phút thì hiển thị cảnh báo
    LaunchedEffect(remainingTime) {
        if (remainingTime != null && remainingTime > 0) {
            val minutes = remainingTime / 60000
            showWarning = minutes < 5
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Spacer đầu
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Header đơn hàng
        item {
            CompactOrderHeader(orderDetail.don_hang)
        }

        // Hiển thị countdown timer cho đơn hàng "Chờ xác nhận"
        if (orderDetail.don_hang.trang_thai == "cho_xac_nhan" && remainingTime != null && remainingTime > 0) {
            item {
                CountdownTimerCard(
                    remainingTime = remainingTime,
                    showWarning = showWarning,
                    onDismissWarning = { showWarning = false }
                )
            }
        }

        // Hiển thị trạng thái auto cancel
        if (isAutoCancel || cancelMessage != null) {
            item {
                AutoCancelStatusCard(
                    isAutoCancel = isAutoCancel,
                    cancelMessage = cancelMessage
                )
            }
        }

        // Nút hủy đơn hàng (chỉ hiển thị khi trạng thái là "Chờ xác nhận" và không đang auto cancel)
        if (orderDetail.don_hang.trang_thai == "cho_xac_nhan" && !isAutoCancel) {
            item {
                CancelOrderButton(
                    onCancelOrder = onCancelOrder,
                    isCancelling = isCancelling
                )
            }
        }

        // Nút đánh giá đơn hàng (chỉ hiển thị khi trạng thái là "Hoàn thành")
        if (orderDetail.don_hang.trang_thai == "hoan_thanh") {
            item {
                ReviewOrderButton(onReviewOrder = onReviewOrder)
            }
        }

        // Card chứa tất cả mặt hàng
        item {
            MatHangListCard(orderDetail.mat_hang)
        }

        // Tổng tiền
        item {
            CompactTotalCard(orderDetail.don_hang)
        }

        // Spacer cuối
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
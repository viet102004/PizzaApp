package com.example.pizza_app.ui.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizza_app.data.model.Order
import com.example.pizza_app.ui.home.formatDateOnly
import com.example.pizza_app.ui.home.formatDateTime
import com.example.pizza_app.ui.home.formatDateTimeByParsing

@Composable
fun OrderList(orders: List<Order>, onDetailClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orders) { order ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mã đơn: #${order.ma_don_hang ?: "N/A"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF333333)
                        )

                        val statusText = order.trang_thai ?: "unknown"
                        androidx.compose.material3.Surface(
                            color = when (statusText) {
                                "cho_xac_nhan" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                                "dang_chuan_bi" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                                "dang_giao" -> Color(0xFF2196F3).copy(alpha = 0.1f)
                                "hoan_thanh" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                                "da_huy" -> Color(0xFFF44336).copy(alpha = 0.1f)
                                else -> Color.Gray.copy(alpha = 0.1f)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = when (statusText) {
                                    "cho_xac_nhan" -> "Chờ xác nhận"
                                    "dang_chuan_bi" -> "Đang chuẩn bị"
                                    "dang_giao" -> "Đang giao"
                                    "hoan_thanh" -> "Hoàn thành"
                                    "da_huy" -> "Đã hủy"
                                    else -> "Không xác định"
                                },
                                color = when (statusText) {
                                    "cho_xac_nhan" -> Color(0xFFFF9800)
                                    "dang_chuan_bi" -> Color(0xFFFF9800)
                                    "dang_giao" -> Color(0xFF2196F3)
                                    "hoan_thanh" -> Color(0xFF4CAF50)
                                    "da_huy" -> Color(0xFFF44336)
                                    else -> Color.Gray
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Đặt lúc: ${order.ngay_tao.formatDateTimeByParsing() ?: "Không xác định"}",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    order.items.forEach { item ->
                        val itemName = when (item.loai_mat_hang ?: "") {
                            "san_pham" -> item.ten_san_pham ?: "Sản phẩm"
                            "combo" -> item.ten_combo ?: "Combo"
                            else -> item.ten_san_pham ?: item.ten_combo ?: "Mặt hàng"
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• $itemName",
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "x${item.so_luong ?: 0}",
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    androidx.compose.material3.Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            val tongTienSanPham = order.tong_tien_san_pham ?: 0.0
                            val giamGiaMa = order.giam_gia_ma_giam_gia ?: 0.0
                            val giamGiaCombo = order.giam_gia_combo ?: 0.0
                            val phiGiaoHang = order.phi_giao_hang ?: 0.0
                            val tongTienCuoi = order.tong_tien_cuoi_cung ?: 0.0

                            if (giamGiaMa > 0 || giamGiaCombo > 0) {
                                Text(
                                    text = "Tạm tính: ${String.format("%,.0f", tongTienSanPham)} đ",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                                if (giamGiaMa > 0) {
                                    Text(
                                        text = "Giảm giá: -${String.format("%,.0f", giamGiaMa)} đ",
                                        fontSize = 12.sp,
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                                if (phiGiaoHang > 0) {
                                    Text(
                                        text = "Phí giao hàng: +${
                                            String.format("%,.0f", phiGiaoHang)
                                        } đ",
                                        fontSize = 12.sp,
                                        color = Color(0xFF666666)
                                    )
                                }
                            }

                            Text(
                                text = "Tổng tiền: ${String.format("%,.0f", tongTienCuoi)} đ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFFFFB700)
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                order.ma_don_hang.toInt().let { onDetailClick(it) }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFFFB700)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFFFB700))
                        ) {
                            Text("Xem chi tiết", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

package com.example.pizza_app.ui.order

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.*
import com.example.pizza_app.data.source.getFullImageUrl

// Định nghĩa color palette hài hòa
object HarmoniousColors {
    val Primary = Color(0xFFE8782F) // Cam ấm, dịu hơn
    val PrimaryLight = Color(0xFFFFF1E6) // Background cam nhạt
    val Secondary = Color(0xFF4A90A4) // Xanh teal cân bằng
    val Success = Color(0xFF2E7D32) // Xanh lá đậm hơn
    val Warning = Color(0xFFED6C02) // Cam cảnh báo
    val Error = Color(0xFFD32F2F) // Đỏ nhẹ hơn
    val Background = Color(0xFFF8F9FA) // Nền xám nhẹ
    val Surface = Color.White
    val OnSurface = Color(0xFF2D3748) // Xám đen dịu
    val OnSurfaceVariant = Color(0xFF718096) // Xám trung tính
    val OnSurfaceLight = Color(0xFF9CA3AF) // Xám nhạt
    val Divider = Color(0xFFE2E8F0) // Divider dịu
    val StarYellow = Color(0xFFFFC107) // Màu sao vàng
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: Int,
    viewModel: OrderDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val orderDetail by viewModel.orderDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isCancelling by viewModel.isCancelling.collectAsState()
    val isSubmittingReview by viewModel.isSubmittingReview.collectAsState()

    // State để hiển thị dialog xác nhận hủy và dialog đánh giá
    var showCancelDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderId) {
        viewModel.getOrderDetail(orderId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HarmoniousColors.Background)
    ) {
        // TopAppBar với gradient tinh tế
        TopAppBar(
            title = {
                Text(
                    "Chi tiết đơn hàng",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = HarmoniousColors.Primary
            )
        )

        when {
            isLoading -> Box(
                Modifier.fillMaxSize(),
                Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = HarmoniousColors.Primary,
                    strokeWidth = 3.dp
                )
            }

            errorMessage != null -> Box(
                Modifier.fillMaxSize(),
                Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Lỗi",
                    color = HarmoniousColors.Error,
                    fontSize = 16.sp
                )
            }
            orderDetail != null -> OrderDetailContent(
                orderDetail = orderDetail!!,
                onCancelOrder = { showCancelDialog = true },
                onReviewOrder = { showReviewDialog = true },
                isCancelling = isCancelling
            )

            else -> Box(
                Modifier.fillMaxSize(),
                Alignment.Center
            ) {
                Text(
                    "Không tìm thấy thông tin đơn hàng",
                    color = HarmoniousColors.OnSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        }
    }

    // Dialog xác nhận hủy đơn hàng
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = {
                Text(
                    text = "Xác nhận hủy đơn hàng",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Bạn có chắc chắn muốn hủy đơn hàng này không? Hành động này không thể hoàn tác.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        viewModel.cancelOrder(orderId)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = HarmoniousColors.Error
                    )
                ) {
                    Text("Hủy đơn hàng")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelDialog = false }
                ) {
                    Text("Không")
                }
            }
        )
    }

    // Dialog đánh giá đơn hàng
    if (showReviewDialog && orderDetail != null) {
        ReviewDialog(
            orderDetail = orderDetail!!,
            isSubmitting = isSubmittingReview,
            onDismiss = { showReviewDialog = false },
            onSubmitReview = { productId, rating, comment, imageUri ->
                viewModel.submitReview(orderId, productId, rating, comment, imageUri, context)
                showReviewDialog = false
            }
        )
    }
}

@Composable
fun OrderDetailContent(
    orderDetail: OrderDetail,
    onCancelOrder: () -> Unit,
    onReviewOrder: () -> Unit,
    isCancelling: Boolean
) {
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

        // Nút hủy đơn hàng (chỉ hiển thị khi trạng thái là "cho_xac_nhan")
        if (orderDetail.don_hang.trang_thai == "cho_xac_nhan") {
            item {
                CancelOrderButton(
                    onCancelOrder = onCancelOrder,
                    isCancelling = isCancelling
                )
            }
        }

        // Nút đánh giá đơn hàng (chỉ hiển thị khi trạng thái là "hoan_thanh")
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

@Composable
fun ReviewDialog(
    orderDetail: OrderDetail,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onSubmitReview: (Long, Int, String, String?) -> Unit
) {
    var selectedProductId by remember { mutableStateOf<Long?>(null) }
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri?.toString()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Tiêu đề
                Text(
                    text = "Đánh giá đơn hàng",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = HarmoniousColors.OnSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Chọn sản phẩm để đánh giá
                Text(
                    text = "Chọn sản phẩm:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HarmoniousColors.OnSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Danh sách sản phẩm
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(orderDetail.mat_hang.filter { it.loai_mat_hang == "san_pham" }) { matHang ->
                        val productId = matHang.ma_san_pham?.toLong() ?: 0L

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedProductId = productId },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedProductId == productId)
                                    HarmoniousColors.PrimaryLight
                                else
                                    HarmoniousColors.Background
                            ),
                            border = if (selectedProductId == productId)
                                androidx.compose.foundation.BorderStroke(2.dp, HarmoniousColors.Primary)
                            else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = getFullImageUrl(matHang.hinh_anh),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = matHang.ten_san_pham ?: "Sản phẩm",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = HarmoniousColors.OnSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Đánh giá sao
                Text(
                    text = "Đánh giá:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HarmoniousColors.OnSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating) HarmoniousColors.StarYellow else HarmoniousColors.OnSurfaceLight,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { rating = index + 1 }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bình luận
                Text(
                    text = "Bình luận:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = HarmoniousColors.OnSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Chia sẻ trải nghiệm của bạn...") },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Thêm hình ảnh
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hình ảnh:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = HarmoniousColors.OnSurface
                    )

                    TextButton(
                        onClick = { imagePickerLauncher.launch("image/*") }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Thêm ảnh")
                    }
                }

                // Hiển thị hình ảnh đã chọn
                if (imageUri != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nút hành động
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isSubmitting
                    ) {
                        Text("Hủy")
                    }

                    Button(
                        onClick = {
                            selectedProductId?.let { productId ->
                                onSubmitReview(productId, rating, comment, imageUri)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSubmitting && selectedProductId != null,
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
                }
            }
        }
    }
}

@Composable
fun CancelOrderButton(
    onCancelOrder: () -> Unit,
    isCancelling: Boolean
) {
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
                onClick = onCancelOrder,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HarmoniousColors.Error
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !isCancelling
            ) {
                if (isCancelling) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Đang hủy...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hủy đơn hàng",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lưu ý: Chỉ có thể hủy đơn hàng khi đang ở trạng thái \"Chờ xác nhận\"",
                fontSize = 12.sp,
                color = HarmoniousColors.OnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CompactOrderHeader(donHang: OrderDetail.DonHang) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = HarmoniousColors.Surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Dòng 1: Mã đơn hàng + Trạng thái
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${donHang.ma_don_hang}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = HarmoniousColors.OnSurface
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = getStatusColor(donHang.trang_thai).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = getStatusText(donHang.trang_thai),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = getStatusColor(donHang.trang_thai),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Thông tin người nhận
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = HarmoniousColors.OnSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${donHang.thong_tin_giao_hang.ten_nguoi_nhan} • ${donHang.thong_tin_giao_hang.so_dien_thoai}",
                    fontSize = 14.sp,
                    color = HarmoniousColors.OnSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Địa chỉ
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = HarmoniousColors.OnSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = donHang.thong_tin_giao_hang.dia_chi,
                    fontSize = 14.sp,
                    color = HarmoniousColors.OnSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun MatHangListCard(matHangList: List<MatHang>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = HarmoniousColors.Surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Tiêu đề
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = HarmoniousColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Danh sách mặt hàng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HarmoniousColors.OnSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Danh sách mặt hàng
            matHangList.forEachIndexed { index, matHang ->
                MatHangItem(matHang = matHang)

                // Divider giữa các item (trừ item cuối)
                if (index < matHangList.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(
                        color = HarmoniousColors.Divider,
                        thickness = 0.8.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
@Composable
fun MatHangItem(matHang: MatHang) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Hình ảnh
            val imageUrl = when (matHang.loai_mat_hang) {
                "san_pham" -> matHang.hinh_anh
                "combo" -> matHang.hinh_anh_combo
                else -> matHang.hinh_anh ?: matHang.hinh_anh_combo
            }

            AsyncImage(
                model = getFullImageUrl(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(HarmoniousColors.Background),
                contentScale = ContentScale.Crop
            )

            // Thông tin sản phẩm
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val itemName = when (matHang.loai_mat_hang) {
                    "san_pham" -> matHang.ten_san_pham ?: "Sản phẩm"
                    "combo" -> matHang.ten_combo ?: "Combo"
                    else -> matHang.ten_san_pham ?: matHang.ten_combo ?: "Mặt hàng"
                }

                Text(
                    text = itemName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HarmoniousColors.OnSurface,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Số lượng và đơn giá
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "x${matHang.so_luong}",
                        fontSize = 13.sp,
                        color = HarmoniousColors.OnSurfaceVariant
                    )
                    Text(
                        text = "${String.format("%,.0f", matHang.don_gia_co_ban)}đ",
                        fontSize = 13.sp,
                        color = HarmoniousColors.OnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Thành tiền
                Text(
                    text = "${String.format("%,.0f", matHang.thanh_tien)}đ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = HarmoniousColors.Primary
                )
            }
        }

        // Tùy chọn sản phẩm
        if (matHang.loai_mat_hang == "san_pham" && !matHang.tuy_chon.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = HarmoniousColors.Background.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    matHang.tuy_chon.forEach { tuyChon ->
                        CompactTuyChonItem(tuyChon = tuyChon)
                    }
                }
            }
        }

        // Chi tiết combo
        if (matHang.loai_mat_hang == "combo" && !matHang.chi_tiet_combo.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = HarmoniousColors.Background.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    matHang.chi_tiet_combo.forEach { chiTiet ->
                        CompactChiTietComboItem(chiTiet = chiTiet)
                    }
                }
            }
        }
    }
}

@Composable
fun CompactTuyChonItem(tuyChon: TuyChon) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${tuyChon.ten_tuy_chon}: ${tuyChon.gia_tri_tuy_chon}",
            fontSize = 13.sp,
            color = HarmoniousColors.OnSurfaceVariant,
            modifier = Modifier.weight(1f)
        )

        if (tuyChon.gia_them > 0) {
            Text(
                text = "+${String.format("%,.0f", tuyChon.gia_them)}đ",
                fontSize = 13.sp,
                color = HarmoniousColors.Primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CompactChiTietComboItem(chiTiet: ChiTietCombo) {
    Column(
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = "${chiTiet.ten_san_pham} (x${chiTiet.so_luong})",
            fontSize = 13.sp,
            color = HarmoniousColors.OnSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        chiTiet.tuy_chon?.forEach { tuyChonCombo ->
            Text(
                text = "  ${tuyChonCombo.ten_tuy_chon}: ${tuyChonCombo.gia_tri_tuy_chon}",
                fontSize = 12.sp,
                color = HarmoniousColors.OnSurfaceLight,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun CompactTotalCard(donHang: OrderDetail.DonHang) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = HarmoniousColors.Surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Tiêu đề với icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Receipt,
                    contentDescription = null,
                    tint = HarmoniousColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tổng kết thanh toán",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HarmoniousColors.OnSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chi tiết tính toán
            if (donHang.giam_gia_ma_giam_gia > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tạm tính",
                        fontSize = 14.sp,
                        color = HarmoniousColors.OnSurfaceVariant
                    )
                    Text(
                        text = "${String.format("%,.0f", donHang.tong_tien_san_pham)}đ",
                        fontSize = 14.sp,
                        color = HarmoniousColors.OnSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Giảm giá",
                        fontSize = 14.sp,
                        color = HarmoniousColors.OnSurfaceVariant
                    )
                    Text(
                        text = "-${String.format("%,.0f", donHang.giam_gia_ma_giam_gia)}đ",
                        fontSize = 14.sp,
                        color = HarmoniousColors.Success
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = HarmoniousColors.Divider,
                    thickness = 0.8.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Tổng cộng trong một Surface để tạo highlight
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = HarmoniousColors.PrimaryLight
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tổng cộng",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = HarmoniousColors.OnSurface
                    )

                    Text(
                        text = "${String.format("%,.0f", donHang.tong_tien_cuoi_cung)}đ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = HarmoniousColors.Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Thông tin thanh toán
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = HarmoniousColors.Background.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Phương thức thanh toán",
                            fontSize = 12.sp,
                            color = HarmoniousColors.OnSurfaceVariant
                        )
                        Text(
                            text = donHang.phuong_thuc_thanh_toan.toString(),
                            fontSize = 14.sp,
                            color = HarmoniousColors.OnSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = getPaymentStatusColor(donHang.trang_thai_thanh_toan).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = getPaymentStatusText(donHang.trang_thai_thanh_toan.toString()),
                            fontSize = 12.sp,
                            color = getPaymentStatusColor(donHang.trang_thai_thanh_toan),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// Helper functions với màu sắc hài hòa hơn
fun getStatusText(status: String): String = when (status) {
    "cho_xac_nhan" -> "Chờ xác nhận"
    "dang_chuan_bi" -> "Đang chuẩn bị"
    "dang_giao" -> "Đang giao"
    "hoan_thanh" -> "Hoàn thành"
    "da_huy" -> "Đã hủy"
    else -> "Không xác định"
}

fun getStatusColor(status: String): Color = when (status) {
    "cho_xac_nhan" -> HarmoniousColors.Warning
    "dang_chuan_bi" -> HarmoniousColors.Warning
    "dang_giao" -> HarmoniousColors.Secondary
    "hoan_thanh" -> HarmoniousColors.Success
    "da_huy" -> HarmoniousColors.Error
    else -> HarmoniousColors.OnSurfaceVariant
}

fun getPaymentMethodText(method: String?): String = when (method) {
    "tien_mat" -> "Tiền mặt"
    "the_tin_dung" -> "Thẻ tín dụng"
    "vi_dien_tu" -> "Ví điện tử"
    "chuyen_khoan" -> "Chuyển khoản"
    else -> "Không xác định"
}

fun getPaymentStatusText(status: String?): String = when (status) {
    "cho_xu_ly" -> "Chưa thanh toán"
    "hoan_thanh" -> "Đã thanh toán"
    "that_bai" -> "Thất bại"
    else -> "Không xác định"
}

fun getPaymentStatusColor(status: String?): Color = when (status) {
    "chua_thanh_toan" -> HarmoniousColors.Warning
    "da_thanh_toan" -> HarmoniousColors.Success
    "that_bai" -> HarmoniousColors.Error
    else -> HarmoniousColors.OnSurfaceVariant
}
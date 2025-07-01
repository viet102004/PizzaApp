@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.order

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.R
import com.example.pizza_app.data.model.Order
import com.example.pizza_app.ui.components.AuthDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    isLoggedIn: Boolean,
    onNavigateTo: (String) -> Unit
) {
    val tabs = listOf("Chờ xác nhận", "Đang chuẩn bị", "Đang giao", "Hoàn thành", "Đã hủy")
    val statusMap = listOf("cho_xac_nhan", "dang_chuan_bi" ,"dang_giao", "hoan_thanh", "da_huy")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val viewModel: OrderViewModel = viewModel()
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showAuthDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            viewModel.loadOrders()
        }
    }

    // SỬA: Xử lý null status khi filter
    LaunchedEffect(orders, selectedTabIndex) {
        Log.d("OrderScreen", "=== ORDER DEBUG INFO ===")
        Log.d("OrderScreen", "Total orders loaded: ${orders.size}")
        Log.d("OrderScreen", "Selected tab index: $selectedTabIndex")
        Log.d("OrderScreen", "Current filter status: ${statusMap[selectedTabIndex]}")

        orders.forEachIndexed { index, order ->
            val status = order.trang_thai ?: "null"
            Log.d("OrderScreen", "Order $index: ID=${order.ma_don_hang}, Status='$status', Items=${order.items.size}")
        }

        val currentStatus = statusMap[selectedTabIndex]
        val filteredCount = orders.count {
            val orderStatus = it.trang_thai ?: "" // SỬA: Convert null to empty string
            orderStatus == currentStatus
        }
        Log.d("OrderScreen", "Orders matching current status '$currentStatus': $filteredCount")
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        // TopAppBar (giữ nguyên)
        TopAppBar(
            title = { Text("Đơn hàng của tôi", fontSize = 26.sp, fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFFFFB700))
                }
                IconButton(onClick = {
                    if (isLoggedIn) onNavigateTo("favorite") else showAuthDialog = true
                }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color(0xFFFFB700))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        // Tabs (giữ nguyên)
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Color(0xFFFFB700),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xFFFFB700),
                    height = 3.dp
                )
            },
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (selectedTabIndex == index) Color(0xFFFFB700) else Color.Gray
                        )
                    },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        // Content - SỬA CHÍNH TẠI ĐÂY
        if (!isLoggedIn) {
            OrderEmptyContent(
                title = "Vui lòng đăng nhập",
                subtitle = "Bạn cần đăng nhập để xem các đơn hàng",
                onNavigateTo = onNavigateTo
            )
        } else if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFFB700))
            }
        } else if (errorMessage != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Lỗi: $errorMessage",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = {
                        viewModel.resetError()
                        viewModel.loadOrders()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700))
                ) {
                    Text("Thử lại", color = Color.White)
                }
            }
        } else {

            val currentStatus = statusMap[selectedTabIndex]

            // SỬA: Filter với null safety
            val filteredOrders = orders.filter { order ->
                val orderStatus = order.trang_thai ?: "" // Convert null to empty string
                Log.d("OrderScreen", "Comparing: '$orderStatus' == '$currentStatus'")
                orderStatus == currentStatus
            }

            // Debug filtering
            LaunchedEffect(selectedTabIndex, orders) {
                Log.d("OrderScreen", "=== FILTERING DEBUG ===")
                Log.d("OrderScreen", "Looking for status: '$currentStatus'")
                Log.d("OrderScreen", "Available orders with status:")
                orders.forEach { order ->
                    val status = order.trang_thai ?: "null"
                    Log.d("OrderScreen", "  - Order ${order.ma_don_hang}: status='$status' (length=${status.length})")
                }
                Log.d("OrderScreen", "Filtered result: ${filteredOrders.size} orders")
            }

            if (filteredOrders.isEmpty()) {
                // Debug info khi empty
                if (orders.isNotEmpty()) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // THÊM: Hiển thị tất cả đơn hàng khi status = null
                        if (orders.all { it.trang_thai == null }) {
                            Text(
                                text = "⚠️ Tất cả đơn hàng có status = null!",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Hiển thị tất cả đơn hàng:",
                                color = Color.Blue,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // SỬA: Nếu tất cả status là null, hiển thị tất cả đơn hàng
                if (orders.isNotEmpty() && orders.all { it.trang_thai == null }) {
                    Text(
                        text = "⚠️ API trả về dữ liệu lỗi (status = null). Hiển thị tất cả đơn hàng:",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    OrderList(orders = orders) // Hiển thị tất cả
                } else {
                    // Empty state bình thường
                    val emptyTitle = when (currentStatus) {
                        "cho_xac_nhan" -> "Bạn chưa có đơn hàng nào đang chờ xác nhận"
                        "dang_chuan_bi" -> ""
                        "dang_giao" -> "Chưa có đơn hàng đang giao"
                        "hoan_thanh" -> "Chưa có đơn hàng hoàn thành"
                        "da_huy" -> "Chưa có đơn hàng bị hủy"
                        else -> "Không có đơn hàng"
                    }
                    OrderEmptyContent(title = emptyTitle, onNavigateTo = onNavigateTo)
                }
            } else {
                OrderList(orders = filteredOrders)
            }
        }
    }

    AuthDialog(
        showDialog = showAuthDialog,
        onDismiss = { showAuthDialog = false },
        onLoginClick = { onNavigateTo("login") },
        onRegisterClick = { onNavigateTo("register") }
    )
}
@Composable
fun OrderEmptyContent(
    title: String = "Quên chưa đặt món rồi nè bạn ơi!!!",
    subtitle: String = "Bạn sẽ nhìn thấy các món đang được chuẩn bị hoặc giao đi tại đây để kiểm tra đơn hàng nhanh hơn!",
    onNavigateTo: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon with background circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color(0xFFFFB700).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color(0xFFFFB700)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = subtitle,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // CTA Button
        Button(
            onClick = { onNavigateTo("home") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFB700)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Đặt món ngay",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
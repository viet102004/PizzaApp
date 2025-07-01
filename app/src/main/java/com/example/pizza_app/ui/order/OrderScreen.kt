@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.order

import android.util.Log
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
import com.example.pizza_app.ui.components.AuthDialog

@Composable
fun OrderScreen(
    navController: NavController,
    isLoggedIn: Boolean,
    onNavigateTo: (String) -> Unit
) {
    val tabs = listOf("Chờ xác nhận", "Đang giao", "Hoàn thành", "Đã hủy")
    val statusMap = listOf("cho_xac_nhan", "dang_giao", "hoan_thanh", "da_huy")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val viewModel: OrderViewModel = viewModel()
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showAuthDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isLoggedIn) {
            viewModel.loadOrders()
        }
    }

    // Debug logs
    LaunchedEffect(orders) {
        Log.d("OrderScreen", "Orders loaded: ${orders.size}")
        orders.forEach { order ->
            Log.d("OrderScreen", "Order ${order.ma_don_hang}: DB Status = ${order.trang_thai}, UI Status = ${order.trang_thai}")
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        // TopAppBar
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

        // Tabs
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

        // Content
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
            // Error state
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
            val filteredOrders = orders.filter { it.trang_thai == currentStatus }

            // Debug filtering
            LaunchedEffect(selectedTabIndex, orders) {
                Log.d("OrderScreen", "Filtering for status: $currentStatus")
                Log.d("OrderScreen", "Available orders: ${orders.map { "${it.ma_don_hang}:${it.trang_thai}" }}")
                Log.d("OrderScreen", "Filtered orders: ${filteredOrders.size}")
            }

            if (filteredOrders.isEmpty()) {
                val emptyTitle = when (currentStatus) {
                    "cho_xac_nhan" -> "Bạn chưa có đơn hàng nào đang chờ xác nhận"
                    "dang_giao" -> "Chưa có đơn hàng đang giao"
                    "hoan_thanh" -> "Chưa có đơn hàng hoàn thành"
                    "da_huy" -> "Chưa có đơn hàng bị hủy"
                    else -> "Không có đơn hàng"
                }

                OrderEmptyContent(title = emptyTitle, onNavigateTo = onNavigateTo)
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

        // Sample suggested items
    }
}

@Composable
fun SuggestShopItem(
    name: String,
    rating: Double,
    discount: String,
    imageRes: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Navigate to restaurant */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Restaurant image
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "$rating",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color(0xFF666666)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Discount tags
                Row {
                    AssistChip(
                        onClick = { /* TODO */ },
                        label = { Text("Giảm giá", fontSize = 12.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFFFB700).copy(alpha = 0.1f),
                            labelColor = Color(0xFFFFB700)
                        ),
                        modifier = Modifier.height(28.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    AssistChip(
                        onClick = { /* TODO */ },
                        label = { Text("Giảm $discount", fontSize = 12.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f),
                            labelColor = Color(0xFF4CAF50)
                        ),
                        modifier = Modifier.height(28.dp)
                    )
                }
            }
        }
    }
}

// Sample data
data class SuggestedItem(
    val name: String,
    val rating: Double,
    val discount: String,
    val imageRes: Int
)
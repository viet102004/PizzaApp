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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.R
import com.example.pizza_app.data.model.Order
import com.example.pizza_app.ui.components.AuthDialog


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderScreen(
    navController: NavController,
    isLoggedIn: Boolean,
    onNavigateTo: (String) -> Unit
) {
    val tabs = listOf("Chờ xác nhận", "Đang chuẩn bị", "Đang giao", "Hoàn thành", "Đã hủy")
    val statusMap = listOf("cho_xac_nhan", "dang_chuan_bi", "dang_giao", "hoan_thanh", "da_huy")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val viewModel: OrderViewModel = viewModel()
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showAuthDialog by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { viewModel.refreshData() }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {

            TopAppBar(
                title = { Text("Đơn hàng của tôi", fontSize = 26.sp, fontWeight = FontWeight.Bold) },
                actions = {
//                    IconButton(onClick = {
//                        if (isLoggedIn) onNavigateTo("favorite") else showAuthDialog = true
//                    }) {
//                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color(0xFFFFB700))
//                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

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

            if (!isLoggedIn) {
                OrderEmptyContent(
                    title = "Vui lòng đăng nhập",
                    subtitle = "Bạn cần đăng nhập để xem các đơn hàng",
                    onNavigateTo = onNavigateTo
                )
            } else if (isLoading && orders.isEmpty()) {
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
                            viewModel.refreshData()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700))
                    ) {
                        Text("Thử lại", color = Color.White)
                    }
                }
            } else {
                val currentStatus = statusMap[selectedTabIndex]
                val filteredOrders = orders.filter {
                    (it.trang_thai ?: "") == currentStatus
                }

                if (filteredOrders.isEmpty()) {
                    OrderEmptyContent(
                        title = when (currentStatus) {
                            "cho_xac_nhan" -> "Bạn chưa có đơn hàng nào đang chờ xác nhận"
                            "dang_chuan_bi" -> "Chưa có đơn hàng đang được chuẩn bị"
                            "dang_giao" -> "Chưa có đơn hàng đang giao"
                            "hoan_thanh" -> "Chưa có đơn hàng hoàn thành"
                            "da_huy" -> "Chưa có đơn hàng bị hủy"
                            else -> "Không có đơn hàng"
                        },
                        onNavigateTo = onNavigateTo
                    )
                } else {
                    OrderList(
                        orders = filteredOrders,
                        onDetailClick = { maDonHang ->
                            navController.navigate("order_detail/$maDonHang") // Dùng NavController dẫn đến màn chi tiết
                        }
                    )

                }
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
            contentColor = Color(0xFFFFB700)
        )
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
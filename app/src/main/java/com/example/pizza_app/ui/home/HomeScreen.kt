// HomeScreen.kt (Updated with Auto + Pull-to-Refresh)
package com.example.pizza_app.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.ui.components.AuthDialog

@Composable
fun PizzaKimchiLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        // Pizza icon với gradient
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFB700), // Vàng cam
                            Color(0xFFFF8F00)  // Cam đậm
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🍕",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Text logo
        Column {
            Text(
                text = "Pizza",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF8F00),
                lineHeight = 22.sp
            )
            Text(
                text = "KimChi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE53935), // Màu đỏ kimchi
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Kimchi accent
        Text(
            text = "🌶️",
            fontSize = 18.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isLoggedIn: Boolean,
    onNavigateTo: (String) -> Unit
) {
    Log.d("HomeScreen", "HomeScreen được gọi")

    // ViewModels
    val viewModel: HomeViewModel = viewModel()
    val productList by viewModel.productList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val categoryViewModel: CategoryViewModel = viewModel()
    val categories by categoryViewModel.categories.collectAsState()

    val bannerViewModel: BannerViewModel = viewModel()
    val banners by bannerViewModel.bannerList.collectAsState()
    val isBannerLoading by bannerViewModel.isLoading.collectAsState()

    var showAuthDialog by remember { mutableStateOf(false) }

    // Pull to refresh state (Material Design)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = {
            viewModel.refreshData()
        }
    )

    // Handle pull to refresh - Không cần LaunchedEffect nữa
    // Logic đã được handle trong rememberPullRefreshState

    // Lifecycle observer để refresh khi user quay lại màn hình
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    // Refresh khi user quay lại màn hình
                    viewModel.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    // Có thể pause auto refresh để tiết kiệm tài nguyên
                    Log.d("HomeScreen", "Screen paused")
                }
                else -> {}
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
        ) {
            TopAppBar(
                title = {
                    PizzaKimchiLogo()
                },
                actions = {
                    // Icon Search
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFFB700), shape = CircleShape)
                            .clickable {
                                onNavigateTo("search")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Tìm kiếm",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Icon Favorite
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFFB700), shape = CircleShape)
                            .clickable {
                                if (isLoggedIn) {
                                    onNavigateTo("favorite")
                                } else {
                                    showAuthDialog = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = "Yêu thích",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // Nội dung chính
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị error nếu có
                error?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        backgroundColor = Color(0xFFFFEBEE)
                    ) {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Banner Section
                PromoBanner(
                    banners = banners,
                    isLoading = isBannerLoading,
                    onBannerClick = { banner ->
                        banner.ma_san_pham?.let { productId ->
                            navController.navigate("product_detail/$productId")
                        } ?: run {
                            banner.link_chuyen_huong?.let { link ->
                                // Xử lý link_chuyen_huong nếu cần
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Section
                CategorySection(
                    categories = categories,
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Section
                ProductSection(
                    products = productList,
                    navController = navController
                )

                // Loading indicator cho initial load
                if (isLoading && productList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(
                            color = Color(0xFFFFB700)
                        )
                    }
                }

                // Spacer cuối để tránh bị che bởi bottom navigation
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Pull to refresh indicator (Material Design)
        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = Color.White,
            contentColor = Color(0xFFFFB700)
        )
    }

    // Auth Dialog
    AuthDialog(
        showDialog = showAuthDialog,
        onDismiss = { showAuthDialog = false },
        onLoginClick = { onNavigateTo("login") },
        onRegisterClick = { onNavigateTo("register") }
    )
}
package com.example.pizza_app.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(Color(0xFFFFB700), Color(0xFFFF8F00))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🍕", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text("Pizza", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8F00))
            Text("KimChi", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE53935))
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text("🌶️", fontSize = 18.sp)
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

    val scrollState = rememberScrollState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = {
            viewModel.refreshData()
            bannerViewModel.refreshData()
            categoryViewModel.refreshData()
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onResume()
                bannerViewModel.onResume()
                categoryViewModel.onResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Tính toán xem có nên hiển thị banner hay không
    val showBanner by remember {
        derivedStateOf {
            scrollState.value < 200 // Banner sẽ biến mất khi scroll xuống 200px
        }
    }

    // Tính toán xem category có nên sticky hay không
    val isCategorySticky by remember {
        derivedStateOf {
            scrollState.value >= 200 // Category trở thành sticky khi scroll xuống 200px
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
            // TopAppBar - luôn cố định
            TopAppBar(
                title = {
                    PizzaKimchiLogo()
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFFB700), shape = CircleShape)
                            .clickable { onNavigateTo("search") },
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            // Category Sticky khi scroll
            if (isCategorySticky) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .zIndex(1f)
                ) {
                    CategorySection(
                        categories = categories,
                        navController = navController
                    )
                }
            }

            // Phần nội dung có thể scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Banner - chỉ hiển thị khi showBanner = true
                if (showBanner) {
                    PromoBanner(
                        banners = banners,
                        isLoading = isBannerLoading,
                        onBannerClick = { banner ->
                            banner.ma_san_pham?.let { navController.navigate("product_detail/$it") }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Category - hiển thị trong scroll khi chưa sticky
                if (!isCategorySticky) {
                    CategorySection(
                        categories = categories,
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Error message nếu có
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

                // Product section
                ProductSection(
                    products = productList,
                    navController = navController
                )

                // Loading indicator
                if (isLoading && productList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFFB700))
                    }
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
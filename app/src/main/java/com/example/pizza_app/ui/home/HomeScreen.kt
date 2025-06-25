// HomeScreen.kt (Updated)
package com.example.pizza_app.ui.home

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pizza_app.R
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.ItemXamp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isLoggedIn: Boolean,
    onNavigateTo: (String) -> Unit
) {

    Log.d("HomeScreen", "HomeScreen được gọi")

    val viewModel: HomeViewModel = viewModel()
    val productList by viewModel.productList.collectAsState()
    val categoryViewModel: CategoryViewModel = viewModel()
    val categories by categoryViewModel.categories.collectAsState()
    val bannerViewModel: BannerViewModel = viewModel()
    val banners by bannerViewModel.bannerList.collectAsState()

    // State cho dialog
    var showAuthDialog by remember { mutableStateOf(false) }

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
                // Icon Search với background tròn
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFB700), shape = CircleShape)
                        .clickable {
                            onNavigateTo("search") // 🧭 Chuyển đến SearchScreen
                        }
                    ,
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

                // Icon Profile với background tròn
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
            PromoBanner(banners = banners)
            Spacer(modifier = Modifier.height(16.dp))
            CategorySection(categories = categories, navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
            ProductSection(products = productList, navController = navController)
        }
    }

    // Auth Dialog
    AuthDialog(
        showDialog = showAuthDialog,
        onDismiss = { showAuthDialog = false },
        onLoginClick = { onNavigateTo("login") },
        onRegisterClick = { onNavigateTo("register") }
    )
}
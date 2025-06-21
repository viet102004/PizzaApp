package com.example.pizza_app.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pizza_app.ui.components.BottomNavItem
import com.example.pizza_app.ui.components.BottomNavBar
import com.example.pizza_app.navigation.AppNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.core.view.WindowCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

private val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Default.Home, "Home"),
    BottomNavItem("cart", Icons.Default.ShoppingCart, "Cart"),
    BottomNavItem("order", Icons.Default.List, "Orders"),
    BottomNavItem("profile", Icons.Default.Person, "Profile")
)

private val routesWithoutBottomNav = setOf(
    "login", "product_detail/{id}", "profile_details",
    "update_name", "update_password", "update_email",
    "update_phone", "update_dob", "pay", "wallet", "settings",
    "support_chat","forgot_password","register", "all_categories"
)

@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute by remember {
        derivedStateOf { navBackStackEntry?.destination?.route }
    }

    val shouldShowBottomNav by remember {
        derivedStateOf {
            currentRoute !in routesWithoutBottomNav
        }
    }

    val onNavigate = remember {
        { item: BottomNavItem ->
            navController.navigate(item.route) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Nội dung chính
        AppNavigation(navController)

        if (shouldShowBottomNav) {
            BottomNavBar(
                items = bottomNavItems,
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(), // Thêm dòng này để rộng ra full trái và phải
                onItemClick = onNavigate
            )
        }
    }
}
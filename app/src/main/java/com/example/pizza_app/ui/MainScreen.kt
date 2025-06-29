package com.example.pizza_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*

// Updated bottom nav items with better icons
private val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Outlined.Home, "Home"),
    BottomNavItem("cart", Icons.Outlined.ShoppingCart, "Cart"),
    BottomNavItem("order", Icons.Outlined.Receipt, "Orders"),
    BottomNavItem("profile", Icons.Outlined.Person, "Profile")
)

// More comprehensive list of routes without bottom nav
private val routesWithoutBottomNav = setOf(
    "login", "product_detail/{id}", "profile_details",
    "update_name", "update_password", "update_email",
    "update_phone", "update_dob", "pay", "wallet", "settings",
    "support_chat","forgot_password","register", "all_categories", "favorite",
    "search", "category_products/{categoryId}/{categoryName}", "add_address", "address"
)

@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }

    val shouldShowBottomNav by remember {
        derivedStateOf {
            currentRoute?.let { route ->
                // Check exact match first
                if (route in routesWithoutBottomNav) return@derivedStateOf false

                // Check for routes with parameters
                routesWithoutBottomNav.none { excludedRoute ->
                    if (excludedRoute.contains("{")) {
                        // Extract the base route (before parameters)
                        val baseRoute = excludedRoute.substringBefore("{")
                        route.startsWith(baseRoute)
                    } else {
                        route == excludedRoute
                    }
                }
            } ?: true
        }
    }

    val onNavigate = remember {
        { item: BottomNavItem ->
            if (currentRoute != item.route) {
                navController.navigate(item.route) {
                    // Pop up to the start destination to avoid building up a large stack
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    Scaffold(
        backgroundColor = Color(0xFFF5F5F5),
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavBar(
                    items = bottomNavItems,
                    navController = navController,
                    modifier = Modifier.fillMaxWidth(),
                    onItemClick = onNavigate
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNavigation(navController)
        }
    }
}
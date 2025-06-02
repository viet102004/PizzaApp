package com.example.pizza_app.ui

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.pizza_app.ui.components.BottomNavItem
import com.example.pizza_app.ui.components.BottomNavBar
import com.example.pizza_app.navigation.AppNavigation

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("cart", Icons.Default.ShoppingCart, "Cart"),
        BottomNavItem("order", Icons.Default.List, "Orders"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = items,
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    ) {
        AppNavigation(navController)
    }
}

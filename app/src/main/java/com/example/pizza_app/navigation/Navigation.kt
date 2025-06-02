package com.example.pizza_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pizza_app.ui.cart.CartScreen
import com.example.pizza_app.ui.home.HomeScreen
import com.example.pizza_app.ui.order.OrderScreen
import com.example.pizza_app.ui.profile.ProfileScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable("cart") { CartScreen() }
        composable("order") { OrderScreen() }
        composable("profile") { ProfileScreen() }
    }
}

package com.example.pizza_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pizza_app.ui.cart.CartScreen
import com.example.pizza_app.ui.home.HomeScreen
import com.example.pizza_app.ui.order.OrderScreen
import com.example.pizza_app.ui.profile.ProfileScreen
import com.example.pizza_app.ui.profile.ProfileItem
import com.example.pizza_app.ui.wallet.WalletScreen
import com.example.pizza_app.ui.vouchers.VouchersScreen
import com.example.pizza_app.ui.profile.ProfileDetailsScreen
import com.example.pizza_app.ui.profile.AddressScreen
import com.example.pizza_app.ui.profile.SupportChatScreen
import com.example.pizza_app.ui.profile.SettingsScreen
import com.example.pizza_app.ui.auth.LoginScreen
import com.example.pizza_app.ui.cart.PayScreen
import com.example.pizza_app.ui.home.FavoriteScreen
import com.example.pizza_app.ui.home.ProductDetailScreen
import com.example.pizza_app.ui.home.ProductSection
import com.example.pizza_app.ui.profile.UpdateDOBScreen
import com.example.pizza_app.ui.profile.UpdateEmailScreen
import com.example.pizza_app.ui.profile.UpdateNameScreen
import com.example.pizza_app.ui.profile.UpdatePasswordScreen
import com.example.pizza_app.ui.profile.UpdatePhoneScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("cart") { CartScreen(navController) }
        composable("order") {
            OrderScreen(navController)
        }
        composable("profile") {
            ProfileScreen(
                isLoggedIn = true,
                userName = "Hoàng Việt",
                points = 1000,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                onNavigateTo = { destination ->
                    navController.navigate(destination)
                }
            )
        }
        composable("wallet") { WalletScreen(navController) }
        composable("vouchers") { VouchersScreen() }
        composable("profile_details") { ProfileDetailsScreen(navController) }
        composable("address") { AddressScreen() }
        composable("support_chat") { SupportChatScreen() }
        composable("settings") { SettingsScreen() }
        composable("login") { LoginScreen(navController) }
        composable ("pay"){ PayScreen(navController) }
        composable ("favorite"){ FavoriteScreen(navController) }
        composable("product_detail") { ProductDetailScreen(navController) }
        composable("update_name") { UpdateNameScreen(navController) }
        composable("update_phone") { UpdatePhoneScreen(navController) }
        composable("update_email") { UpdateEmailScreen(navController) }
        composable("update_dob") { UpdateDOBScreen(navController) }
        composable("update_password") { UpdatePasswordScreen(navController) }
    }
}

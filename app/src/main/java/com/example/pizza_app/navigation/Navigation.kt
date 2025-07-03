// Trong AppNavigation.kt - Cách chia sẻ CartViewModel giữa các màn hình

package com.example.pizza_app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.pizza_app.data.model.OrderDetail
import com.example.pizza_app.data.model.User
import com.example.pizza_app.data.model.UserPreferences
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.ui.auth.ForgotPasswordScreen
import com.example.pizza_app.ui.cart.CartScreen
import com.example.pizza_app.ui.cart.CartViewModel
import com.example.pizza_app.ui.home.HomeScreen
import com.example.pizza_app.ui.order.OrderScreen
import com.example.pizza_app.ui.profile.ProfileScreen
import com.example.pizza_app.ui.wallet.WalletScreen
import com.example.pizza_app.ui.profile.ProfileDetailsScreen
import com.example.pizza_app.ui.profile.SupportChatScreen
import com.example.pizza_app.ui.auth.LoginScreen
import com.example.pizza_app.ui.auth.RegisterScreen
import com.example.pizza_app.ui.cart.PayScreen
import com.example.pizza_app.ui.home.AllCategoriesScreen
import com.example.pizza_app.ui.home.CategoryProductsScreen
import com.example.pizza_app.ui.home.CategoryViewModel
import com.example.pizza_app.ui.home.FavoriteScreen
import com.example.pizza_app.ui.home.ProductDetailScreen
import com.example.pizza_app.ui.home.SearchScreen
import com.example.pizza_app.ui.order.OrderDetailScreen
import com.example.pizza_app.ui.order.OrderDetailViewModel
import com.example.pizza_app.ui.profile.AddOrEditAddressScreen
import com.example.pizza_app.ui.profile.AddressListScreen
import com.example.pizza_app.ui.profile.AddressViewModel
import com.example.pizza_app.ui.profile.UpdateDOBScreen
import com.example.pizza_app.ui.settings.SettingsScreen
import com.example.pizza_app.ui.profile.UpdateEmailScreen
import com.example.pizza_app.ui.profile.UpdateNameScreen
import com.example.pizza_app.ui.profile.UpdatePasswordScreen
import com.example.pizza_app.ui.profile.UpdatePhoneScreen
import com.example.pizza_app.ui.vouchers.VoucherScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    val cartViewModel: CartViewModel = viewModel()
    val context = LocalContext.current
    val user by UserManager.currentUser.collectAsState()
    val isLoggedIn = user != null

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                isLoggedIn = isLoggedIn,
                onNavigateTo = { destination -> navController.navigate(destination) }
            )
        }

        composable("cart") {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                isLoggedIn = isLoggedIn,
                onNavigateTo = { destination -> navController.navigate(destination) }
            )
        }

        composable("order") {
            OrderScreen(
                navController = navController,
                isLoggedIn = isLoggedIn,
                onNavigateTo = { destination -> navController.navigate(destination) }
            )
        }

        composable("order_detail/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")?.toIntOrNull()
            if (orderId != null) {
                OrderDetailScreen(orderId = orderId, onBackClick = { navController.popBackStack() })
            }
        }



        composable("profile") {

            ProfileScreen(
                isLoggedIn = user != null,
                onLogout = {
                    UserManager.setUser(null)
                    UserPreferences(context).clear()
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateTo = { destination ->
                    navController.navigate(destination)
                }
            )

        }

        composable("search") { SearchScreen(navController) }
        composable("wallet") { WalletScreen(navController) }
        composable("vouchers") { VoucherScreen(navController) }
        composable("profile_details") { ProfileDetailsScreen(navController) }
        composable("address") { AddressListScreen(navController) }
        composable ("register"){ RegisterScreen(navController) }
        composable ("forgot_password"){ ForgotPasswordScreen(navController) }
        composable("support_chat") { SupportChatScreen(navController) }
        composable("settings") { SettingsScreen() }
        composable("login") { LoginScreen(navController) }
        composable ("pay"){ PayScreen(navController) }
        composable ("favorite"){ FavoriteScreen(navController) }

        composable("add_address") {
            AddOrEditAddressScreen(
                navController = navController,
                isEditMode = false
            )
        }

        composable("edit_address/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
            val viewModel: AddressViewModel = viewModel()
            val addressList by viewModel.addressList.collectAsState()

            LaunchedEffect(id) {
                if (id != null) {
                    viewModel.getDeliveryAddresses()
                }
            }

            val address = addressList.find { it.ma_thong_tin_giao_hang == id }

            address?.let {
                AddOrEditAddressScreen(
                    navController = navController,
                    isEditMode = true,
                    existingAddress = it,
                    viewModel = viewModel
                )
            }
        }

        composable("product_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                ProductDetailScreen(
                    navController = navController,
                    maSanPham = id,
                    isLoggedIn = isLoggedIn,
                    onNavigateTo = { route ->
                        navController.navigate(route)
                    },
                    cartViewModel = cartViewModel
                )
            }
        }

        composable("update_name") { UpdateNameScreen(navController) }
        composable("update_phone") { UpdatePhoneScreen(navController) }
        composable("update_email") { UpdateEmailScreen(navController) }
        composable("update_dob") { UpdateDOBScreen(navController) }
        composable("update_password") { UpdatePasswordScreen(navController) }

        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("all_categories") {
            val categoryViewModel: CategoryViewModel = viewModel()
            val categories by categoryViewModel.categories.collectAsState()

            AllCategoriesScreen(
                categories = categories,
                onCategoryClick = { category ->
                    navController.navigate("category_products/${category.ma_danh_muc}/${category.ten_danh_muc}")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("category_products/{categoryId}/{categoryName}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
            val categoryName = backStackEntry.arguments?.getString("categoryName")

            if (categoryId != null && categoryName != null) {
                val category = com.example.pizza_app.data.model.Category(
                    ma_danh_muc = categoryId,
                    ten_danh_muc = categoryName,
                    hinh_anh = ""
                )
                CategoryProductsScreen(
                    navController = navController,
                    category = category
                )
            }
        }
    }
}
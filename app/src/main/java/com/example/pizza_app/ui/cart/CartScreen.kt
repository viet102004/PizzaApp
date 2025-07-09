@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.cart

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.data.model.CartItem
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.model.ProductOption
import com.example.pizza_app.data.source.remote.RetrofitInstance
import com.example.pizza_app.ui.components.AuthDialog
import com.example.pizza_app.ui.cart.CartItemCard

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    isLoggedIn: Boolean,
    onNavigateTo: (String) -> Unit
) {
     // true khi bấm "chỉnh sửa"

    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.totalAmount.collectAsState()
    val itemCount by cartViewModel.itemCount.collectAsState()

    val isEditMode = mutableStateOf(false)
    val showDialog = remember { mutableStateOf(false) }
    val selectedCartItem = remember { mutableStateOf<CartItem?>(null) }
    val product = remember { mutableStateOf<Product?>(null) }
    val quantity = remember { mutableStateOf(1) }
    val selectedImage = remember { mutableStateOf<String?>(null) }
    val dialogAction = remember { mutableStateOf("add_to_cart") }

    val selectedOptions = remember { mutableStateMapOf<Long, Long>() }

    val multipleSelectedOptions = remember { mutableStateMapOf<Long, MutableSet<Long>>() }

    var showAuthDialog by remember { mutableStateOf(false) }
    val productOptionViewModel: CartViewModel = viewModel()
    val options by productOptionViewModel.options.collectAsState()


    //val options = remember { mutableStateListOf<ProductOption>() }

    LaunchedEffect(Unit) {
        cartViewModel.fetchCartItems()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFFFFB700),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Giỏ hàng",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (cartItems.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .shadow(elevation = 4.dp, shape = CircleShape)
                                .background(color = Color(0xFFFF6B35), shape = CircleShape)
                                .size(28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cartItems.size.toString(),
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            actions = {
                // Nút xóa tất cả (chỉ hiện khi có sản phẩm)
                if (cartItems.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFFF6B35), shape = CircleShape)
                            .clickable {
                                cartViewModel.clearAllItems()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.DeleteSweep,
                            contentDescription = "Xóa tất cả",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // Nút yêu thích
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
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        if (cartItems.isEmpty()) {
            CartEmptyContent(navController)
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Main content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(cartItems, key = { it.id }) { item: CartItem ->
                        CartItemCard(
                            item = item,
                            onQuantityChange = { newQuantity ->
                                cartViewModel.updateQuantity(item.id, newQuantity)
                            },
                            onRemove = {
                                cartViewModel.removeFromCart(item.id)
                            },
//                            onEditClick = {
//                                selectedCartItem.value = item
//                                product.value = item.product
//                                quantity.value = item.quantity
//                                selectedImage.value = item.imageUrl
//                                dialogAction.value = "edit_cart"
//                                isEditMode.value = true
//
//                                restoreSelectedOptionsFromCartItem(
//                                    item = item,
//                                    options = options,
//                                    selectedOptions = selectedOptions,
//                                    multipleSelectedOptions = multipleSelectedOptions
//                                )
//
//                                showDialog.value = true
//                            }

                        )
                    }
                }

                // Compact bottom section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "$itemCount món",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = formatCurrency(cartTotal),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFB700)
                            )
                        }

                        Button(
                            onClick = {
                                println("Đặt hàng clicked - Navigating to pay")
                                onNavigateTo("pay")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFB700)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = "Đặt hàng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    AuthDialog(
        showDialog = showAuthDialog,
        onDismiss = { showAuthDialog = false },
        onLoginClick = { onNavigateTo("login") },
        onRegisterClick = { onNavigateTo("register") }
    )
}

fun restoreSelectedOptionsFromCartItem(
    item: CartItem,
    options: List<ProductOption>,
    selectedOptions: MutableMap<Long, Long>,
    multipleSelectedOptions: MutableMap<Long, MutableSet<Long>>
) {
    selectedOptions.clear()
    multipleSelectedOptions.clear()

    item.extraOptions.forEach { selected ->
        val matchedOption = options.find { option ->
            option.gia_tri.any { it.ma_gia_tri == selected.ma_gia_tri }
        }

        matchedOption?.let { option ->
            when (option.loai_lua_chon) {
                "radio", "single" -> {
                    selectedOptions[option.ma_loai_tuy_chon.toLong()] = selected.ma_gia_tri.toLong()
                }
                "checkbox", "multiple" -> {
                    val current = multipleSelectedOptions.getOrPut(option.ma_loai_tuy_chon.toLong()) { mutableSetOf() }
                    current.add(selected.ma_gia_tri.toLong())
                }
            }
        }
    }
}



@Composable
fun CartEmptyContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(Color(0xFFFFB700).copy(alpha = 0.1f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🍕", fontSize = 80.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quên chưa thêm món rồi nè bạn ơi!!!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Hãy thêm một số món pizza ngon vào giỏ hàng của bạn để bắt đầu đặt hàng!",
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            color = Color(0xFF666666),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700)),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                "Đặt món ngay",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

fun formatCurrency(amount: Double): String {
    return "${String.format("%,.0f", amount)}đ"
}
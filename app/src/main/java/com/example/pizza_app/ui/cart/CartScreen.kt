@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.cart

import CartItemCard
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pizza_app.data.source.ItemXamp.sampleCartItems
import java.text.DecimalFormat


fun formatCurrency(amount: Double): String {
    val formatter = DecimalFormat("#,###")
    return "${formatter.format(amount)}đ"
}



@Composable
fun CartScreen(navController: NavController) {
    var cartItems by remember { mutableStateOf(sampleCartItems) }

    val subtotal by remember {
        derivedStateOf { cartItems.sumOf { it.price * it.quantity } }
    }

    val totalUniqueItems by remember {
        derivedStateOf { cartItems.size }
    }

    val onQuantityChange = remember {
        { itemId: String, newQuantity: Int ->
            cartItems = cartItems.map { item ->
                if (item.id == itemId) item.copy(quantity = newQuantity) else item
            }
        }
    }

    val onRemoveItem = remember {
        { itemId: String ->
            cartItems = cartItems.filter { it.id != itemId }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Giỏ hàng",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (cartItems.isNotEmpty()) {
                        Text(
                            text = " (${totalUniqueItems.toString().padStart(2, '0')})",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            },
            actions = {
                // Icon Favorite với background tròn
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFB700), shape = CircleShape)
                        .clickable { /* TODO: Favorites */ },
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

        // Conditional Content
        if (cartItems.isEmpty()) {
            EmptyCartScreen()
        } else {
            // Cart Items
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = cartItems,
                    key = { it.id } // Important for performance
                ) { item ->
                    CartItemCard(
                        item = item,
                        onQuantityChange = { newQuantity ->
                            onQuantityChange(item.id, newQuantity)
                        },
                        onRemove = { onRemoveItem(item.id) }
                    )
                }
            }

            CheckoutSection(
                subtotal = subtotal,
                onCheckout = { navController.navigate("pay") }
            )
        }
    }
}

@Composable
private fun CheckoutSection(
    subtotal: Double,
    onCheckout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Total Cost
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tổng Tiền",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = formatCurrency(subtotal),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B35)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Checkout Button
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B35)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Thanh Toán",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(65.dp))
        }
    }
}

@Composable
private fun EmptyCartScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty Cart Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color(0xFFFFF3E0),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalPizza,
                contentDescription = "Giỏ hàng trống",
                tint = Color(0xFFFF6B35),
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Giỏ hàng trống",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Hãy thêm pizza yêu thích vào giỏ hàng\nđể bắt đầu đặt hàng",
            fontSize = 16.sp,
            color = Color(0xFF757575),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { /* Navigate to menu */ },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFB700)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Xem Menu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
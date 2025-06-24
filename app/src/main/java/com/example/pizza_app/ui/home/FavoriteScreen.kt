@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.FavoriteProduct
import com.example.pizza_app.data.source.getFullImageUrl
import android.widget.Toast

@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FavoriteViewModel = viewModel()
) {
    val favoriteProducts by viewModel.favoriteProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    val primaryColor = Color(0xFFFF6B35)
    val backgroundColor = Color(0xFFF8F9FA)
    val cardColor = Color.White

    LaunchedEffect(Unit) {
        viewModel.fetchFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Header với style giống CartScreen
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Yêu thích",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (favoriteProducts.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .shadow(
                                    elevation = 4.dp,
                                    shape = CircleShape
                                )
                                .background(
                                    color = Color(0xFFFF6B35),
                                    shape = CircleShape
                                )
                                .size(28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = favoriteProducts.size.toString(),
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFF6B35), shape = CircleShape)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else if (favoriteProducts.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(
                                Color(0xFFFF6B35).copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color(0xFFFF6B35)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Chưa có sản phẩm yêu thích",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Hãy thêm sản phẩm bạn yêu thích để xem lại sau",
                        fontSize = 15.sp,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "Khám phá sản phẩm",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // Products list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteProducts, key = { it.ma_yeu_thich }) { product ->
                    FavoriteProductItem(
                        product = product,
                        onProductClick = {
                            navController.navigate("product_detail/${product.ma_san_pham}")
                        },
                        onRemoveClick = {
                            viewModel.removeFromFavorites(product.ma_san_pham) { success ->
                                if (success) {
                                    Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteProductItem(
    product: FavoriteProduct,
    onProductClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val primaryColor = Color(0xFFFF6B35)
    val cardColor = Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = cardColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            AsyncImage(
                model = getFullImageUrl(product.url_hinh_anh),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Product Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.ten_san_pham,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                product.ten_danh_muc?.let { category ->
                    Text(
                        text = category,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .background(
                                Color.Gray.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = product.gia_co_ban.formatCurrency(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                product.mo_ta?.let { description ->
                    if (description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Remove button
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color.Red.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from favorites",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Extension function để format currency
fun Double.formatCurrency(): String {
    return String.format("%,.0f₫", this)
}
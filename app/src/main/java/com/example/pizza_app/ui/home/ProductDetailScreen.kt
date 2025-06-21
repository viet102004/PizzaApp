package com.example.pizza_app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.getFullImageUrl

@Composable
fun ProductDetailScreen(
    navController: NavController,
    maSanPham: Int,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val product by viewModel.product.collectAsState()
    val imageList by viewModel.images.collectAsState()

    val selectedImage = remember { mutableStateOf<String?>(null) }
    val selectedCrust = remember { mutableStateOf("Mỏng") }
    val selectedSize = remember { mutableStateOf("M") }
    val quantity = remember { mutableStateOf(1) }
    val isFavorite = remember { mutableStateOf(false) }

    val crustOptions = listOf("Mỏng", "Vừa", "Dày")
    val sizeOptions = listOf("S", "M", "L")
    val sizeLabels = listOf("12 inch", "16 inch", "20 inch")

    val primaryColor = Color(0xFFFF6B35)
    val secondaryColor = Color(0xFFFFB700)
    val backgroundColor = Color(0xFFF8F9FA)
    val cardColor = Color.White

    LaunchedEffect(maSanPham) {
        viewModel.fetchProductDetail(maSanPham)
    }

    LaunchedEffect(imageList) {
        if (imageList.isNotEmpty()) selectedImage.value = imageList.first().url_hinh_anh
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                AsyncImage(
                    model = getFullImageUrl(selectedImage.value),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )

                Box(
                    modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.2f)
                            )
                        )
                    ).clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp).statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.size(44.dp).shadow(8.dp, CircleShape)
                            .background(cardColor, CircleShape)
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = com.example.pizza_app.R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier.size(44.dp).shadow(8.dp, CircleShape)
                            .background(cardColor, CircleShape)
                            .clickable { isFavorite.value = !isFavorite.value },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite.value) primaryColor else Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = cardColor
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Hình ảnh khác", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(imageList) { image ->
                                Box(
                                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
                                        .border(
                                            width = if (selectedImage.value == image.url_hinh_anh) 2.dp else 0.dp,
                                            color = if (selectedImage.value == image.url_hinh_anh) primaryColor else Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        ).clickable {
                                            selectedImage.value = image.url_hinh_anh
                                        }
                                ) {
                                    AsyncImage(
                                        model = getFullImageUrl(image.url_hinh_anh),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(), elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp), backgroundColor = cardColor
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(product?.ten_san_pham ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("${product?.gia_co_ban?.toInt() ?: 0}đ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                            Spacer(modifier = Modifier.weight(1f))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = secondaryColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("4.6", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(" (2k+ Reviews)", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(product?.mo_ta ?: "", fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp)
                    }
                }

                // phần chọn đế bánh, kích thước, số lượng: giữ nguyên từ code cũ của bạn...

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            elevation = 12.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            backgroundColor = cardColor
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp).navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* Thêm vào giỏ */ },
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF0F0F0)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text("Thêm vào giỏ", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }

                Button(
                    onClick = { /* Mua ngay */ },
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevation(4.dp)
                ) {
                    Text("Mua ngay", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
            }
        }
    }
}
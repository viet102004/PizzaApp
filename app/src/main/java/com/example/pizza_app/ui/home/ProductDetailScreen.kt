package com.example.pizza_app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pizza_app.R

@Composable
fun ProductDetailScreen(navController:NavController) {
    val imageList = listOf(
        R.drawable.pizza1,
        R.drawable.pizza2,
        R.drawable.pizza3,
        R.drawable.pizza4,
        R.drawable.pizza5
    )
    val selectedImage = remember { mutableStateOf(imageList[0]) }
    val selectedCrust = remember { mutableStateOf("Mỏng") }
    val selectedSize = remember { mutableStateOf("M") }

    val crustOptions = listOf("Mỏng", "Vừa", "Dày")
    val sizeOptions = listOf("12 inch", "16 inch", "20 inch")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Ảnh chính + nút Back/Tim đè lên
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Image(
                    painter = painterResource(id = selectedImage.value),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back button
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                            .clickable { navController.navigate("favorite") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Danh sách ảnh nhỏ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                imageList.forEach { imageRes ->
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { selectedImage.value = imageRes }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Pizza Phô Mai", style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold))
            Text("99.000đ", style = MaterialTheme.typography.subtitle1)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Chọn đế bánh:", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                crustOptions.forEach { crust ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (selectedCrust.value == crust) Color(0xFFFFB700) else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedCrust.value = crust }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(crust)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Chọn size:", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                sizeOptions.forEach { size ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (selectedSize.value == size) Color(0xFFFFB700) else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedSize.value = size }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(size)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("⭐ 4.6/5 (2k+ Reviews)", style = MaterialTheme.typography.body2)

            Spacer(modifier = Modifier.height(12.dp))

            Text("Pizza thơm ngon, giòn rụm, kết hợp cùng phô mai béo ngậy, xúc xích, mì ý")

            Spacer(modifier = Modifier.weight(1f))
        }

        // Nút Add to Cart + Buy Now
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { /* Add to cart */ },
                modifier = Modifier.weight(1f),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
            ) {
                Text("Add to Cart", color = Color.Black)
            }

            Button(
                onClick = { /* Buy now */ },
                modifier = Modifier.weight(1f),
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFB700))
            ) {
                Text("Buy Now", color = Color.Black)
            }
        }
    }
}

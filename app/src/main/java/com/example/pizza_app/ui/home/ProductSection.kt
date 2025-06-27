package com.example.pizza_app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pizza_app.data.model.Product
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductSection(
    products: List<Product>,
    navController: NavController,
    isLoading: Boolean = false
) {

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .width(150.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        } else {
            Text(
                text = "Ăn gì hôm nay!!!",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }

        // Products grid
        if (isLoading || products.isEmpty()) {
            SkeletonProductGrid()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductItem(product = product, navController = navController)
                }
            }
        }
    }
}

@Composable
private fun SkeletonProductGrid() {
    // Tạo hiệu ứng shimmer
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 600.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(6) { index ->
            SkeletonProductItem(
                shimmerColors = shimmerColors,
                translateAnim = translateAnim.value
            )
        }
    }
}

@Composable
private fun SkeletonProductItem(
    shimmerColors: List<Color>,
    translateAnim: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Skeleton image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = shimmerColors,
                            start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                        )
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Skeleton product name
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = shimmerColors,
                            start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                        )
                    )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Skeleton description (shorter line)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = shimmerColors,
                            start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                        )
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Skeleton price and add button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Skeleton price
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = shimmerColors,
                                start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
                                end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                            )
                        )
                )

                // Skeleton add button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = shimmerColors,
                                start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
                                end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                            )
                        )
                )
            }
        }
    }
}

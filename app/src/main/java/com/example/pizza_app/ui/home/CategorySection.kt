package com.example.pizza_app.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.source.getFullImageUrl

@Composable
fun CategorySection(
    categories: List<Category>,
    navController: NavController,
    isLoading: Boolean = false
) {
    Column {
        // Header section
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                // Skeleton for title
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )
            } else {
                Text("Danh mục", fontWeight = FontWeight.Bold)
            }

            if (isLoading) {
                // Skeleton for "Xem tất cả"
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )
            } else {
                Box(modifier = Modifier.clickable{navController.navigate("all_categories")}) {
                    Text("Xem tất cả", color = Color(0xFFFFB700), fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Categories list
        if (isLoading || categories.isEmpty()) {
            SkeletonCategoryList()
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        category = category,
                        onClick = {
                            navController.navigate("category_products/${category.ma_danh_muc}/${category.ten_danh_muc}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = getFullImageUrl(category.hinh_anh),
                contentDescription = category.ten_danh_muc,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.ten_danh_muc,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun SkeletonCategoryList() {
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

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(6) { index ->
            SkeletonCategoryItem(
                shimmerColors = shimmerColors,
                translateAnim = translateAnim.value
            )
        }
    }
}

@Composable
private fun SkeletonCategoryItem(
    shimmerColors: List<Color>,
    translateAnim: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        // Skeleton circle for category icon
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = shimmerColors,
                        start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
                        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                    )
                )
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Skeleton text for category name
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(2.dp))
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
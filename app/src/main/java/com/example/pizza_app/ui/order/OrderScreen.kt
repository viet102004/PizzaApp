@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pizza_app.R

@Composable
fun OrderScreen(navController: NavController) {
    val tabs = listOf("Chờ xác nhận", "Đang giao", "Hoàn thành", "Đã hủy")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Đơn hàng của tôi",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            actions = {
                // Icon Search với background tròn
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFB700), shape = CircleShape)
                        .clickable { /* TODO: Search */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Tìm kiếm",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

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

        // Tabs với thiết kế đẹp hơn
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Color(0xFFFFB700),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xFFFFB700),
                    height = 3.dp
                )
            },
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (selectedTabIndex == index) Color(0xFFFFB700) else Color.Gray
                        )
                    },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        // Nội dung theo tab
        when (selectedTabIndex) {
            0 -> OrderEmptyContent()
            1 -> OrderEmptyContent(
                title = "Chưa có đơn hàng đang giao",
                subtitle = "Các đơn hàng đang được giao sẽ hiện thị tại đây"
            )
            2 -> OrderEmptyContent(
                title = "Chưa có đơn hàng hoàn thành",
                subtitle = "Lịch sử các đơn hàng đã hoàn thành sẽ xuất hiện ở đây"
            )
            3 -> OrderEmptyContent(
                title = "Chưa có đơn hàng bị hủy",
                subtitle = "Các đơn hàng đã hủy sẽ được hiển thị tại đây"
            )
        }
    }
}

@Composable
fun OrderEmptyContent(
    title: String = "Quên chưa đặt món rồi nè bạn ơi!!!",
    subtitle: String = "Bạn sẽ nhìn thấy các món đang được chuẩn bị hoặc giao đi tại đây để kiểm tra đơn hàng nhanh hơn!"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon with background circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color(0xFFFFB700).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color(0xFFFFB700)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = subtitle,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // CTA Button
        Button(
            onClick = { /* TODO: Navigate to menu */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFB700)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Đặt món ngay",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Suggested items section
        Text(
            "Gợi ý món ăn",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sample suggested items
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getSuggestedItems()) { item ->
                SuggestShopItem(
                    name = item.name,
                    rating = item.rating,
                    discount = item.discount,
                    imageRes = item.imageRes
                )
            }
        }
    }
}

@Composable
fun SuggestShopItem(
    name: String,
    rating: Double,
    discount: String,
    imageRes: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Navigate to restaurant */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Restaurant image
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "$rating",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color(0xFF666666)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Discount tags
                Row {
                    AssistChip(
                        onClick = { /* TODO */ },
                        label = { Text("Giảm giá", fontSize = 12.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFFFB700).copy(alpha = 0.1f),
                            labelColor = Color(0xFFFFB700)
                        ),
                        modifier = Modifier.height(28.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    AssistChip(
                        onClick = { /* TODO */ },
                        label = { Text("Giảm $discount", fontSize = 12.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f),
                            labelColor = Color(0xFF4CAF50)
                        ),
                        modifier = Modifier.height(28.dp)
                    )
                }
            }
        }
    }
}

// Sample data
data class SuggestedItem(
    val name: String,
    val rating: Double,
    val discount: String,
    val imageRes: Int
)

private fun getSuggestedItems(): List<SuggestedItem> {
    return listOf(
        SuggestedItem(
            name = "Pizza Hut - Nguyễn Trãi",
            rating = 4.5,
            discount = "20%",
            imageRes = android.R.drawable.ic_menu_gallery
        ),
        SuggestedItem(
            name = "Domino's Pizza - Quận 1",
            rating = 4.3,
            discount = "15%",
            imageRes = android.R.drawable.ic_menu_camera
        ),
        SuggestedItem(
            name = "The Pizza Company",
            rating = 4.2,
            discount = "25%",
            imageRes = android.R.drawable.ic_menu_slideshow
        )
    )
}
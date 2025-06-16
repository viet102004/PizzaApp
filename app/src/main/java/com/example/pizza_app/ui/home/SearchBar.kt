package com.example.pizza_app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End // Căn các icon về bên phải
    ) {
        // Icon tìm kiếm
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFFFB700), shape = CircleShape)
                .clickable {
                    // Xử lý sự kiện click search
                    // Có thể mở search dialog hoặc navigate to search screen
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Tìm kiếm",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Icon yêu thích
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFFFB700), shape = CircleShape)
                .clickable {
                    // Xử lý sự kiện click favorite
                    // Navigate to favorites screen
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Yêu thích",
                tint = Color.White
            )
        }
    }
}
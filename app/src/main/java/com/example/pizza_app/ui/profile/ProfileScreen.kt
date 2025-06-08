package com.example.pizza_app.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizza_app.R
import com.example.pizza_app.ui.profile.ProfileItem

@Composable
fun ProfileScreen(
    isLoggedIn: Boolean,
    userName: String = "Hoàng Việt",
    points: Int = 1000,
    onLogout: () -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)) // màu nền hồng nhạt
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFC8C8A9)) // màu xanh rêu nhạt
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.pizza2), // Thay avatar tại đây
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(50)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Số điểm: $points Điểm",
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách các mục
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                ProfileItem(icon = Icons.Default.AccountBalanceWallet, title = "Ví") {
                    onNavigateTo("wallet")
                }
                ProfileItem(icon = Icons.Default.CardGiftcard, title = "Kho voucher") {
                    onNavigateTo("vouchers")
                }
                ProfileItem(icon = Icons.Default.Person, title = "Thông tin cá nhân") {
                    onNavigateTo("profile_details")
                }
                ProfileItem(icon = Icons.Default.LocationOn, title = "Địa chỉ") {
                    onNavigateTo("address")
                }
                ProfileItem(icon = Icons.Default.Chat, title = "Chat với người hỗ trợ") {
                    onNavigateTo("support_chat")
                }
                ProfileItem(icon = Icons.Default.Settings, title = "Cài đặt") {
                    onNavigateTo("settings")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Đăng nhập hoặc đăng xuất
        Button(
            onClick = { onLogout() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E2723)),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLoggedIn) "Đăng xuất" else "Đăng nhập",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
            .background(Color(0xFFF5F5F5))
    ) {
        TopAppBar(
            title = {

            },
            navigationIcon = {

            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
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
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
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

            // Menu Items Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

            // Logout Button
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
}
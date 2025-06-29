@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.rememberAsyncImagePainter
import com.example.pizza_app.R
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.getFullImageUrl
import com.example.pizza_app.ui.profile.ProfileItem
import com.example.pizza_app.ui.components.AuthDialog

@Composable
fun ProfileScreen(
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    onNavigateTo: (String) -> Unit
) {
    val user by UserManager.currentUser.collectAsState()
    val displayName = if (isLoggedIn) user?.ho_ten ?: "Người dùng" else "Khách"
    val avatarUrl = user?.anh_dai_dien ?: ""

    // State cho dialog
    var showAuthDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Hồ sơ của tôi",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            actions = {
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFB700), shape = CircleShape)
                        .clickable {
                            if (isLoggedIn) {
                                onNavigateTo("favorite")
                            } else {
                                // Hiển thị dialog thay vì navigate trực tiếp
                                showAuthDialog = true
                            }
                        },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Thông tin người dùng
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
                    // Avatar - luôn hiển thị
                    if (isLoggedIn && avatarUrl.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(getFullImageUrl(avatarUrl)),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = displayName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nội dung chính - khác nhau giữa khách và người dùng đã đăng nhập
            if (isLoggedIn) {
                // Card điểm thưởng nổi bật với gradient và design mới
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: Navigate to points detail */ },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFFB700).copy(alpha = 0.15f),
                                        Color(0xFFFF8F00).copy(alpha = 0.1f)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Icon với background gradient
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Color(0xFFFFB700),
                                                Color(0xFFFF8F00)
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Điểm tích lũy hiện tại",
                                    fontSize = 14.sp,
                                    color = Color(0xFF666666),
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = "1,000",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFFFB700)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "điểm",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF666666),
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Nhấn để xem chi tiết",
                                    fontSize = 12.sp,
                                    color = Color(0xFF999999)
                                )
                            }

                            // Arrow với background tròn
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        Color(0xFFFFB700).copy(alpha = 0.2f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ArrowForwardIos,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB700),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Grid menu với style mới
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        // Hàng đầu
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ProfileMenuItem(
                                icon = Icons.Default.CardGiftcard,
                                title = "Voucher",
                                modifier = Modifier.weight(1f)
                            ) { onNavigateTo("vouchers") }

                            ProfileMenuItem(
                                icon = Icons.Default.Person,
                                title = "Thông tin",
                                modifier = Modifier.weight(1f)
                            ) { onNavigateTo("profile_details") }

                            ProfileMenuItem(
                                icon = Icons.Default.LocationOn,
                                title = "Địa chỉ",
                                modifier = Modifier.weight(1f)
                            ) { onNavigateTo("address") }
                        }

//                        Spacer(modifier = Modifier.height(8.dp))
//
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                           horizontalArrangement = Arrangement.Start
//                        ) {
//                            ProfileMenuItem(
//                                icon = Icons.Default.Chat,
//                                title = "Hỗ trợ",
//                                modifier = Modifier.weight(1f)
//                            ) { onNavigateTo("support_chat") }
//
//
//                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nút đăng xuất với style mới
                OutlinedButton(
                    onClick = onLogout,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        Color(0xFF3E2723)
                    )
                ) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = Color(0xFF3E2723),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Đăng xuất",
                        fontSize = 16.sp,
                        color = Color(0xFF3E2723),
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Giao diện cho khách vãng lai - chỉ có 2 nút
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Nút đăng nhập
                    Button(
                        onClick = { onNavigateTo("login") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Đăng nhập",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Nút đăng ký
                    OutlinedButton(
                        onClick = { onNavigateTo("register") },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth(),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            Color(0xFFFFB700)
                        )
                    ) {
                        Text(
                            text = "Đăng ký",
                            fontSize = 16.sp,
                            color = Color(0xFFFFB700),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Thông tin khuyến khích đăng nhập
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB700),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Đăng nhập để trải nghiệm đầy đủ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tích điểm, nhận voucher và nhiều ưu đãi hấp dẫn khác",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    // Auth Dialog - Thêm phần này giống như OrderScreen
    AuthDialog(
        showDialog = showAuthDialog,
        onDismiss = { showAuthDialog = false },
        onLoginClick = { onNavigateTo("login") },
        onRegisterClick = { onNavigateTo("register") }
    )
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    Color(0xFFFFB700).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFB700),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UpdateNameScreen(navController: NavController) {
    var hoTen by remember { mutableStateOf("Hoàng Việt") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFC8C8A9),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        // Header với TopAppBar
        TopAppBar(
            title = {
                Text(
                    text = "Thay đổi tên",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            actions = {

            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Cập nhật tên của bạn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Trường nhập Tên
                    OutlinedTextField(
                        value = hoTen,
                        onValueChange = { hoTen = it },
                        label = { Text("Tên") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Lưu thông tin thay đổi",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
package com.example.pizza_app.ui.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UpdateNameScreen(navController: NavController) {
    var ho by remember { mutableStateOf("Hoàng") }
    var tenDem by remember { mutableStateOf("Việt") }
    var ten by remember { mutableStateOf("Việt") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header với nút back
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Thay đổi tên",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(36.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Trường nhập Họ
        OutlinedTextField(
            value = ho,
            onValueChange = { ho = it },
            label = { Text("Họ") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Trường nhập Tên đệm
        OutlinedTextField(
            value = tenDem,
            onValueChange = { tenDem = it },
            label = { Text("Tên đệm") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Trường nhập Tên
        OutlinedTextField(
            value = ten,
            onValueChange = { ten = it },
            label = { Text("Tên") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Nút xác nhận
        Button(
            onClick = {
                // TODO: Lưu lại thông tin tên
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFB700)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Lưu thông tin thay đổi", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

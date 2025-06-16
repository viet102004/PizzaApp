@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.pizza_app.ui.vouchers


import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun VoucherScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header theo style chuẩn đẹp
        TopAppBar(
            title = {
                Text(
                    text = "Mã Giảm Giá",
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
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Nhập mã & Tìm thêm
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.CardGiftcard, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nhập mã voucher")
            }
            OutlinedButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Percent, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tìm thêm voucher")
            }
        }

        Divider()

        // Danh sách mã giảm giá
        val vouchers = listOf(
            "13%" to "Mã giảm 13%",
            "20%" to "Mã giảm 20%",
            "15%" to "Mã giảm 15%",
            "12%" to "Mã giảm 12%",
            "10%" to "Mã giảm 10%",
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(vouchers) { (percent, name) ->
                VoucherItem(
                    voucherName = name,
                    discount = percent,
                    buttonLabel = "Dùng ngay"
                )
            }
        }
    }
}

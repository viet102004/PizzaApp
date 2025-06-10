package com.example.pizza_app.ui.auth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen() {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E6D3))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Header với nút back và tiêu đề
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Handle back navigation */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Quên mật khẩu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Input field cho email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        "Số di động hoặc email",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nút "Tiếp theo"
            Button(
                onClick = {
                    // Handle forgot password logic
                    println("Email entered: $email")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2D2D2D)
                )
            ) {
                Text(
                    text = "Tiếp theo",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.data.source.UserManager

@Composable
fun UpdatePhoneScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: UserUpdateViewModel = viewModel()

    val user by UserManager.currentUser.collectAsState()
    val currentPhone = user?.so_dien_thoai ?: ""
    var phoneNumber by remember(currentPhone) {
        mutableStateOf(if (currentPhone.isNotBlank()) currentPhone else "+84")
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.message.collectAsState()
    val success by viewModel.success.collectAsState()

    LaunchedEffect(success) {
        if (success) {
            Toast.makeText(context, "Cập nhật số điện thoại thành công", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
            viewModel.resetState()
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotBlank()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

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
                    text = "Thay đổi số điện thoại",
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
                        text = "Cập nhật số điện thoại của bạn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Text field nhập số điện thoại
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nút lưu
                    Button(
                        onClick = {
                            if (isValidPhoneNumber(phoneNumber)) {
                                viewModel.updatePhone(phoneNumber, context)
                            } else {
                                Toast.makeText(context, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700)),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = "Lưu thông tin thay đổi",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instruction Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📱",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Vui lòng nhập số điện thoại hợp lệ bao gồm mã quốc gia. Số này sẽ được sử dụng để xác thực và liên lạc.",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

private fun isValidPhoneNumber(phone: String): Boolean {
    val phonePattern = Regex("^[+]?[0-9]{10,15}$")
    return phone.isNotBlank() && phonePattern.matches(phone.replace(" ", ""))
}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.data.source.UserManager

@Composable
fun UpdateEmailScreen(
    navController: NavController,
    viewModel: UserUpdateViewModel = viewModel()
) {
    val context = LocalContext.current
    val user by UserManager.currentUser.collectAsState()

    // States
    val currentEmail = user?.email ?: ""
    var email by remember(currentEmail) { mutableStateOf(currentEmail) }
    var initialEmail by remember { mutableStateOf(currentEmail) }
    val focusRequester = remember { FocusRequester() }

    // State để track thay đổi
    val hasChanges = remember(initialEmail, email) {
        derivedStateOf {
            email.trim() != initialEmail.trim()
        }
    }

    // State để hiển thị dialog
    var showExitDialog by remember { mutableStateOf(false) }

    // ViewModel states
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val success by viewModel.success.collectAsState()

    // Handle success
    LaunchedEffect(success) {
        if (success) {
            Toast.makeText(context, "Cập nhật email thành công", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
            viewModel.resetState()
        }
    }

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Initialize email from user
    LaunchedEffect(user) {
        user?.email?.let { userEmail ->
            email = userEmail
            initialEmail = userEmail
        }
    }

    // Hàm xử lý khi nhấn back
    fun handleBackPress() {
        if (hasChanges.value) {
            showExitDialog = true
        } else {
            navController.navigateUp()
        }
    }

    // Dialog xác nhận thoát
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "Xác nhận thoát",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Bạn có thay đổi chưa được lưu. Bạn có chắc chắn muốn thoát không?",
                    color = Color(0xFF666666)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        navController.navigateUp()
                    }
                ) {
                    Text(
                        text = "Thoát",
                        color = Color(0xFFFF3333),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text(
                        text = "Hủy",
                        color = Color(0xFFFFB700),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFC8C8A9), Color.White)
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Thay đổi email",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { handleBackPress() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Cập nhật địa chỉ email của bạn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        ),
                        trailingIcon = {
                            if (email.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        email = ""
                                        focusRequester.requestFocus()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear text",
                                        tint = Color(0xFF666666)
                                    )
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (isValidEmail(email)) {
                                viewModel.updateEmail(email.trim(), context)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Email không hợp lệ. Vui lòng nhập email đúng định dạng.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hasChanges.value && !isLoading) Color(0xFFFFB700) else Color(0xFFCCCCCC)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading && hasChanges.value // Chỉ enable khi có thay đổi và không loading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text(
                                text = "Lưu thông tin thay đổi",
                                fontWeight = FontWeight.Bold,
                                color = if (hasChanges.value) Color.Black else Color(0xFF666666)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                    Text("📧", fontSize = 20.sp, modifier = Modifier.padding(end = 12.dp))
                    Text(
                        text = "Vui lòng nhập địa chỉ email hợp lệ. Email này sẽ được sử dụng để xác thực tài khoản và nhận thông báo.",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// Helper function để validate email
private fun isValidEmail(email: String): Boolean {
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    return email.trim().matches(emailPattern.toRegex())
}
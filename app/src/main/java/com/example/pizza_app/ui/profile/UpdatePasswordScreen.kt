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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun UpdatePasswordScreen(
    navController: NavController,
    viewModel: UserUpdateViewModel = viewModel()
) {
    val context = LocalContext.current

    // States
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    // State để track thay đổi
    val hasChanges = remember(currentPassword, newPassword, confirmPassword) {
        derivedStateOf {
            currentPassword.isNotBlank() || newPassword.isNotBlank() || confirmPassword.isNotBlank()
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
            Toast.makeText(context, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
            viewModel.resetState()
        }
    }

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                    text = "Thay đổi mật khẩu",
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
                        text = "Cập nhật mật khẩu của bạn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Mật khẩu hiện tại
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Mật khẩu hiện tại") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (currentVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Row {
                                if (currentPassword.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            currentPassword = ""
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
                                val icon = if (currentVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                                IconButton(onClick = { currentVisible = !currentVisible }) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = if (currentVisible) "Hide password" else "Show password",
                                        tint = Color(0xFF666666)
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mật khẩu mới
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Mật khẩu mới") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        visualTransformation = if (newVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Row {
                                if (newPassword.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            newPassword = ""
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
                                val icon = if (newVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                                IconButton(onClick = { newVisible = !newVisible }) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = if (newVisible) "Hide password" else "Show password",
                                        tint = Color(0xFF666666)
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Xác nhận mật khẩu
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Xác nhận mật khẩu mới") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Row {
                                if (confirmPassword.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            confirmPassword = ""
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
                                val icon = if (confirmVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                                IconButton(onClick = { confirmVisible = !confirmVisible }) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = if (confirmVisible) "Hide password" else "Show password",
                                        tint = Color(0xFF666666)
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Nút lưu
                    Button(
                        onClick = {
                            if (isValidPasswordUpdate(currentPassword, newPassword, confirmPassword)) {
                                viewModel.updatePassword(currentPassword, newPassword, context)
                            } else {
                                val errorMsg = getPasswordValidationError(currentPassword, newPassword, confirmPassword)
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hasChanges.value && !isLoading) Color(0xFFFFB700) else Color(0xFFCCCCCC)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading && hasChanges.value
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text(
                                text = "Lưu mật khẩu mới",
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
                    Text("🔒", fontSize = 20.sp, modifier = Modifier.padding(end = 12.dp))
                    Text(
                        text = "Mật khẩu mới phải có ít nhất 8 ký tự và bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt để đảm bảo an toàn.",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// Helper functions để validate mật khẩu
private fun isValidPasswordUpdate(currentPassword: String, newPassword: String, confirmPassword: String): Boolean {
    return currentPassword.isNotBlank() &&
            newPassword.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            newPassword == confirmPassword &&
            isStrongPassword(newPassword)
}

private fun isStrongPassword(password: String): Boolean {
    val minLength = 8
    val hasUpperCase = password.any { it.isUpperCase() }
    val hasLowerCase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }

    return password.length >= minLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
}

private fun getPasswordValidationError(currentPassword: String, newPassword: String, confirmPassword: String): String {
    return when {
        currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() ->
            "Vui lòng điền đầy đủ thông tin"

        newPassword != confirmPassword ->
            "Mật khẩu xác nhận không khớp"

        newPassword.length < 8 ->
            "Mật khẩu mới phải có ít nhất 8 ký tự"

        !newPassword.any { it.isUpperCase() } ->
            "Mật khẩu mới phải chứa ít nhất 1 chữ hoa"

        !newPassword.any { it.isLowerCase() } ->
            "Mật khẩu mới phải chứa ít nhất 1 chữ thường"

        !newPassword.any { it.isDigit() } ->
            "Mật khẩu mới phải chứa ít nhất 1 chữ số"

        !newPassword.any { !it.isLetterOrDigit() } ->
            "Mật khẩu mới phải chứa ít nhất 1 ký tự đặc biệt"

        else -> "Mật khẩu không hợp lệ"
    }
}
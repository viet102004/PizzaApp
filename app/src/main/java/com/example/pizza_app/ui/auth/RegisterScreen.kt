package com.example.pizza_app.ui.auth

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    val viewModel: RegisterViewModel = viewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val primaryColor = Color(0xFFFF6B35)
    val lightOrange = Color(0xFFFFE4D6)

    LaunchedEffect(registerSuccess) {
        registerSuccess?.let {
            // Hiển thị thông báo thành công
            Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()

            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    LaunchedEffect(errorMessage) {
        showError = errorMessage.isNotBlank()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                            .background(primaryColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                println("TopAppBar back button clicked!")
                                navController.popBackStack()
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Quay lại",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(lightOrange, Color.White)))
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Bỏ Spacer cho back button vì đã có TopAppBar

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Đăng ký", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                    Text("Tạo tài khoản mới", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Họ và tên", fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = primaryColor) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                cursorColor = primaryColor
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = primaryColor) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                cursorColor = primaryColor
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = {
                                // Chỉ cho phép nhập số và giới hạn độ dài
                                if (it.all { char -> char.isDigit() || char == '+' || char == ' ' || char == '-' } && it.length <= 15) {
                                    phone = it
                                }
                            },
                            label = { Text("Số điện thoại", fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = primaryColor) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                cursorColor = primaryColor
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                            placeholder = { Text("", color = Color.Gray) },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Mật khẩu", fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = primaryColor) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = primaryColor
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                cursorColor = primaryColor
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Xác nhận mật khẩu", fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = primaryColor) },
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = primaryColor
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                cursorColor = primaryColor
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (showError && errorMessage.isNotBlank()) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )
                        }

                        Button(
                            onClick = {
                                // Kiểm tra validation trước khi gọi API
                                if (isValidRegistration(name.trim(), email.trim(), phone.trim(), password, confirmPassword)) {
                                    viewModel.register(
                                        name.trim(),
                                        email.trim(),
                                        phone.trim(),
                                        password,
                                        confirmPassword,
                                        context
                                    )
                                } else {
                                    val errorMsg = getRegistrationValidationError(name.trim(), email.trim(), phone.trim(), password, confirmPassword)
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            enabled = !isLoading && name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                        ) {
                            if (isLoading) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Đang đăng ký...")
                                }
                            } else {
                                Text("Đăng ký", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text("Đã có tài khoản? ", fontSize = 14.sp, color = Color.Gray)
                    TextButton(onClick = { navController.navigate("login") }) {
                        Text("Đăng nhập", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = primaryColor)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Helper functions để validate đăng ký
private fun isValidRegistration(name: String, email: String, phone: String, password: String, confirmPassword: String): Boolean {
    return name.isNotBlank() &&
            email.isNotBlank() &&
            phone.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            password == confirmPassword &&
            isStrongPassword(password) &&
            isValidEmail(email) &&
            isValidPhone(phone)
}

private fun isStrongPassword(password: String): Boolean {
    val minLength = 8
    val hasUpperCase = password.any { it.isUpperCase() }
    val hasLowerCase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }

    return password.length >= minLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun isValidPhone(phone: String): Boolean {
    return phone.length >= 10 && phone.all { it.isDigit() || it == '+' || it == ' ' || it == '-' }
}

private fun getRegistrationValidationError(name: String, email: String, phone: String, password: String, confirmPassword: String): String {
    return when {
        name.isBlank() -> "Vui lòng nhập họ và tên"
        email.isBlank() -> "Vui lòng nhập email"
        !isValidEmail(email) -> "Email không hợp lệ"
        phone.isBlank() -> "Vui lòng nhập số điện thoại"
        !isValidPhone(phone) -> "Số điện thoại không hợp lệ"
        password.isBlank() -> "Vui lòng nhập mật khẩu"
        confirmPassword.isBlank() -> "Vui lòng xác nhận mật khẩu"
        password != confirmPassword -> "Mật khẩu xác nhận không khớp"
        password.length < 8 -> "Mật khẩu phải có ít nhất 8 ký tự"
        !password.any { it.isUpperCase() } -> "Mật khẩu phải chứa ít nhất 1 chữ hoa"
        !password.any { it.isLowerCase() } -> "Mật khẩu phải chứa ít nhất 1 chữ thường"
        !password.any { it.isDigit() } -> "Mật khẩu phải chứa ít nhất 1 chữ số"
        !password.any { !it.isLetterOrDigit() } -> "Mật khẩu phải chứa ít nhất 1 ký tự đặc biệt"
        else -> "Thông tin đăng ký không hợp lệ"
    }
}
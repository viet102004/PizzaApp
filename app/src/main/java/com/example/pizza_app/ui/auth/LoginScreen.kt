package com.example.pizza_app.ui.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@Composable
fun LoginScreen(navController: NavController) {

    val viewModel: LoginViewModel = viewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val loginUser by viewModel.loginSuccess.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    // TextFieldValue để control scroll position
    var emailTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Hàm để scroll text trong TextField với cursor visible
    fun scrollEmailText(deltaX: Float) {
        val currentText = emailTextFieldValue.text
        if (currentText.isNotEmpty()) {
            val currentSelection = emailTextFieldValue.selection.start
            val sensitivity = 30f // Tăng độ nhạy để dễ điều khiển hơn
            val newPosition = (currentSelection + (deltaX / sensitivity).toInt()).coerceIn(0, currentText.length)
            emailTextFieldValue = emailTextFieldValue.copy(
                selection = TextRange(newPosition, newPosition) // Cursor tại vị trí mới
            )
        }
    }

    fun scrollPasswordText(deltaX: Float) {
        val currentText = passwordTextFieldValue.text
        if (currentText.isNotEmpty()) {
            val currentSelection = passwordTextFieldValue.selection.start
            val sensitivity = 30f // Tăng độ nhạy để dễ điều khiển hơn
            val newPosition = (currentSelection + (deltaX / sensitivity).toInt()).coerceIn(0, currentText.length)
            passwordTextFieldValue = passwordTextFieldValue.copy(
                selection = TextRange(newPosition, newPosition) // Cursor tại vị trí mới
            )
        }
    }

    val primaryColor = Color(0xFFFF6B35)
    val lightOrange = Color(0xFFFFE4D6)

    LaunchedEffect(loginUser) {
        println("LaunchedEffect triggered, loginUser: $loginUser")
        loginUser?.let {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Gán lỗi nếu có
    LaunchedEffect(errorMessage) {
        showError = errorMessage.isNotBlank()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(lightOrange, Color.White))
            )
    ) {

        Box(
            modifier = Modifier
                .padding(top = 45.dp, start = 16.dp)
                .size(40.dp)
                .background(primaryColor, CircleShape)
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Quay lại",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(primaryColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.LocalPizza,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Pizza Kimchi", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                Text("Chào mừng bạn trở lại", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Form đăng nhập
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    OutlinedTextField(
                        value = emailTextFieldValue,
                        onValueChange = {
                            emailTextFieldValue = it
                            email = it.text
                        },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = primaryColor)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures(
                                    onDragStart = {
                                        // Khi bắt đầu drag, đảm bảo cursor hiển thị
                                    },
                                    onDragEnd = {
                                        // Khi kết thúc drag, giữ cursor tại vị trí hiện tại
                                    }
                                ) { _, dragAmount ->
                                    scrollEmailText(-dragAmount)
                                }
                            }
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    // Khi mất focus, reset cursor về đầu text
                                    emailTextFieldValue = emailTextFieldValue.copy(
                                        selection = TextRange(0)
                                    )
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor,
                            cursorColor = primaryColor
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp),
                        maxLines = 1,
                        readOnly = false
                    )

                    OutlinedTextField(
                        value = passwordTextFieldValue,
                        onValueChange = {
                            passwordTextFieldValue = it
                            password = it.text
                        },
                        label = { Text("Mật khẩu") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = primaryColor)
                        },
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
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures(
                                    onDragStart = {
                                        // Khi bắt đầu drag, đảm bảo cursor hiển thị
                                    },
                                    onDragEnd = {
                                        // Khi kết thúc drag, giữ cursor tại vị trí hiện tại
                                    }
                                ) { _, dragAmount ->
                                    scrollPasswordText(-dragAmount)
                                }
                            }
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    // Khi mất focus, reset cursor về đầu text
                                    passwordTextFieldValue = passwordTextFieldValue.copy(
                                        selection = TextRange(0)
                                    )
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor,
                            cursorColor = primaryColor
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp),
                        maxLines = 1,
                        readOnly = false
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { navController.navigate("forgot_password") }) {
                            Text("Quên mật khẩu?", color = primaryColor, fontSize = 14.sp)
                        }
                    }

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
                            viewModel.login(email, password, context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
                    ) {
                        if (isLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Đang đăng nhập...")
                            }
                        } else {
                            Text("Đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Chưa có tài khoản? ", fontSize = 14.sp, color = Color.Gray)
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Đăng ký", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = primaryColor)
                }
            }
        }
    }
}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.data.source.UserManager
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun UpdateDOBScreen(
    navController: NavController,
    viewModel: UserUpdateViewModel = viewModel()
) {
    val context = LocalContext.current
    val user by UserManager.currentUser.collectAsState()

    // States
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var initialDate by remember { mutableStateOf(getCurrentDate()) }

    // State để track thay đổi
    val hasChanges = remember(initialDate, selectedDate) {
        derivedStateOf {
            selectedDate != initialDate
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
            Toast.makeText(context, "Cập nhật ngày sinh thành công", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
            viewModel.resetState()
        }
    }

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Initialize date from user
    LaunchedEffect(user) {
        user?.ngay_sinh?.let { dateStr ->
            try {
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val userDate = format.parse(dateStr) ?: Date()
                selectedDate = userDate
                initialDate = userDate // Cập nhật initialDate với ngày sinh từ user
            } catch (e: Exception) {
                // Keep current date if parsing fails
            }
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
                    text = "Thay đổi ngày sinh",
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
                        text = "Cập nhật ngày sinh của bạn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showDatePicker = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = Color(0xFFF8F9FA)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color(0xFFFFB700)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Ngày sinh",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                                Text(
                                    text = formatDate(selectedDate),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (isValidBirthDate(selectedDate)) {
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val dateString = dateFormat.format(selectedDate)
                                viewModel.updateBirthDate(dateString, context)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Ngày sinh không được lớn hơn ngày hiện tại",
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
                    Text("🎂", fontSize = 20.sp, modifier = Modifier.padding(end = 12.dp))
                    Text(
                        text = "Ngày sinh giúp chúng tôi gửi cho bạn những ưu đãi đặc biệt và chúc mừng sinh nhật. Lưu ý: Ngày sinh không được lớn hơn ngày hiện tại.",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }

    // Date picker
    if (showDatePicker) {
        SimpleDatePickerDialog(
            currentDate = selectedDate,
            onDateSelected = { newDate ->
                if (isValidBirthDate(newDate)) {
                    selectedDate = newDate
                    showDatePicker = false
                } else {
                    Toast.makeText(
                        context,
                        "Ngày sinh không được lớn hơn ngày hiện tại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDatePickerDialog(
    currentDate: Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate.time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Chỉ cho phép chọn ngày <= ngày hiện tại
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Date(millis)
                        if (isValidBirthDate(selectedDate)) {
                            onDateSelected(selectedDate)
                        }
                    }
                }
            ) {
                Text(
                    text = "Xác nhận",
                    color = Color(0xFFFFB700),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Hủy",
                    color = Color(0xFF666666)
                )
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

// Helper functions
private fun getCurrentDate(): Date = Date()

private fun formatDate(date: Date): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

// Validation function để kiểm tra ngày sinh hợp lệ
private fun isValidBirthDate(birthDate: Date): Boolean {
    val currentDate = Date()
    // Kiểm tra ngày sinh không được lớn hơn ngày hiện tại
    return !birthDate.after(currentDate)
}
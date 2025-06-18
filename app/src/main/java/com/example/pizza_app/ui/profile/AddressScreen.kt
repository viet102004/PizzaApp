package com.example.pizza_app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    navController: NavController,
    onBackClick: () -> Unit = {},
    onSaveClick: (AddressData) -> Unit = {}
) {
    var fullName by remember { mutableStateOf(TextFieldValue("")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var province by remember { mutableStateOf(TextFieldValue("")) }
    var district by remember { mutableStateOf(TextFieldValue("")) }
    var ward by remember { mutableStateOf(TextFieldValue("")) }
    var streetAddress by remember { mutableStateOf(TextFieldValue("")) }
    var addressType by remember { mutableStateOf("Nhà riêng") }
    var isDefault by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }

    val addressTypes = listOf("Nhà riêng", "Văn phòng", "Khác")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F8F8),
                        Color(0xFFF5F5F5)
                    )
                )
            )
    ) {
        // Header giống WalletScreen
        TopAppBar(
            title = {
                Text(
                    text = "Sửa địa chỉ",
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

        Spacer(modifier = Modifier.height(24.dp))

        // Main Card Content giống WalletScreen
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header với icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Thông tin địa chỉ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }

                // Họ và tên
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = {
                        Text(
                            "Họ và tên",
                            color = Color(0xFF666666)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE53935),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                // Số điện thoại
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = {
                        Text(
                            "Số điện thoại",
                            color = Color(0xFF666666)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE53935),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                // Tỉnh/Thành phố
                OutlinedTextField(
                    value = province,
                    onValueChange = { province = it },
                    label = {
                        Text(
                            "Tỉnh/Thành phố",
                            color = Color(0xFF666666)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE53935),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                // Quận/Huyện
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = {
                        Text(
                            "Quận/Huyện",
                            color = Color(0xFF666666)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE53935),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                // Phường/Xã
                OutlinedTextField(
                    value = ward,
                    onValueChange = { ward = it },
                    label = {
                        Text(
                            "Phường/Xã",
                            color = Color(0xFF666666)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE53935),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                // Địa chỉ cụ thể
                OutlinedTextField(
                    value = streetAddress,
                    onValueChange = { streetAddress = it },
                    label = {
                        Text(
                            "Số nhà, tên đường",
                            color = Color(0xFF666666)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    maxLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE53935),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                // Loại địa chỉ
                Text(
                    "Loại địa chỉ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    addressTypes.forEach { type ->
                        FilterChip(
                            selected = addressType == type,
                            onClick = { addressType = type },
                            label = {
                                Text(
                                    type,
                                    fontSize = 12.sp,
                                    color = if (addressType == type) Color.White else Color(0xFF666666)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE53935),
                                containerColor = Color(0xFFF5F5F5)
                            )
                        )
                    }
                }

                // Đặt làm địa chỉ mặc định
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDefault,
                        onCheckedChange = { isDefault = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFE53935)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Đặt làm địa chỉ mặc định",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }

                // Nút lưu
                Button(
                    onClick = {
                        val addressData = AddressData(
                            fullName = fullName.text,
                            phoneNumber = phoneNumber.text,
                            province = province.text,
                            district = district.text,
                            ward = ward.text,
                            streetAddress = streetAddress.text,
                            addressType = addressType,
                            isDefault = isDefault
                        )

                        if (validateAddress(addressData)) {
                            showSaveDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Lưu địa chỉ",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Dialog xác nhận lưu
        if (showSaveDialog) {
            AlertDialog(
                onDismissRequest = { showSaveDialog = false },
                title = {
                    Text(
                        "Xác nhận",
                        color = Color.Black
                    )
                },
                text = {
                    Text(
                        "Bạn có muốn lưu địa chỉ này không?",
                        color = Color(0xFF666666)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSaveDialog = false
                            onSaveClick(
                                AddressData(
                                    fullName = fullName.text,
                                    phoneNumber = phoneNumber.text,
                                    province = province.text,
                                    district = district.text,
                                    ward = ward.text,
                                    streetAddress = streetAddress.text,
                                    addressType = addressType,
                                    isDefault = isDefault
                                )
                            )
                        }
                    ) {
                        Text(
                            "Lưu",
                            color = Color(0xFFE53935)
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showSaveDialog = false }
                    ) {
                        Text(
                            "Hủy",
                            color = Color(0xFF666666)
                        )
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

// Data class để lưu thông tin địa chỉ
data class AddressData(
    val fullName: String,
    val phoneNumber: String,
    val province: String,
    val district: String,
    val ward: String,
    val streetAddress: String,
    val addressType: String,
    val isDefault: Boolean
)

// Hàm validate địa chỉ
private fun validateAddress(address: AddressData): Boolean {
    return address.fullName.isNotBlank() &&
            address.phoneNumber.isNotBlank() &&
            address.province.isNotBlank() &&
            address.district.isNotBlank() &&
            address.ward.isNotBlank() &&
            address.streetAddress.isNotBlank()
}
package com.example.pizza_app.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddressScreen(navController: NavController,
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
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    "Sửa địa chỉ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                }
            },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header với icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Thông tin địa chỉ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Họ và tên
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Họ và tên") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true
            )

            // Số điện thoại
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Số điện thoại") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true
            )

            // Tỉnh/Thành phố
            OutlinedTextField(
                value = province,
                onValueChange = { province = it },
                label = { Text("Tỉnh/Thành phố") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true
            )

            // Quận/Huyện
            OutlinedTextField(
                value = district,
                onValueChange = { district = it },
                label = { Text("Quận/Huyện") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true
            )

            // Phường/Xã
            OutlinedTextField(
                value = ward,
                onValueChange = { ward = it },
                label = { Text("Phường/Xã") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true
            )

            // Địa chỉ cụ thể
            OutlinedTextField(
                value = streetAddress,
                onValueChange = { streetAddress = it },
                label = { Text("Số nhà, tên đường") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                maxLines = 2
            )

            // Loại địa chỉ
            Text(
                "Loại địa chỉ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
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
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            type,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
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
                    onCheckedChange = { isDefault = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Đặt làm địa chỉ mặc định",
                    fontSize = 14.sp
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
                    backgroundColor = MaterialTheme.colors.primary
                )
            ) {
                Text(
                    "Lưu địa chỉ",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Dialog xác nhận lưu
            if (showSaveDialog) {
                AlertDialog(
                    onDismissRequest = { showSaveDialog = false },
                    title = { Text("Xác nhận") },
                    text = { Text("Bạn có muốn lưu địa chỉ này không?") },
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
                            Text("Lưu")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showSaveDialog = false }
                        ) {
                            Text("Hủy")
                        }
                    }
                )
            }
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
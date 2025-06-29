@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.data.model.Province
import com.example.pizza_app.data.model.District
import com.example.pizza_app.data.model.Ward
import com.example.pizza_app.ui.components.ProvincePickerDialog
import com.example.pizza_app.ui.components.DistrictPickerDialog
import com.example.pizza_app.ui.components.WardPickerDialog

@Composable
fun AddAddressScreen(
    navController: NavController,
    viewModel: AddressViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Collect states from ViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val success by viewModel.success.collectAsState()

    // Form states
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }
    var selectedWard by remember { mutableStateOf<Ward?>(null) }
    var streetAddress by remember { mutableStateOf("") }
    var isDefault by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }

    // Dialog states
    var showProvinceDialog by remember { mutableStateOf(false) }
    var showDistrictDialog by remember { mutableStateOf(false) }
    var showWardDialog by remember { mutableStateOf(false) }

    // Error states
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var provinceError by remember { mutableStateOf(false) }
    var districtError by remember { mutableStateOf(false) }
    var wardError by remember { mutableStateOf(false) }
    var streetAddressError by remember { mutableStateOf(false) }

    // Show dialogs
    if (showProvinceDialog) {
        ProvincePickerDialog(
            onDismiss = { showProvinceDialog = false },
            onProvinceSelected = { province ->
                selectedProvince = province
                selectedDistrict = null // Reset district when province changes
                selectedWard = null     // Reset ward when province changes
                provinceError = false
                showProvinceDialog = false
            }
        )
    }

    if (showDistrictDialog && selectedProvince != null) {
        DistrictPickerDialog(
            province = selectedProvince!!,
            onDismiss = { showDistrictDialog = false },
            onDistrictSelected = { district ->
                selectedDistrict = district
                selectedWard = null // Reset ward when district changes
                districtError = false
                showDistrictDialog = false
            }
        )
    }

    if (showWardDialog && selectedProvince != null && selectedDistrict != null) {
        WardPickerDialog(
            province = selectedProvince!!,
            district = selectedDistrict!!,
            onDismiss = { showWardDialog = false },
            onWardSelected = { ward ->
                selectedWard = ward
                wardError = false
                showWardDialog = false
            }
        )
    }

    // Show loading dialog
    if (isLoading) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Đang xử lý") },
            text = { Text("Vui lòng chờ...") },
            confirmButton = { },
            dismissButton = { }
        )
    }

    // Handle success/error messages
    LaunchedEffect(success, message) {
        if (success && message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            navController.popBackStack()
        } else if (!success && message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Địa chỉ mới",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color(0xFFFFB700).copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color(0xFFFFB700),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Contact Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Thông tin liên hệ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Name Field
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = false
                        },
                        label = { Text("Họ và tên") },
                        placeholder = { Text("Nhập họ và tên") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = if (nameError) Color(0xFFE74C3C) else Color(0xFFFFB700)
                            )
                        },
                        isError = nameError,
                        supportingText = if (nameError) {
                            { Text("Vui lòng nhập họ và tên", color = Color(0xFFE74C3C)) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Phone Field
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            // Only allow numbers
                            if (it.all { char -> char.isDigit() }) {
                                phone = it
                                phoneError = false
                            }
                        },
                        label = { Text("Số điện thoại") },
                        placeholder = { Text("Nhập số điện thoại") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = null,
                                tint = if (phoneError) Color(0xFFE74C3C) else Color(0xFFFFB700)
                            )
                        },
                        isError = phoneError,
                        supportingText = if (phoneError) {
                            { Text("Vui lòng nhập số điện thoại hợp lệ", color = Color(0xFFE74C3C)) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        )
                    )
                }
            }

            // Address Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Địa chỉ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Province Selector
                    LocationSelector(
                        value = selectedProvince?.name ?: "",
                        placeholder = "Chọn Tỉnh/Thành phố",
                        isError = provinceError,
                        onClick = {
                            showProvinceDialog = true
                        }
                    )

                    if (provinceError) {
                        Text(
                            text = "Vui lòng chọn tỉnh/thành phố",
                            color = Color(0xFFE74C3C),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // District Selector
                    LocationSelector(
                        value = selectedDistrict?.name ?: "",
                        placeholder = "Chọn Quận/Huyện",
                        isError = districtError,
                        enabled = selectedProvince != null,
                        onClick = {
                            if (selectedProvince != null) {
                                showDistrictDialog = true
                            }
                        }
                    )

                    if (districtError) {
                        Text(
                            text = "Vui lòng chọn quận/huyện",
                            color = Color(0xFFE74C3C),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ward Selector
                    LocationSelector(
                        value = selectedWard?.name ?: "",
                        placeholder = "Chọn Phường/Xã",
                        isError = wardError,
                        enabled = selectedDistrict != null,
                        onClick = {
                            if (selectedDistrict != null) {
                                showWardDialog = true
                            }
                        }
                    )

                    if (wardError) {
                        Text(
                            text = "Vui lòng chọn phường/xã",
                            color = Color(0xFFE74C3C),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Street Address Field
                    OutlinedTextField(
                        value = streetAddress,
                        onValueChange = {
                            streetAddress = it
                            streetAddressError = false
                        },
                        label = { Text("Tên đường, Tòa nhà, Số nhà") },
                        placeholder = { Text("Nhập địa chỉ chi tiết") },
                        isError = streetAddressError,
                        supportingText = if (streetAddressError) {
                            { Text("Vui lòng nhập địa chỉ chi tiết", color = Color(0xFFE74C3C)) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        ),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notes Field
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Ghi chú (không bắt buộc)") },
                        placeholder = { Text("Thêm ghi chú cho địa chỉ") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB700),
                            focusedLabelColor = Color(0xFFFFB700)
                        ),
                        minLines = 2
                    )
                }
            }

            // Settings Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Cài đặt",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    SwitchRow(
                        title = "Đặt làm địa chỉ mặc định",
                        checked = isDefault,
                        onCheckedChange = { isDefault = it }
                    )
                }
            }

            // Save Button
            Button(
                onClick = {
                    // Validate form
                    var hasError = false

                    if (name.isBlank()) {
                        nameError = true
                        hasError = true
                    }

                    if (phone.isBlank() || phone.length < 10) {
                        phoneError = true
                        hasError = true
                    }

                    if (selectedProvince == null) {
                        provinceError = true
                        hasError = true
                    }

                    if (selectedDistrict == null) {
                        districtError = true
                        hasError = true
                    }

                    if (selectedWard == null) {
                        wardError = true
                        hasError = true
                    }

                    if (streetAddress.isBlank()) {
                        streetAddressError = true
                        hasError = true
                    }

                    if (!hasError) {
                        viewModel.createAddress(
                            name = name,
                            phone = phone,
                            street = streetAddress,
                            ward = selectedWard!!.name,
                            district = selectedDistrict!!.name,
                            province = selectedProvince!!.name,
                            isDefault = isDefault,
                            notes = notes
                        )
                    } else {
                        Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB700)
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "HOÀN THÀNH",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun LocationSelector(
    value: String,
    placeholder: String,
    isError: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                isError -> Color(0xFFE74C3C)
                !enabled -> Color(0xFFE0E0E0)
                else -> Color(0xFFE0E0E0)
            }
        ),
        color = if (enabled) Color.White else Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (value.isNotEmpty()) value else placeholder,
                fontSize = 16.sp,
                color = when {
                    !enabled -> Color(0xFF999999)
                    value.isEmpty() -> Color(0xFF999999)
                    else -> Color(0xFF2D3436)
                },
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = if (enabled) Color(0xFF999999) else Color(0xFFCCCCCC),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color(0xFF2D3436),
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFFFB700),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}

package com.example.pizza_app.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizza_app.data.model.CartItem
import com.example.pizza_app.data.model.AddressInfo
import com.example.pizza_app.ui.cart.CartItemCard
import com.example.pizza_app.ui.profile.AddressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    addressViewModel: AddressViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.totalAmount.collectAsState()

    // Address states
    val isAddressLoading by addressViewModel.isLoading.collectAsState()
    val addressMessage by addressViewModel.message.collectAsState()


    var selectedDiscount by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("Tiền mặt") }
    var orderNote by remember { mutableStateOf("") }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showDiscountDialog by remember { mutableStateOf(false) }
    var discountAmount by remember { mutableStateOf(0.0) }


    val viewModel = remember { PayViewModel() }
    val isLoading by viewModel.isLoading.collectAsState()
    val orderResult by viewModel.orderResult.collectAsState()
    val errorMessage by viewModel.message.collectAsState()

    val addressList by addressViewModel.addressList.collectAsState()
    var selectedAddress by remember { mutableStateOf<AddressInfo?>(null) }
    val selectedAddressId = selectedAddress?.ma_thong_tin_giao_hang




    val paymentMethods = listOf(
        PaymentMethod("Tiền mặt", "💰", "Thanh toán khi nhận hàng"),
        PaymentMethod("MoMo", "📱", "Ví điện tử MoMo"),
        PaymentMethod("ZaloPay", "⚡", "Ví điện tử ZaloPay"),
        PaymentMethod("Thẻ tín dụng", "💳", "Visa, Master, JCB")
    )

    val discountCodes = mapOf(
        "GIAM10" to DiscountCode("GIAM10", "Giảm 10K", 10000.0, "Cho đơn từ 50K"),
        "GIAM20" to DiscountCode("GIAM20", "Giảm 20K", 20000.0, "Cho đơn từ 100K"),
        "FREESHIP" to DiscountCode("FREESHIP", "Miễn phí ship", 15000.0, "Cho đơn từ 80K")
    )

    // Load data when screen opens
    LaunchedEffect(Unit) {
        cartViewModel.fetchCartItems()
        addressViewModel.getDeliveryAddresses()
    }

    // Set default address when address list is loaded
    LaunchedEffect(addressList) {
        if (selectedAddress == null && addressList.isNotEmpty()) {
            // Find default address or use first address
            val defaultAddress = addressList.find { it.la_dia_chi_mac_dinh == 1 }
                ?: addressList.firstOrNull()
            selectedAddress = defaultAddress
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Thanh toán",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6B35)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 120.dp), // Space for bottom section
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                // 1. Địa chỉ giao hàng - Updated to show selected address
                item {
                    ModernCard(
                        icon = Icons.Default.LocationOn,
                        iconColor = Color(0xFF4CAF50),
                        title = "Địa chỉ giao hàng",
                        action = "Thay đổi",
                        onActionClick = {
                            addressViewModel.getDeliveryAddresses() // Refresh addresses
                            showAddressDialog = true
                        }
                    ) {
                        if (selectedAddress != null) {
                            Column {
                                Text(
                                    text = selectedAddress!!.ten_nguoi_nhan ?: "",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1A1A1A)
                                )
                                Text(
                                    text = selectedAddress!!.so_dien_thoai_nguoi_nhan ?: "",
                                    fontSize = 13.sp,
                                    color = Color(0xFF666666),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Text(
                                    text = buildString {
                                        selectedAddress!!.so_duong?.let { append(it) }
                                        selectedAddress!!.phuong_xa?.let { append(", $it") }
                                        selectedAddress!!.quan_huyen?.let { append(", $it") }
                                        selectedAddress!!.tinh_thanh_pho?.let { append(", $it") }
                                    },
                                    fontSize = 13.sp,
                                    color = Color(0xFF666666),
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                if (selectedAddress!!.la_dia_chi_mac_dinh == 1) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                                        modifier = Modifier.padding(top = 6.dp)
                                    ) {
                                        Text(
                                            text = "Địa chỉ mặc định",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF4CAF50),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = if (addressList.isEmpty()) "Chưa có địa chỉ giao hàng" else "Đang tải địa chỉ...",
                                fontSize = 14.sp,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                }

                // 2. Danh sách sản phẩm - Enhanced header
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sản phẩm đã chọn",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1A1A)
                                )
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color(0xFFFF6B35).copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = "${cartItems.size} món",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFFF6B35),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Items trong giỏ hàng - Keep original
                items(cartItems, key = { it.id }) { item ->
                    PaymentItemCard(item = item)
                }

                // 3. Tổng tiền sản phẩm - Enhanced design
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Chi tiết thanh toán",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A1A),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            PaymentDetailRow("Tạm tính", formatCurrency(cartTotal))
                            PaymentDetailRow("Phí vận chuyển", "Miễn phí", valueColor = Color(0xFF4CAF50))

                            if (selectedDiscount.isNotEmpty()) {
                                PaymentDetailRow(
                                    "Giảm giá ($selectedDiscount)",
                                    "-${formatCurrency(discountAmount)}",
                                    valueColor = Color(0xFFFF6B35)
                                )
                            }

                            Divider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = Color(0xFFE0E0E0)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tổng cộng",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1A1A)
                                )
                                Text(
                                    text = formatCurrency(cartTotal - discountAmount),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF6B35)
                                )
                            }
                        }
                    }
                }

                // 4. Mã giảm giá - Enhanced design
                item {
                    ModernCard(
                        icon = Icons.Default.LocalOffer,
                        iconColor = Color(0xFFFF6B35),
                        title = "Mã giảm giá",
                        action = "Chọn mã",
                        onActionClick = { showDiscountDialog = true }
                    ) {
                        if (selectedDiscount.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = selectedDiscount,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF4CAF50)
                                    )
                                    Text(
                                        text = discountCodes[selectedDiscount]?.description ?: "",
                                        fontSize = 12.sp,
                                        color = Color(0xFF888888)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF4CAF50).copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = "-${formatCurrency(discountAmount)}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CAF50),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Chọn mã giảm giá để tiết kiệm hơn",
                                fontSize = 14.sp,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                }

                // 5. Phương thức thanh toán - Enhanced design
                item {
                    ModernCard(
                        icon = Icons.Default.Payment,
                        iconColor = Color(0xFF2196F3),
                        title = "Phương thức thanh toán"
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            paymentMethods.forEach { method ->
                                PaymentMethodItem(
                                    method = method,
                                    isSelected = selectedPaymentMethod == method.name,
                                    onSelect = { selectedPaymentMethod = method.name }
                                )
                            }
                        }
                    }
                }

                // 6. Ghi chú - Enhanced design
                item {
                    ModernCard(
                        icon = Icons.Default.Note,
                        iconColor = Color(0xFF9C27B0),
                        title = "Ghi chú đơn hàng"
                    ) {
                        OutlinedTextField(
                            value = orderNote,
                            onValueChange = { orderNote = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    "Nhập ghi chú cho đơn hàng...",
                                    color = Color(0xFF999999)
                                )
                            },
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFF6B35),
                                cursorColor = Color(0xFFFF6B35),
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }

            // 7. Bottom section - Enhanced design
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tổng thanh toán",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = formatCurrency(cartTotal - discountAmount),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6B35)
                            )
                            if (discountAmount > 0) {
                                Text(
                                    text = "Tiết kiệm ${formatCurrency(discountAmount)}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.datHang(
                                    maThongTinGiaoHang = selectedAddressId!!.toInt(),
                                    phuongThucThanhToan = selectedPaymentMethod,
                                    maGiamGia = null,
                                    ghiChu = "noteText",
                                    thoiGianGiaoDuKien = "2025-07-01 15:00:00" // Nếu có chọn lịch, format yyyy-MM-dd HH:mm:ss
                                )
                            },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Text("Đặt hàng")
                            }
                        }

                    }
                }
            }
        }
    }

    if (showAddressDialog) {
        AlertDialog(
            onDismissRequest = { showAddressDialog = false },
            title = {
                Text(
                    "Chọn địa chỉ giao hàng",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    if (isAddressLoading) {
                        // Show loading indicator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFFFF6B35)
                            )
                        }
                    } else if (addressList.isEmpty()) {
                        // Show empty state
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Chưa có địa chỉ giao hàng",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    } else {
                        // Show address list
                        addressList.forEach { address ->
                            AddressRadioItem(
                                address = address,
                                isSelected = selectedAddress?.ma_thong_tin_giao_hang == address.ma_thong_tin_giao_hang,
                                onSelect = {
                                    selectedAddress = address
                                    showAddressDialog = false
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Nút Thêm địa chỉ mới
                    OutlinedButton(
                        onClick = {
                            showAddressDialog = false
                            navController.navigate("add_address")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp,
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                listOf(Color(0xFFFF6B35), Color(0xFFFFB700))
                            )
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFFF6B35)
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Thêm địa chỉ mới")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showAddressDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFF6B35)
                    )
                ) {
                    Text("Đóng", fontWeight = FontWeight.Medium)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Enhanced Discount Dialog (unchanged)
    if (showDiscountDialog) {
        AlertDialog(
            onDismissRequest = { showDiscountDialog = false },
            title = {
                Text(
                    "Chọn mã giảm giá",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    discountCodes.forEach { (code, discount) ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedDiscount = code
                                    discountAmount = discount.amount
                                    showDiscountDialog = false
                                }
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedDiscount == code)
                                Color(0xFFFF6B35).copy(alpha = 0.1f)
                            else Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        code,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1A1A1A)
                                    )
                                    Text(
                                        discount.title,
                                        fontSize = 12.sp,
                                        color = Color(0xFF666666)
                                    )
                                    Text(
                                        discount.description,
                                        fontSize = 11.sp,
                                        color = Color(0xFF888888)
                                    )
                                }
                                Text(
                                    "-${formatCurrency(discount.amount)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF6B35)
                                )
                            }
                        }
                    }

                    if (selectedDiscount.isNotEmpty()) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedDiscount = ""
                                    discountAmount = 0.0
                                    showDiscountDialog = false
                                }
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Transparent
                        ) {
                            Text(
                                "Không sử dụng mã giảm giá",
                                fontSize = 14.sp,
                                color = Color(0xFFFF5722),
                                modifier = Modifier.padding(12.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDiscountDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFF6B35)
                    )
                ) {
                    Text("Đóng", fontWeight = FontWeight.Medium)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

// New composable for address radio items
@Composable
fun AddressRadioItem(
    address: AddressInfo,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Color(0xFFFF6B35).copy(alpha = 0.05f) else Color.Transparent,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(0xFFFF6B35).copy(alpha = 0.3f)
        ) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFFF6B35)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name and phone
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = address.ten_nguoi_nhan ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A1A1A)
                    )
                    if (address.la_dia_chi_mac_dinh == 1) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF4CAF50).copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "Mặc định",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = address.so_dien_thoai_nguoi_nhan ?: "",
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Full address
                Text(
                    text = buildString {
                        address.so_duong?.let { append(it) }
                        address.phuong_xa?.let { append(", $it") }
                        address.quan_huyen?.let { append(", $it") }
                        address.tinh_thanh_pho?.let { append(", $it") }
                    },
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Notes if available
                address.ghi_chu?.let { notes ->
                    if (notes.isNotBlank()) {
                        Text(
                            text = "Ghi chú: $notes",
                            fontSize = 12.sp,
                            color = Color(0xFF888888),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// Data classes for better organization (unchanged)
data class PaymentMethod(
    val name: String,
    val icon: String,
    val description: String
)

data class DiscountCode(
    val code: String,
    val title: String,
    val amount: Double,
    val description: String
)

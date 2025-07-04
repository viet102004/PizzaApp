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
import androidx.compose.material.icons.filled.Warning
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
import com.example.pizza_app.data.model.MaGiamGia
import com.example.pizza_app.ui.cart.CartItemCard
import com.example.pizza_app.ui.profile.AddressViewModel
import com.example.pizza_app.ui.vouchers.VoucherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    addressViewModel: AddressViewModel = viewModel()
) {

    val voucherViewModel: VoucherViewModel = viewModel()
    val vouchers by voucherViewModel.voucherList.collectAsState()


    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.totalAmount.collectAsState()

    // Address states
    val isAddressLoading by addressViewModel.isLoading.collectAsState()
    val addressMessage by addressViewModel.message.collectAsState()


    var selectedVoucher by remember { mutableStateOf<MaGiamGia?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("Tiền mặt") }
    var orderNote by remember { mutableStateOf("") }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showDiscountDialog by remember { mutableStateOf(false) }
    var showAddressRequiredDialog by remember { mutableStateOf(false) }

    val discountAmount = remember(cartTotal, selectedVoucher) {
        selectedVoucher?.let { voucher ->
            val isValid = cartTotal >= (voucher.gia_tri_don_hang_toi_thieu?.toDouble() ?: 0.0)
            if (!isValid) return@remember 0.0
            when (voucher.loai_giam_gia.lowercase()) {
                "phan_tram" -> cartTotal * (voucher.gia_tri_giam.toDouble() / 100)
                "co_dinh" -> voucher.gia_tri_giam.toDouble()
                else -> 0.0
            }
        } ?: 0.0
    }



    val viewModel = remember { PayViewModel() }
    val isLoading by viewModel.isLoading.collectAsState()
    val orderResult by viewModel.orderResult.collectAsState()
    val errorMessage by viewModel.message.collectAsState()

    val addressList by addressViewModel.addressList.collectAsState()
    var selectedAddress by remember { mutableStateOf<AddressInfo?>(null) }
    val selectedAddressId = selectedAddress?.ma_thong_tin_giao_hang

    // Kiểm tra có địa chỉ hay không
    val hasAddress = addressList.isNotEmpty()
    val isOrderEnabled = hasAddress && selectedAddress != null && !isLoading

    val paymentMethods = listOf(
        PaymentMethod("Tiền mặt", "💰", "Thanh toán khi nhận hàng"),
        PaymentMethod("MoMo", "📱", "Ví điện tử MoMo"),
        PaymentMethod("ZaloPay", "⚡", "Ví điện tử ZaloPay"),
        PaymentMethod("Thẻ tín dụng", "💳", "Visa, Master, JCB")
    )

    // Load data when screen opens
    LaunchedEffect(Unit) {
        cartViewModel.fetchCartItems()
        addressViewModel.getDeliveryAddresses()
        voucherViewModel.fetchVouchers()
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
                // 1. Địa chỉ giao hàng - Updated to show selected address with validation
                item {
                    ModernCard(
                        icon = Icons.Default.LocationOn,
                        iconColor = if (hasAddress) Color(0xFF4CAF50) else Color(0xFFFF6B35),
                        title = "Địa chỉ giao hàng",
                        action = if (hasAddress) "Thay đổi" else "Thêm địa chỉ",
                        onActionClick = {
                            if (hasAddress) {
                                addressViewModel.getDeliveryAddresses() // Refresh addresses
                                showAddressDialog = true
                            } else {
                                navController.navigate("add_address")
                            }
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
                            // Show different message based on loading state and address availability
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (!hasAddress && !isAddressLoading) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFFF6B35),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    text = when {
                                        isAddressLoading -> "Đang tải địa chỉ..."
                                        !hasAddress -> "Vui lòng thêm địa chỉ giao hàng"
                                        else -> "Chưa có địa chỉ giao hàng"
                                    },
                                    fontSize = 14.sp,
                                    color = if (!hasAddress && !isAddressLoading) Color(0xFFFF6B35) else Color(0xFF888888),
                                    fontWeight = if (!hasAddress && !isAddressLoading) FontWeight.Medium else FontWeight.Normal
                                )
                            }
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

                // 3. Tổng tiền sản phẩm - Đã sửa để dùng selectedVoucher
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

                            if (selectedVoucher != null && discountAmount > 0) {
                                PaymentDetailRow(
                                    "Giảm giá (${selectedVoucher!!.ma_code})",
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

                // 4. Mã giảm giá - Sửa lại để dùng dữ liệu từ API
                item {
                    ModernCard(
                        icon = Icons.Default.LocalOffer,
                        iconColor = Color(0xFFFF6B35),
                        title = "Mã giảm giá",
                        action = "Chọn mã",
                        onActionClick = { showDiscountDialog = true }
                    ) {
                        if (selectedVoucher != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = selectedVoucher!!.ma_code,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF4CAF50)
                                    )
                                    val minOrder = selectedVoucher!!.gia_tri_don_hang_toi_thieu?.toDouble() ?: 0.0
                                    if (minOrder > 0.0) {
                                        Text(
                                            text = "Đơn từ ${formatCurrency(minOrder)}",
                                            fontSize = 12.sp,
                                            color = Color(0xFF888888)
                                        )
                                    }
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

            // 7. Bottom section - Enhanced design with validation
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
                                if (!hasAddress) {
                                    showAddressRequiredDialog = true
                                } else if (selectedAddressId != null) {
                                    viewModel.datHang(
                                        maThongTinGiaoHang = selectedAddressId!!.toInt(),
                                        phuongThucThanhToan = selectedPaymentMethod,
                                        maGiamGia = selectedVoucher?.ma_giam_gia,
                                        ghiChu = orderNote,
                                        thoiGianGiaoDuKien = null
                                    )
                                    navController.navigate("home")
                                }
                            },
                            enabled = isOrderEnabled,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOrderEnabled) Color(0xFFFF6B35) else Color(0xFFCCCCCC),
                                contentColor = Color.White
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Text(
                                    text = if (!hasAddress) "Thêm địa chỉ để đặt hàng" else "Đặt hàng",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
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

    // Dialog yêu cầu thêm địa chỉ
    if (showAddressRequiredDialog) {
        AlertDialog(
            onDismissRequest = { showAddressRequiredDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFF6B35),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Yêu cầu địa chỉ giao hàng",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1A1A)
                )
            },
            text = {
                Text(
                    "Bạn cần thêm địa chỉ giao hàng để có thể đặt hàng. Vui lòng thêm địa chỉ trước khi tiếp tục.",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAddressRequiredDialog = false
                        navController.navigate("add_address")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B35)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Thêm địa chỉ",
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddressRequiredDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF666666)
                    )
                ) {
                    Text("Để sau", fontWeight = FontWeight.Medium)
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
                    vouchers.forEach { voucher ->
                        val isSelected = selectedVoucher?.ma_giam_gia == voucher.ma_giam_gia
                        val isValid = cartTotal >= (voucher.gia_tri_don_hang_toi_thieu?.toDouble() ?: 0.0)

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isValid) {
                                        selectedVoucher = voucher
                                        showDiscountDialog = false
                                    }
                                }
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFFFF6B35).copy(alpha = 0.1f) else Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(voucher.ma_code, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("Giảm ${voucher.gia_tri_giam} (${voucher.loai_giam_gia})", fontSize = 12.sp)
                                    if (voucher.gia_tri_don_hang_toi_thieu != null) {
                                        Text("Áp dụng cho đơn từ ${formatCurrency(voucher.gia_tri_don_hang_toi_thieu.toDouble())}", fontSize = 11.sp)
                                    }
                                    if (!isValid) {
                                        Text("Không đủ điều kiện", fontSize = 11.sp, color = Color.Red)
                                    }
                                }
                                if (isSelected) {
                                    Text("-${formatCurrency(discountAmount)}", fontWeight = FontWeight.Bold, color = Color(0xFFFF6B35))
                                }
                            }
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

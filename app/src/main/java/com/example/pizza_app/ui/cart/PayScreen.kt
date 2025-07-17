package com.example.pizza_app.ui.cart

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizza_app.MainActivity
import com.example.pizza_app.R
import com.example.pizza_app.data.model.CartItem
import com.example.pizza_app.data.model.AddressInfo
import com.example.pizza_app.data.model.MaGiamGia
import com.example.pizza_app.ui.cart.CartItemCard
import com.example.pizza_app.ui.profile.AddressViewModel
import com.example.pizza_app.ui.vouchers.VoucherViewModel
import kotlinx.coroutines.launch

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

    var selectedVoucher by remember { mutableStateOf<MaGiamGia?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf<String?>(null) }
    var orderNote by remember { mutableStateOf("") }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showDiscountDialog by remember { mutableStateOf(false) }
    var showAddressRequiredDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()


    val payViewModel: PayViewModel = viewModel()
    val context = LocalContext.current
    val activity = context as? MainActivity

    var showPaymentFailDialog by remember { mutableStateOf(false) }
    var paymentFailMessage by remember { mutableStateOf("") }
    val showPaymentFailureDialog by payViewModel.showPaymentFailureDialog.collectAsState()
    val paymentUrl by payViewModel.paymentUrl.collectAsState()
    val paymentCallbackResult by payViewModel.paymentCallbackResult.collectAsState()

    var isAppInForeground by remember { mutableStateOf(true) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (!isAppInForeground) {
                        // App đã resume từ background
                        payViewModel.onAppResumed()
                    }
                    isAppInForeground = true
                }
                Lifecycle.Event.ON_PAUSE -> {
                    isAppInForeground = false
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    LaunchedEffect(paymentCallbackResult) {
        paymentCallbackResult?.let { result ->
            if (!result.isSuccess) {
                paymentFailMessage = result.message
                showPaymentFailDialog = true
            } else {
                // Thanh toán thành công - về home
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }

    LaunchedEffect(paymentUrl) {
        paymentUrl?.let { url ->
            Log.d("PayScreen", "Received payment URL: $url")
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                Log.d("PayScreen", "Started payment intent")
            } catch (e: Exception) {
                Log.e("PayScreen", "Error starting payment intent", e)
            }
            // Không reset state ngay lập tức để tránh mất thông tin
            // payViewModel.resetState()
        }
    }

    LaunchedEffect(payViewModel) {
        activity?.setPayViewModel(payViewModel)
    }

    val discountAmount = remember(cartTotal, selectedVoucher) {
        selectedVoucher?.let { voucher ->
            val isValid = cartTotal >= (voucher.gia_tri_don_hang_toi_thieu?.toDouble() ?: 0.0)
            if (!isValid) return@remember 0.0
            val calculatedDiscount = when (voucher.loai_giam_gia.lowercase()) {
                "phan_tram" -> cartTotal * (voucher.gia_tri_giam.toDouble() / 100)
                "co_dinh" -> voucher.gia_tri_giam.toDouble()
                else -> 0.0
            }
            minOf(calculatedDiscount, cartTotal)
        } ?: 0.0
    }

    val finalTotal = remember(cartTotal, discountAmount) {
        maxOf(0.0, cartTotal - discountAmount)
    }


    val viewModel = remember { PayViewModel() }
    val isLoading by viewModel.isLoading.collectAsState()

    val addressList by addressViewModel.addressList.collectAsState()
    var selectedAddress by remember { mutableStateOf<AddressInfo?>(null) }
    val selectedAddressId = selectedAddress?.ma_thong_tin_giao_hang

    // Kiểm tra có địa chỉ hay không
    val hasAddress = addressList.isNotEmpty()
    val isOrderEnabled = hasAddress && selectedAddress != null && selectedPaymentMethod != null && !isLoading
    var paymentMethodIndex by remember { mutableStateOf(-1) }

    var isOrderPlaced by remember { mutableStateOf(false) }

    val paymentMethods = listOf(
        PaymentMethod("Tiền mặt", R.drawable.ic_cash, "Thanh toán khi nhận hàng"),
        PaymentMethod("MoMo", R.drawable.ic_momo, "Ví điện tử MoMo"),
        //PaymentMethod("ZaloPay", "⚡", "Ví điện tử ZaloPay"),
        //PaymentMethod("Thẻ tín dụng", "💳", "Visa, Master, JCB")
    )

    LaunchedEffect(Unit) {
        cartViewModel.fetchCartItems()
        addressViewModel.getDeliveryAddresses()
        voucherViewModel.fetchVouchers()
    }

    LaunchedEffect(addressList) {
        if (selectedAddress == null && addressList.isNotEmpty()) {
            val defaultAddress = addressList.find { it.la_dia_chi_mac_dinh == 1 }
                ?: addressList.firstOrNull()
            selectedAddress = defaultAddress
        }
    }

    BackHandler(enabled = isOrderPlaced) {}

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
                state = listState,
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
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = formatCurrency(finalTotal),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF6B35)
                                    )
                                }
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

                    LaunchedEffect(Unit) {
                        paymentMethodIndex = 5 // Index của payment method section (đếm từ 0)
                    }

                    ModernCard(
                        icon = Icons.Default.Payment,
                        iconColor = Color(0xFF2196F3),
                        title = "Phương thức thanh toán"
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            if (selectedPaymentMethod == null) {
                                Text(
                                    text = "Vui lòng chọn phương thức thanh toán",
                                    fontSize = 14.sp,
                                    color = Color(0xFFFF6B35),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

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
                                text = formatCurrency(finalTotal),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6B35)
                            )
                            if (discountAmount > 0) {
                                Text(
                                    text = if (finalTotal == 0.0) "🎉 Đơn hàng miễn phí!" else "Tiết kiệm ${formatCurrency(discountAmount)}",
                                    fontSize = 12.sp,
                                    color = if (finalTotal == 0.0) Color(0xFF4CAF50) else Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Button(
                            onClick = {
                                when {
                                    !hasAddress -> {
                                        showAddressRequiredDialog = true
                                    }
                                    selectedPaymentMethod == null -> {
                                        scope.launch {
                                            listState.animateScrollToItem(paymentMethodIndex)
                                        }
                                    }
                                    selectedAddressId != null -> {
                                        val paymentMethodValue = when (selectedPaymentMethod) {
                                            "Tiền mặt" -> "tien_mat"
                                            "MoMo" -> "momo"
                                            "ZaloPay" -> "zalopay"
                                            "Thẻ tín dụng" -> "the_tin_dung"
                                            else -> "tien_mat"
                                        }

                                        Log.d("PayScreen", "Placing order with payment method: $paymentMethodValue")

                                        payViewModel.datHang(
                                            context = context,
                                            maThongTinGiaoHang = selectedAddressId!!.toInt(),
                                            phuongThucThanhToan = paymentMethodValue,
                                            maGiamGia = selectedVoucher?.ma_giam_gia,
                                            ghiChu = orderNote,
                                            thoiGianGiaoDuKien = null
                                        )

                                        // Chỉ navigate về home nếu là thanh toán tiền mặt
                                        if(selectedPaymentMethod == "Tiền mặt") {
                                            isOrderPlaced = true
                                            navController.navigate("home") {
                                                popUpTo("home") { inclusive = true }
                                            }
                                        }
                                        // Với MoMo, chờ paymentUrl để mở link thanh toán
                                    }
                                }
                            },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    !hasAddress -> Color(0xFFCCCCCC)
                                    selectedPaymentMethod == null -> Color(0xFFFFB700)
                                    isLoading -> Color(0xFFCCCCCC)
                                    else -> Color(0xFFFF6B35)
                                },
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
                                    text = when {
                                        !hasAddress -> "Thêm địa chỉ để đặt hàng"
                                        selectedPaymentMethod == null -> "Chọn phương thức thanh toán"
                                        else -> "Đặt hàng"
                                    },
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
    if (showPaymentFailureDialog) {
        AlertDialog(
            onDismissRequest = { }, // Không cho dismiss bằng cách click ra ngoài
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Thanh toán không thành công",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        "Bạn đã không hoàn thành thanh toán MoMo, nhưng đơn hàng đã được đặt thành công.",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Bạn có thể:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "• Thanh toán khi nhận hàng\n• Thanh toán online sau trong phần đơn hàng",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        payViewModel.dismissPaymentFailureDialog()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B35)
                    )
                ) {
                    Text("Đóng", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        payViewModel.dismissPaymentFailureDialog()
                        navController.navigate("orders") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                ) {
                    Text("Xem đơn hàng", color = Color(0xFFFF6B35))
                }
            }
        )
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

    if (showDiscountDialog) {
        AlertDialog(
            onDismissRequest = { showDiscountDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Chọn mã giảm giá",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    IconButton(
                        onClick = { showDiscountDialog = false },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (vouchers.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = Color(0xFFCCCCCC),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Chưa có mã giảm giá",
                                    fontSize = 16.sp,
                                    color = Color(0xFF666666),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "Mã giảm giá sẽ xuất hiện tại đây",
                                    fontSize = 12.sp,
                                    color = Color(0xFF999999),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    } else {
                        items(vouchers) { voucher ->
                            val isSelected = selectedVoucher?.ma_giam_gia == voucher.ma_giam_gia
                            val isValid = cartTotal >= (voucher.gia_tri_don_hang_toi_thieu?.toDouble() ?: 0.0)
                            val discountValue = when (voucher.loai_giam_gia.lowercase()) {
                                "phan_tram" -> cartTotal * (voucher.gia_tri_giam.toDouble() / 100)
                                "co_dinh" -> voucher.gia_tri_giam.toDouble()
                                else -> 0.0
                            }
                            // Đảm bảo discount không vượt quá giá trị đơn hàng
                            val actualDiscount = minOf(discountValue, cartTotal)
                            val willBeFree = actualDiscount >= cartTotal

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isValid) {
                                            selectedVoucher = voucher
                                            showDiscountDialog = false
                                        }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = when {
                                        isSelected -> Color(0xFFFF6B35).copy(alpha = 0.1f)
                                        isValid -> Color.White
                                        else -> Color(0xFFF5F5F5)
                                    }
                                ),
                                elevation = CardDefaults.cardElevation(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Left side - Voucher icon and info
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // Voucher icon with gradient background
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(
                                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                                        colors = if (isValid) {
                                                            listOf(Color(0xFFFF6B35), Color(0xFFFFB700))
                                                        } else {
                                                            listOf(Color(0xFFCCCCCC), Color(0xFFE0E0E0))
                                                        }
                                                    ),
                                                    shape = RoundedCornerShape(12.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.LocalOffer,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        // Voucher details
                                        Column {
                                            Text(
                                                text = voucher.ma_code,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isValid) Color(0xFF1A1A1A) else Color(0xFF999999)
                                            )

                                            Text(
                                                text = "Giảm ${voucher.gia_tri_giam.toInt()}${if (voucher.loai_giam_gia == "phan_tram") "%" else "đ"}",
                                                fontSize = 14.sp,
                                                color = if (isValid) Color(0xFF4CAF50) else Color(0xFF999999),
                                                fontWeight = FontWeight.Medium
                                            )

                                            voucher.gia_tri_don_hang_toi_thieu?.let { minValue ->
                                                Text(
                                                    text = "Đơn từ ${formatCurrency(minValue.toDouble())}",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF666666)
                                                )
                                            }

                                            if (!isValid) {
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = Color(0xFFFFEBEE)
                                                ) {
                                                    Text(
                                                        text = "Không đủ điều kiện",
                                                        fontSize = 11.sp,
                                                        color = Color(0xFFE53935),
                                                        fontWeight = FontWeight.Medium,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Right side - Discount amount and selection indicator
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        if (isValid) {
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = if (isSelected) Color(0xFFFF6B35) else Color(0xFF4CAF50).copy(alpha = 0.1f)
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Text(
                                                        text = "-${formatCurrency(actualDiscount)}",
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) Color.White else Color(0xFF4CAF50)
                                                    )
//                                                    if (willBeFree) {
//                                                        Text(
//                                                            text = "🎉 Miễn phí",
//                                                            fontSize = 10.sp,
//                                                            color = if (isSelected) Color.White else Color(0xFF4CAF50),
//                                                            fontWeight = FontWeight.Bold
//                                                        )
//                                                    }
                                                }
                                            }
                                        }

                                        if (isSelected) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = Color(0xFF4CAF50)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        Icons.Default.CardGiftcard,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Đã chọn",
                                                        fontSize = 10.sp,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (selectedVoucher != null) {
                        OutlinedButton(
                            onClick = {
                                selectedVoucher = null
                                showDiscountDialog = false
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF666666)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Bỏ chọn", fontWeight = FontWeight.Medium)
                        }
                    }

                    Button(
                        onClick = { showDiscountDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B35)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Xác nhận", fontWeight = FontWeight.Medium, color = Color.White)
                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color(0xFFFAFAFA)
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
    val icon: Int?,      // Drawable resource ID (nullable)
    val description: String
)

data class DiscountCode(
    val code: String,
    val title: String,
    val amount: Double,
    val description: String
)

package com.example.pizza_app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.getFullImageUrl
import android.widget.Toast
import com.example.pizza_app.ui.cart.CartViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.pizza_app.ui.components.AuthDialog

@Composable
fun ProductDetailScreen(
    navController: NavController,
    maSanPham: Int,
    isLoggedIn: Boolean,
    onNavigateTo: (String) -> Unit,
    viewModel: ProductDetailViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(maSanPham) {
        viewModel.fetchProductDetail(maSanPham)
        viewModel.checkIsFavorite(maSanPham)
        viewModel.fetchProductReviews(maSanPham)
        viewModel.fetchReviewStats(maSanPham)
    }

    val reviews by viewModel.reviews.collectAsState()
    val reviewStats by viewModel.reviewStats.collectAsState()
    val isLoadingReviews by viewModel.isLoadingReviews.collectAsState()

    val options by viewModel.options.collectAsState()
    val product by viewModel.product.collectAsState()
    val imageList by viewModel.images.collectAsState()
    val context = LocalContext.current

    val selectedImage = remember { mutableStateOf<String?>(null) }
    val quantity = remember { mutableStateOf(1) }

    val selectedOptions = remember { mutableStateMapOf<Int, Int>() }

    val multipleSelectedOptions = remember { mutableStateMapOf<Int, MutableSet<Int>>() } // ma_loai_tuy_chon -> Set<ma_gia_tri>

    // State cho dialog tùy chọn sản phẩm
    val showDialog = remember { mutableStateOf(false) }
    val dialogAction = remember { mutableStateOf<String?>(null) }

    // State cho auth dialog
    val showAuthDialog = remember { mutableStateOf(false) }
    val pendingAction = remember { mutableStateOf<String?>(null) }

    val primaryColor = Color(0xFFFF6B35)
    val secondaryColor = Color(0xFFFFB700)
    val backgroundColor = Color(0xFFF8F9FA)
    val cardColor = Color.White

    LaunchedEffect(maSanPham) {
        viewModel.fetchProductDetail(maSanPham)
    }

    LaunchedEffect(imageList) {
        if (imageList.isNotEmpty()) selectedImage.value = imageList.first().url_hinh_anh
    }

    LaunchedEffect(options) {
        if (options.isNotEmpty()) {
            options.forEach { option ->
                when (option.loai_lua_chon) {
                    "checkbox", "multiple" -> {
                        // Khởi tạo multiple choice options
                        if (multipleSelectedOptions[option.ma_loai_tuy_chon] == null) {
                            multipleSelectedOptions[option.ma_loai_tuy_chon] = mutableSetOf()
                        }
                    }
                    else -> {
                        // Single choice options
                        if (selectedOptions[option.ma_loai_tuy_chon] == null && option.gia_tri.isNotEmpty()) {
                            selectedOptions[option.ma_loai_tuy_chon] = option.gia_tri.first().ma_gia_tri
                        }
                    }
                }
            }
        }
    }

    // Function để tính tổng giá với tùy chọn
    fun calculateTotalPrice(): Double {
        val basePrice = product?.gia_co_ban ?: 0.0
        val singleOptionsPrice = selectedOptions.entries.sumOf { (maLoaiTuyChon, maGiaTri) ->
            options.find { it.ma_loai_tuy_chon == maLoaiTuyChon }?.gia_tri
                ?.find { it.ma_gia_tri == maGiaTri }?.gia_them ?: 0.0
        }
        val multipleOptionsPrice = multipleSelectedOptions.entries.sumOf { (maLoaiTuyChon, selectedValues) ->
            val option = options.find { it.ma_loai_tuy_chon == maLoaiTuyChon }
            selectedValues.sumOf { maGiaTri ->
                option?.gia_tri?.find { it.ma_gia_tri == maGiaTri }?.gia_them ?: 0.0
            }
        }
        return basePrice + singleOptionsPrice + multipleOptionsPrice
    }

    // Function để xử lý click button
    fun handleButtonClick(action: String) {
        if (isLoggedIn) {
            when (action) {
                "favorite" -> {
                    viewModel.toggleFavorite(maSanPham, isFavorite)
                }
                else -> {
                    dialogAction.value = action
                    showDialog.value = true
                }
            }
        } else {
            pendingAction.value = action
            showAuthDialog.value = true
        }
    }

    // Function để validate required options
    fun validateRequiredOptions(): String? {
        val missingOptions = mutableListOf<String>()

        options.forEach { option ->
            if (option.bat_buoc) {
                when (option.loai_lua_chon) {
                    "checkbox", "multiple" -> {
                        val selectedValues = multipleSelectedOptions[option.ma_loai_tuy_chon]
                        if (selectedValues.isNullOrEmpty()) {
                            missingOptions.add(option.ten_loai)
                        }
                    }
                    else -> {
                        if (selectedOptions[option.ma_loai_tuy_chon] == null) {
                            missingOptions.add(option.ten_loai)
                        }
                    }
                }
            }
        }

        return if (missingOptions.isNotEmpty()) {
            "Vui lòng chọn: ${missingOptions.joinToString(", ")}"
        } else null
    }

    // Function để tạo final selected options map
    fun createFinalSelectedOptionsMap(): Map<Int, Int> {
        val finalMap = mutableMapOf<Int, Int>()

        // Thêm single choice options
        finalMap.putAll(selectedOptions)

        // Thêm multiple choice options (chỉ lấy option đầu tiên cho đơn giản)
        // Trong thực tế, bạn có thể cần logic phức tạp hơn để handle multiple options
        multipleSelectedOptions.forEach { (maLoaiTuyChon, selectedValues) ->
            if (selectedValues.isNotEmpty()) {
                finalMap[maLoaiTuyChon] = selectedValues.first()
            }
        }

        return finalMap
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            // Image section
            Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                AsyncImage(
                    model = getFullImageUrl(selectedImage.value),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )

                Box(
                    modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.2f)
                            )
                        )
                    ).clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp).statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.size(44.dp).shadow(8.dp, CircleShape)
                            .background(cardColor, CircleShape)
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = com.example.pizza_app.R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier.size(44.dp).shadow(8.dp, CircleShape)
                            .background(cardColor, CircleShape)
                            .clickable { handleButtonClick("favorite") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) primaryColor else Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                // Other images section
                if (imageList.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = cardColor
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Hình ảnh khác", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                            Spacer(modifier = Modifier.height(12.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(imageList) { image ->
                                    Box(
                                        modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
                                            .border(
                                                width = if (selectedImage.value == image.url_hinh_anh) 2.dp else 0.dp,
                                                color = if (selectedImage.value == image.url_hinh_anh) primaryColor else Color.Transparent,
                                                shape = RoundedCornerShape(12.dp)
                                            ).clickable {
                                                selectedImage.value = image.url_hinh_anh
                                            }
                                    ) {
                                        AsyncImage(
                                            model = getFullImageUrl(image.url_hinh_anh),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(), elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp), backgroundColor = cardColor
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(product?.ten_san_pham ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val totalPrice = calculateTotalPrice()
                            val basePrice = product?.gia_co_ban ?: 0.0
                            val additionalPrice = totalPrice - basePrice

                            Text(totalPrice.formatCurrency(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                            if (additionalPrice > 0) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("(+${additionalPrice.formatCurrency()})", fontSize = 14.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.weight(1f))
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(Icons.Default.Star, contentDescription = null, tint = secondaryColor, modifier = Modifier.size(16.dp))
//                                Spacer(modifier = Modifier.width(4.dp))
//                                Text("4.6", fontSize = 14.sp, fontWeight = FontWeight.Medium)
//                                Text(" (2k+ Reviews)", fontSize = 12.sp, color = Color.Gray)
//                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(product?.mo_ta ?: "", fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // REVIEWS VÀO ĐÂY
                ReviewsSection(
                    reviews = reviews,
                    reviewStats = reviewStats,
                    isLoading = isLoadingReviews,
                    onSeeAllClick = {
                        // Navigate to full reviews screen
                        onNavigateTo("reviews/$maSanPham")
                    },
                    primaryColor = primaryColor,
                    cardColor = cardColor
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Bottom buttons
        Card(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            elevation = 12.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            backgroundColor = cardColor
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp).navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { handleButtonClick("add_to_cart") },
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF0F0F0)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text("Thêm vào giỏ", color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }

                Button(
                    onClick = { handleButtonClick("buy_now") },
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevation(4.dp)
                ) {
                    Text("Mua ngay", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
            }
        }
    }

    // Auth Dialog
    AuthDialog(
        showDialog = showAuthDialog.value,
        onDismiss = {
            showAuthDialog.value = false
            pendingAction.value = null
        },
        onLoginClick = {
            onNavigateTo("login")
        },
        onRegisterClick = {
            onNavigateTo("register")
        }
    )

    // Bottom Sheet Dialog với dữ liệu động (chỉ hiển thị khi đã đăng nhập)
    if (showDialog.value && isLoggedIn) {
        Dialog(
            onDismissRequest = { showDialog.value = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AnimatedVisibility(
                visible = showDialog.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { showDialog.value = false }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f)
                            .align(Alignment.BottomCenter)
                            .clickable { },
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        backgroundColor = cardColor,
                        elevation = 16.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(20.dp)
                        ) {
                            // Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tùy chọn sản phẩm",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                IconButton(
                                    onClick = { showDialog.value = false },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Hiển thị các tùy chọn động từ API
                            options.forEach { option ->
                                Text(
                                    text = option.ten_loai + if (option.bat_buoc) " *" else "",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (option.bat_buoc) primaryColor else Color.Black
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                // Hiển thị các giá trị của tùy chọn dựa trên loai_lua_chon
                                when (option.loai_lua_chon) {
                                    "radio", "single" -> {
                                        // Single choice
                                        if (option.gia_tri.size <= 3) {
                                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                                option.gia_tri.forEach { value ->
                                                    val isSelected = selectedOptions[option.ma_loai_tuy_chon] == value.ma_gia_tri
                                                    Box(
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .height(64.dp)
                                                            .background(
                                                                if (isSelected) primaryColor else Color(0xFFF5F5F5),
                                                                RoundedCornerShape(12.dp)
                                                            )
                                                            .clickable {
                                                                selectedOptions[option.ma_loai_tuy_chon] = value.ma_gia_tri
                                                            },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                            Text(
                                                                text = value.ten_gia_tri,
                                                                fontSize = 14.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = if (isSelected) Color.White else Color.Black
                                                            )
                                                            if (value.gia_them > 0) {
                                                                Text(
                                                                    text = "+${value.gia_them.formatCurrency()}",
                                                                    fontSize = 12.sp,
                                                                    color = if (isSelected) Color.White else Color.Gray
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                option.gia_tri.forEach { value ->
                                                    val isSelected = selectedOptions[option.ma_loai_tuy_chon] == value.ma_gia_tri
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(48.dp)
                                                            .background(
                                                                if (isSelected) primaryColor else Color(0xFFF5F5F5),
                                                                RoundedCornerShape(12.dp)
                                                            )
                                                            .clickable {
                                                                selectedOptions[option.ma_loai_tuy_chon] = value.ma_gia_tri
                                                            }
                                                            .padding(horizontal = 16.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = value.ten_gia_tri,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = if (isSelected) Color.White else Color.Black
                                                        )
                                                        if (value.gia_them > 0) {
                                                            Text(
                                                                text = "+${value.gia_them.formatCurrency()}",
                                                                fontSize = 12.sp,
                                                                color = if (isSelected) Color.White else Color.Gray
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    "checkbox", "multiple" -> {
                                        // Multiple choice
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            option.gia_tri.forEach { value ->
                                                val selectedValues = multipleSelectedOptions[option.ma_loai_tuy_chon] ?: mutableSetOf()
                                                val isSelected = selectedValues.contains(value.ma_gia_tri)

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(48.dp)
                                                        .background(
                                                            if (isSelected) primaryColor.copy(alpha = 0.1f) else Color(0xFFF5F5F5),
                                                            RoundedCornerShape(12.dp)
                                                        )
                                                        .clickable {
                                                            val currentSet = multipleSelectedOptions[option.ma_loai_tuy_chon] ?: mutableSetOf()
                                                            if (isSelected) {
                                                                currentSet.remove(value.ma_gia_tri)
                                                            } else {
                                                                currentSet.add(value.ma_gia_tri)
                                                            }
                                                            multipleSelectedOptions[option.ma_loai_tuy_chon] = currentSet
                                                        }
                                                        .padding(horizontal = 16.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        androidx.compose.material3.Checkbox(
                                                            checked = isSelected,
                                                            onCheckedChange = { checked ->
                                                                val currentSet = multipleSelectedOptions[option.ma_loai_tuy_chon] ?: mutableSetOf()
                                                                if (checked) {
                                                                    currentSet.add(value.ma_gia_tri)
                                                                } else {
                                                                    currentSet.remove(value.ma_gia_tri)
                                                                }
                                                                multipleSelectedOptions[option.ma_loai_tuy_chon] = currentSet
                                                            }
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            text = value.ten_gia_tri,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = Color.Black
                                                        )
                                                    }
                                                    if (value.gia_them > 0) {
                                                        Text(
                                                            text = "+${value.gia_them.formatCurrency()}",
                                                            fontSize = 12.sp,
                                                            color = Color.Gray
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else -> {
                                        // Default fallback
                                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                            option.gia_tri.forEach { value ->
                                                val isSelected = selectedOptions[option.ma_loai_tuy_chon] == value.ma_gia_tri
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(48.dp)
                                                        .background(
                                                            if (isSelected) primaryColor else Color(0xFFF5F5F5),
                                                            RoundedCornerShape(12.dp)
                                                        )
                                                        .clickable {
                                                            selectedOptions[option.ma_loai_tuy_chon] = value.ma_gia_tri
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = value.ten_gia_tri,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = if (isSelected) Color.White else Color.Black
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            // Quantity selection
                            Text("Số lượng", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = { if (quantity.value > 1) quantity.value-- },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFF5F5F5), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Giảm",
                                        tint = Color.Black
                                    )
                                }

                                Spacer(modifier = Modifier.width(24.dp))

                                Text(
                                    text = quantity.value.toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.width(24.dp))

                                IconButton(
                                    onClick = { quantity.value++ },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(primaryColor, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tăng",
                                        tint = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Hiển thị tổng giá
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = Color(0xFFF8F9FA),
                                shape = RoundedCornerShape(12.dp),
                                elevation = 0.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Tổng cộng", fontSize = 14.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = (calculateTotalPrice() * quantity.value).formatCurrency(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Action button
                            Button(
                                onClick = {
                                    product?.let { prod ->
                                        // Validate required options
                                        val validationError = validateRequiredOptions()
                                        if (validationError != null) {
                                            Toast.makeText(context, validationError, Toast.LENGTH_SHORT).show()
                                            return@Button
                                        }

                                        val finalSelectedOptions = createFinalSelectedOptionsMap()

                                        when (dialogAction.value) {
                                            "add_to_cart" -> {
                                                cartViewModel.addToCart(
                                                    product = prod,
                                                    selectedOptions = finalSelectedOptions,
                                                    quantity = quantity.value,
                                                    imageUrl = selectedImage.value ?: "",
                                                    options = options
                                                )
                                                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                                                showDialog.value = false
                                            }
                                            "buy_now" -> {
                                                // Thêm vào giỏ hàng trước khi mua ngay
                                                cartViewModel.addToCart(
                                                    product = prod,
                                                    selectedOptions = finalSelectedOptions,
                                                    quantity = quantity.value,
                                                    imageUrl = selectedImage.value ?: "",
                                                    options = options
                                                )

                                                // Navigate to checkout
                                                onNavigateTo("checkout")
                                                showDialog.value = false
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = primaryColor
                                ),
                                shape = RoundedCornerShape(16.dp),
                                elevation = ButtonDefaults.elevation(4.dp)
                            ) {
                                Text(
                                    text = when (dialogAction.value) {
                                        "add_to_cart" -> "Thêm vào giỏ - ${(calculateTotalPrice() * quantity.value).formatCurrency()}"
                                        "buy_now" -> "Mua ngay - ${(calculateTotalPrice() * quantity.value).formatCurrency()}"
                                        else -> "Xác nhận"
                                    },
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
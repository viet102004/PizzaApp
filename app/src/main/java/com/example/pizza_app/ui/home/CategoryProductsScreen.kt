
package com.example.pizza_app.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.getFullImageUrl
import com.example.pizza_app.ui.cart.CartViewModel
import com.example.pizza_app.ui.components.AuthDialog
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    navController: NavController,
    category: Category,
    isLoggedIn: Boolean,
    onNavigateTo: (String) -> Unit,
    cartViewModel: CartViewModel = viewModel()
) {
    val viewModel: CategoryProductsViewModel = viewModel()
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showAuthDialog = remember { mutableStateOf(false) }
    val pendingProduct = remember { mutableStateOf<Product?>(null) }
    val context = LocalContext.current

    // States cho dialog tùy chọn sản phẩm
    val showProductDialog = remember { mutableStateOf(false) }
    val selectedProductForDialog = remember { mutableStateOf<Product?>(null) }

    // Debug logging
    LaunchedEffect(category.ma_danh_muc) {
        Log.d("CategoryProductsScreen", "=== CATEGORY DEBUG INFO ===")
        Log.d("CategoryProductsScreen", "Category ID: ${category.ma_danh_muc}")
        Log.d("CategoryProductsScreen", "Category Name: ${category.ten_danh_muc}")
        Log.d("CategoryProductsScreen", "Loading products...")

        viewModel.loadProductsByCategory(category.ma_danh_muc)
    }

    // Debug states
    LaunchedEffect(products) {
        Log.d("CategoryProductsScreen", "Products updated: ${products.size} items")
        products.forEachIndexed { index, product ->
            Log.d("CategoryProductsScreen", "Product $index: ${product.ten_san_pham}")
        }
    }

    LaunchedEffect(isLoading) {
        Log.d("CategoryProductsScreen", "Loading state: $isLoading")
    }

    LaunchedEffect(error) {
        Log.d("CategoryProductsScreen", "Error state: $error")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = category.ten_danh_muc,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                // Refresh button
                IconButton(
                    onClick = {
                        Log.d("CategoryProductsScreen", "Manual refresh triggered")
                        viewModel.refresh(category.ma_danh_muc)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Làm mới",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Content
        when {
            isLoading -> {
                LoadingProductGrid()
            }
            error != null -> {
                ErrorState(
                    message = error!!,
                    onRetry = {
                        Log.d("CategoryProductsScreen", "Retry button clicked")
                        viewModel.clearError()
                        viewModel.loadProductsByCategory(category.ma_danh_muc)
                    }
                )
            }
            products.isEmpty() -> {
                EmptyState(categoryName = category.ten_danh_muc)
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(products) { product ->
                        ProductItemCard(
                            product = product,
                            onClick = {
                                Log.d("CategoryProductsScreen", "Product clicked: ${product.ten_san_pham}")
                                navController.navigate("product_detail/${product.ma_san_pham}")
                            },
                            onAddToCart = { selectedProduct ->
                                if (isLoggedIn) {
                                    // Hiển thị dialog tùy chọn sản phẩm
                                    selectedProductForDialog.value = selectedProduct
                                    showProductDialog.value = true
                                } else {
                                    pendingProduct.value = selectedProduct
                                    showAuthDialog.value = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Auth Dialog
    AuthDialog(
        showDialog = showAuthDialog.value,
        onDismiss = {
            showAuthDialog.value = false
            pendingProduct.value = null
        },
        onLoginClick = {
            onNavigateTo("login")
        },
        onRegisterClick = {
            onNavigateTo("register")
        }
    )

    // Product Options Dialog
    selectedProductForDialog.value?.let { product ->
        ProductOptionsDialog(
            product = product,
            showDialog = showProductDialog.value,
            onDismiss = {
                showProductDialog.value = false
                selectedProductForDialog.value = null
            },
            onAddToCart = { selectedProduct, selectedOptions, quantity, imageUrl, options ->
                cartViewModel.addToCart(
                    product = selectedProduct,
                    selectedOptions = selectedOptions,
                    quantity = quantity,
                    imageUrl = imageUrl,
                    options = options
                )
                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                showProductDialog.value = false
                selectedProductForDialog.value = null
            }
        )
    }
}

@Composable
private fun ProductOptionsDialog(
    product: Product,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddToCart: (Product, Map<Int, Int>, Int, String, List<com.example.pizza_app.data.model.ProductOption>) -> Unit
) {
    val productDetailViewModel: ProductDetailViewModel = viewModel()
    val options by productDetailViewModel.options.collectAsState()
    val imageList by productDetailViewModel.images.collectAsState()

    val selectedImage = remember { mutableStateOf<String?>(null) }
    val quantity = remember { mutableStateOf(1) }
    val selectedOptions = remember { mutableStateMapOf<Int, Int>() }
    val multipleSelectedOptions = remember { mutableStateMapOf<Int, MutableSet<Int>>() }
    val context = LocalContext.current

    val primaryColor = Color(0xFFFF6B35)
    val cardColor = Color.White

    // Load product options khi dialog mở
    LaunchedEffect(showDialog, product.ma_san_pham) {
        if (showDialog) {
            productDetailViewModel.fetchProductDetail(product.ma_san_pham.toInt())
        }
    }

    // Set default image
    LaunchedEffect(imageList) {
        if (imageList.isNotEmpty()) {
            selectedImage.value = imageList.first().url_hinh_anh
        } else {
            selectedImage.value = product.hinh_anh
        }
    }

    // Initialize options
    LaunchedEffect(options) {
        if (options.isNotEmpty()) {
            selectedOptions.clear()
            multipleSelectedOptions.clear()

            options.forEach { option ->
                when (option.loai_lua_chon) {
                    "checkbox", "multiple" -> {
                        multipleSelectedOptions[option.ma_loai_tuy_chon] = mutableSetOf()
                    }
                    else -> {
                        if (option.gia_tri.isNotEmpty()) {
                            selectedOptions[option.ma_loai_tuy_chon] = option.gia_tri.first().ma_gia_tri
                        }
                    }
                }
            }
        }
    }

    // Function để tính tổng giá
    fun calculateTotalPrice(): Double {
        val basePrice = product.gia_co_ban
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
        finalMap.putAll(selectedOptions)
        multipleSelectedOptions.forEach { (maLoaiTuyChon, selectedValues) ->
            if (selectedValues.isNotEmpty()) {
                finalMap[maLoaiTuyChon] = selectedValues.first()
            }
        }
        return finalMap
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AnimatedVisibility(
                visible = showDialog,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { onDismiss() }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                            .align(Alignment.BottomCenter)
                            .clickable { },
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
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
                                    text = product.ten_san_pham,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Product Image
                            AsyncImage(
                                model = getFullImageUrl(selectedImage.value),
                                contentDescription = product.ten_san_pham,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Gray.copy(alpha = 0.1f)),
                                contentScale = ContentScale.Crop
                            )

                            // Other images
                            if (imageList.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(imageList) { image ->
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(
                                                    width = if (selectedImage.value == image.url_hinh_anh) 2.dp else 0.dp,
                                                    color = if (selectedImage.value == image.url_hinh_anh) primaryColor else Color.Transparent,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
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

                            Spacer(modifier = Modifier.height(20.dp))

                            // Hiển thị các tùy chọn
                            options.forEach { option ->
                                Text(
                                    text = option.ten_loai + if (option.bat_buoc) " *" else "",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (option.bat_buoc) primaryColor else Color.Black
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                when (option.loai_lua_chon) {
                                    "radio", "single" -> {
                                        // Single choice options
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
                                    "checkbox", "multiple" -> {
                                        // Multiple choice options
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
                                                        Checkbox(
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
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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

                            // Add to cart button
                            Button(
                                onClick = {
                                    val validationError = validateRequiredOptions()
                                    if (validationError != null) {
                                        Toast.makeText(context, validationError, Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    val finalSelectedOptions = createFinalSelectedOptionsMap()
                                    onAddToCart(
                                        product,
                                        finalSelectedOptions,
                                        quantity.value,
                                        selectedImage.value ?: product.hinh_anh ?: "",
                                        options
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "Thêm vào giỏ - ${(calculateTotalPrice() * quantity.value).formatCurrency()}",
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

// Các composable khác giữ nguyên như cũ
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "⚠️",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Có lỗi xảy ra",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB700)
                )
            ) {
                Text("Thử lại")
            }
        }
    }
}

@Composable
private fun EmptyState(categoryName: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🍽️",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Chưa có sản phẩm nào",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Text(
                text = "Danh mục $categoryName hiện tại chưa có sản phẩm",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
private fun ProductItemCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Product Image
            AsyncImage(
                model = getFullImageUrl(product.hinh_anh),
                contentDescription = product.ten_san_pham,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product Name
            Text(
                text = product.ten_san_pham,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Product Description
            Text(
                text = product.mo_ta ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price and Add button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatPrice(product.gia_co_ban),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF8F00)
                )

                // Add to cart button
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onAddToCart(product) },  // Sửa này
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFB700)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingProductGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(6) {
            LoadingProductCard()
        }
    }
}

@Composable
private fun LoadingProductCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Image skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Description skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Price skeleton
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }
    }
}

private fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    return "${formatter.format(price)}đ"
}
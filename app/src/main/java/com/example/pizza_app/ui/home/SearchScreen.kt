package com.example.pizza_app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pizza_app.R
import com.example.pizza_app.data.model.Product
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val results by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Top Bar với nút back
        TopAppBar(
            title = { Text("Tìm kiếm sản phẩm") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = { query = it
                    viewModel.clearResults()},
                label = { Text("Nhập tên sản phẩm...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFB700),
                    focusedLabelColor = Color(0xFFFFB700)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Nút tìm kiếm
            Button(
                onClick = {
                    if (query.isNotEmpty()) {
                        viewModel.search(query)
                    } else {
                        viewModel.clearResults()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB700)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tìm kiếm", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị kết quả
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFFFB700)
                        )
                    }
                }

                query.isEmpty() || (results.isEmpty() && !viewModel.hasSearched.collectAsState().value) -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🔍",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Nhập tên sản phẩm và bấm tìm kiếm",
                                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                    }
                }

                results.isEmpty() && viewModel.hasSearched.collectAsState().value -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "😔",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Không tìm thấy sản phẩm nào",
                                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                            Text(
                                text = "Thử tìm kiếm với từ khóa khác",
                                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }

                else -> {
                    // Hiển thị số kết quả
                    Text(
                        text = "Tìm thấy ${results.size} sản phẩm",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(results) { product ->
                            ProductSearchItem(
                                product = product,
                                onClick = {
                                    navController.navigate("product_detail/${product.ma_san_pham}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductSearchItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hình ảnh sản phẩm
            AsyncImage(
                model = product.hinh_anh,
                contentDescription = product.ten_san_pham,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.pizza_placeholder),
                error = painterResource(R.drawable.pizza_placeholder)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Thông tin sản phẩm
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.ten_san_pham,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                product.mo_ta?.let { moTa ->
                    Text(
                        text = moTa,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                        .format(product.gia_co_ban),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFFF8F00),
                    fontWeight = FontWeight.Bold
                )
            }

            // Icon mới nếu có
            if (product.moi == 1) {
                Card(
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE53935))
                ) {
                    Text(
                        text = "Mới",
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
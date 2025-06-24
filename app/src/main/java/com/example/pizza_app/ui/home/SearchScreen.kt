package com.example.pizza_app.ui.home

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.ui.home.ProductItem

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchScreen(
//    navController: NavController
//) {
//    val viewModel: SearchViewModel = viewModel()
//    val searchQuery by viewModel.searchQuery.collectAsState()
//    val searchResults by viewModel.searchResults.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val hasSearched by viewModel.hasSearched.collectAsState()
//
//    val focusRequester = remember { FocusRequester() }
//    val keyboardController = LocalSoftwareKeyboardController.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF8F9FA))
//    ) {
//        // Top App Bar với thanh tìm kiếm
//        TopAppBar(
//            title = { },
//            navigationIcon = {
//                IconButton(
//                    onClick = { navController.navigateUp() }
//                ) {
//                    Icon(
//                        Icons.Default.ArrowBack,
//                        contentDescription = "Quay lại",
//                        tint = Color(0xFF333333)
//                    )
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.Transparent
//            )
//        )
//
//        // Search Bar
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            shape = RoundedCornerShape(25.dp),
//            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White)
//        ) {
//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = viewModel::updateSearchQuery,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusRequester(focusRequester),
//                placeholder = {
//                    Text(
//                        "Tìm pizza, đồ uống, món ăn...",
//                        color = Color.Gray,
//                        fontSize = 16.sp
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        Icons.Default.Search,
//                        contentDescription = "Tìm kiếm",
//                        tint = Color(0xFFFFB700),
//                        modifier = Modifier.size(24.dp)
//                    )
//                },
//                trailingIcon = {
//                    Row {
//                        if (searchQuery.isNotEmpty()) {
//                            IconButton(
//                                onClick = viewModel::clearSearch
//                            ) {
//                                Icon(
//                                    Icons.Default.Clear,
//                                    contentDescription = "Xóa",
//                                    tint = Color.Gray,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                            }
//                        }
//
//                        // Search Button
//                        Box(
//                            modifier = Modifier
//                                .size(40.dp)
//                                .background(Color(0xFFFFB700), shape = CircleShape)
//                                .clickable {
//                                    if (searchQuery.isNotEmpty()) {
//                                        viewModel.searchProducts()
//                                        keyboardController?.hide()
//                                    }
//                                },
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                Icons.Default.Search,
//                                contentDescription = "Tìm kiếm",
//                                tint = Color.White,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.width(8.dp))
//                    }
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color.Transparent,
//                    unfocusedBorderColor = Color.Transparent,
//                    cursorColor = Color(0xFFFFB700)
//                ),
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                keyboardActions = KeyboardActions(
//                    onSearch = {
//                        if (searchQuery.isNotEmpty()) {
//                            viewModel.searchProducts()
//                            keyboardController?.hide()
//                        }
//                    }
//                )
//            )
//        }
//
//        // Content
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp)
//        ) {
//            when {
//                !hasSearched -> {
//                    // Initial state - hiển thị gợi ý
//                    InitialSearchState()
//                }
//                isLoading -> {
//                    // Loading state
//                    SearchLoadingState()
//                }
//                searchResults.isEmpty() -> {
//                    // No results
//                    NoResultsState(searchQuery = searchQuery)
//                }
//                else -> {
//                    // Results
//                    SearchResultsContent(
//                        results = searchResults,
//                        navController = navController,
//                        searchQuery = searchQuery
//                    )
//                }
//            }
//        }
//    }
//
//    // Auto focus vào search field khi vào màn hình
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }
//}
//
//@Composable
//private fun InitialSearchState() {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        // Pizza icon lớn
//        Box(
//            modifier = Modifier
//                .size(80.dp)
//                .background(
//                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
//                        colors = listOf(
//                            Color(0xFFFFB700),
//                            Color(0xFFFF8F00)
//                        )
//                    ),
//                    shape = CircleShape
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "🍕",
//                fontSize = 40.sp
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Text(
//            text = "Tìm kiếm món ăn yêu thích",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF333333)
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = "Pizza, đồ uống, món ăn nhanh...",
//            fontSize = 14.sp,
//            color = Color.Gray,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Suggestions
//        Text(
//            text = "Gợi ý tìm kiếm:",
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Medium,
//            color = Color(0xFF333333)
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        val suggestions = listOf("Pizza Margherita", "Coca Cola", "Gà rán", "Bánh mì")
//        suggestions.forEach { suggestion ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 4.dp)
//                    .clickable { /* Handle suggestion click */ },
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//            ) {
//                Text(
//                    text = suggestion,
//                    modifier = Modifier.padding(16.dp),
//                    fontSize = 14.sp
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun SearchLoadingState() {
//    Column {
//        Text(
//            text = "Đang tìm kiếm...",
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(vertical = 16.dp)
//        )
//
//        SkeletonSearchResults()
//    }
//}
//
//@Composable
//private fun NoResultsState(searchQuery: String) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "😔",
//            fontSize = 48.sp
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "Không tìm thấy kết quả",
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF333333)
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = "Không có sản phẩm nào phù hợp với \"$searchQuery\"",
//            fontSize = 14.sp,
//            color = Color.Gray,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Text(
//            text = "Thử tìm kiếm với từ khóa khác nhé!",
//            fontSize = 14.sp,
//            color = Color(0xFFFFB700),
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//@Composable
//private fun SearchResultsContent(
//    results: List<Product>,
//    navController: NavController,
//    searchQuery: String
//) {
//    Column {
//        Text(
//            text = "Tìm thấy ${results.size} kết quả cho \"$searchQuery\"",
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Medium,
//            modifier = Modifier.padding(vertical = 16.dp),
//            color = Color(0xFF333333)
//        )
//
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier.fillMaxWidth(),
//            contentPadding = PaddingValues(vertical = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(results) { product ->
//                ProductItem(product = product, navController = navController)
//            }
//        }
//    }
//}
//
//@Composable
//private fun SkeletonSearchResults() {
//    val shimmerColors = listOf(
//        Color.LightGray.copy(alpha = 0.6f),
//        Color.LightGray.copy(alpha = 0.2f),
//        Color.LightGray.copy(alpha = 0.6f)
//    )
//
//    val transition = rememberInfiniteTransition(label = "shimmer")
//    val translateAnim = transition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1000f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1200),
//            repeatMode = RepeatMode.Restart
//        ),
//        label = "shimmer"
//    )
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier = Modifier.fillMaxWidth(),
//        contentPadding = PaddingValues(vertical = 8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(6) {
//            SkeletonProductItem(
//                shimmerColors = shimmerColors,
//                translateAnim = translateAnim.value
//            )
//        }
//    }
//}
//
//@Composable
//private fun SkeletonProductItem(
//    shimmerColors: List<Color>,
//    translateAnim: Float
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(220.dp),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(12.dp)
//        ) {
//            // Skeleton image
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(120.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .background(
//                        androidx.compose.ui.graphics.Brush.linearGradient(
//                            colors = shimmerColors,
//                            start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
//                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
//                        )
//                    )
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Skeleton product name
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .height(16.dp)
//                    .clip(RoundedCornerShape(4.dp))
//                    .background(
//                        androidx.compose.ui.graphics.Brush.linearGradient(
//                            colors = shimmerColors,
//                            start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
//                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
//                        )
//                    )
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Skeleton price
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(0.6f)
//                    .height(14.dp)
//                    .clip(RoundedCornerShape(4.dp))
//                    .background(
//                        androidx.compose.ui.graphics.Brush.linearGradient(
//                            colors = shimmerColors,
//                            start = androidx.compose.ui.geometry.Offset(translateAnim - 200f, 0f),
//                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
//                        )
//                    )
//            )
//        }
//    }
//}
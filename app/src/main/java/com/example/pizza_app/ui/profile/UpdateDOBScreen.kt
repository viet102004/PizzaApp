@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.draw.alpha
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun UpdateDOBScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf("19 tháng 10, 2004") }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFC8C8A9),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        // Header với TopAppBar
        TopAppBar(
            title = {
                Text(
                    text = "Thay đổi ngày sinh",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Cập nhật ngày sinh của bạn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Date picker field (clickable)
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = { },
                        label = { Text("Ngày sinh") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color(0xFFCCCCCC),
                            disabledLabelColor = Color(0xFF666666),
                            disabledTextColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // TODO: Lưu ngày sinh
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Lưu thông tin thay đổi",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        InfiniteScrollDatePickerDialog(
            currentDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun InfiniteScrollDatePickerDialog(
    currentDate: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDay by remember { mutableStateOf(19) }
    var selectedMonth by remember { mutableStateOf(10) }
    var selectedYear by remember { mutableStateOf(2004) }

    val months = listOf(
        "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
        "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chỉnh sửa ngày sinh của bạn",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Day Picker
                        InfiniteScrollPicker(
                            items = (1..31).map { it.toString() },
                            selectedIndex = selectedDay - 1,
                            onItemSelected = { index -> selectedDay = index + 1 },
                            modifier = Modifier.weight(1f)
                        )

                        // Month Picker
                        InfiniteScrollPicker(
                            items = months,
                            selectedIndex = selectedMonth - 1,
                            onItemSelected = { index -> selectedMonth = index + 1 },
                            modifier = Modifier.weight(2f)
                        )

                        // Year Picker
                        InfiniteScrollPicker(
                            items = (1950..2025).map { it.toString() },
                            selectedIndex = selectedYear - 1950,
                            onItemSelected = { index -> selectedYear = 1950 + index },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Selection background highlight
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(
                                Color(0xFFFF69B4).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Save Button
                Button(
                    onClick = {
                        val monthName = months[selectedMonth - 1].lowercase()
                        val formattedDate = "$selectedDay $monthName, $selectedYear"
                        onDateSelected(formattedDate)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF69B4)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Lưu",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InfiniteScrollPicker(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Optimized: Only create 100 repetitions instead of 1000
    val infiniteItems = remember(items) {
        val repeatedItems = mutableListOf<String>()
        repeat(100) {
            items.forEach { item ->
                repeatedItems.add(item)
            }
        }
        repeatedItems
    }

    val middleStart = remember(items) { 50 * items.size }
    val itemHeight = 40.dp

    // Khởi tạo vị trí ban đầu của list ở giữa
    LaunchedEffect(Unit) {
        val initialIndex = middleStart + selectedIndex
        listState.scrollToItem(initialIndex)
    }

    // Chỉ tự động chọn item nằm trong highlight zone khi user dừng scroll (không auto-scroll)
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportSize.height / 2

            // Tìm item nằm trong highlight zone
            val highlightItem = layoutInfo.visibleItemsInfo.find { itemInfo ->
                val itemCenter = itemInfo.offset + itemInfo.size / 2
                abs(itemCenter - viewportCenter) < 20 // Cùng threshold với highlight zone
            }

            highlightItem?.let { item ->
                val actualIndex = item.index % items.size
                if (actualIndex != selectedIndex) {
                    onItemSelected(actualIndex) // Tự động chọn item trong highlight zone
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 80.dp)
    ) {
        items(infiniteItems.size) { index ->
            val item = infiniteItems[index]
            val actualIndex = index % items.size
            val isSelected = actualIndex == selectedIndex

            // Calculate distance from center for fade effect
            val centerIndex = middleStart + selectedIndex
            val distance = abs(index - centerIndex)
            val alpha = when {
                distance == 0 -> 1f  // Selected item
                distance == 1 -> 0.7f  // Adjacent items
                distance == 2 -> 0.4f  // Further items
                else -> 0.2f  // Distant items
            }

            // Check if this item is currently in the highlight zone (center area)
            val isInHighlightZone = remember(listState, index) {
                derivedStateOf {
                    val layoutInfo = listState.layoutInfo
                    val viewportCenter = layoutInfo.viewportSize.height / 2
                    val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }
                    itemInfo?.let {
                        val itemCenter = it.offset + it.size / 2
                        abs(itemCenter - viewportCenter) < 20 // Item trong vùng highlight (20dp threshold)
                    } ?: false
                }
            }.value

            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .padding(vertical = 8.dp)
                    .alpha(alpha)
                    .clickable {
                        // Chỉ cho phép chọn bằng tay (click)
                        if (actualIndex != selectedIndex) {
                            onItemSelected(actualIndex)
                        }
                    },
                fontSize = if (isSelected) 20.sp else if (distance <= 1) 18.sp else 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    isSelected -> Color(0xFFFF1493) // Màu hồng đậm khi được chọn chính thức
                    isInHighlightZone -> Color(0xFFFF69B4) // Màu hồng vừa khi nằm trong vùng highlight
                    else -> Color.Black // Màu đen bình thường
                },
                textAlign = TextAlign.Center
            )
        }
    }
}
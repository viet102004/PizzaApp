package com.example.pizza_app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun SupportChatScreen(navController: NavController,
    onBackPressed: () -> Unit = {}
) {
    var messages by remember { mutableStateOf(
        listOf(
            ChatMessage(
                message = "Xin chào! Tôi có thể giúp gì cho bạn hôm nay?",
                isFromUser = false
            ),
            ChatMessage(
                message = "Chào bạn, tôi muốn hỏi về đơn hàng của mình",
                isFromUser = true
            ),
            ChatMessage(
                message = "Tất nhiên! Bạn có thể cung cấp mã đơn hàng để tôi kiểm tra giúp bạn không?",
                isFromUser = false
            )
        )
    )}

    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Handle back button press
    BackHandler {
        onBackPressed()
    }

    // Auto focus on text field when screen loads and show keyboard immediately
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100) // Small delay to ensure UI is ready
        focusRequester.requestFocus()
    }
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Hỗ trợ khách hàng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController?.popBackStack()// Đảm bảo callback được gọi để quay lại trang trước
                    }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color.Black
                    )
                }
            },
            backgroundColor = Color.White,
            elevation = 2.dp
        )

        // Messages List - clickable to focus text field
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .clickable {
                    focusRequester.requestFocus()
                },
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                ChatMessageItem(message = message)
            }
        }

        // Message Input
        Surface(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    placeholder = { Text("Nhập tin nhắn...") },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFFFF6B35),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    maxLines = 3,
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (messageText.trim().isNotEmpty()) {
                                messages = messages + ChatMessage(
                                    message = messageText.trim(),
                                    isFromUser = true
                                )
                                messageText = ""
                                keyboardController?.hide()

                                // Simulate auto response
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(1000)
                                    messages = messages + ChatMessage(
                                        message = "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi sớm nhất có thể.",
                                        isFromUser = false
                                    )
                                }
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (messageText.trim().isNotEmpty()) {
                            messages = messages + ChatMessage(
                                message = messageText.trim(),
                                isFromUser = true
                            )
                            messageText = ""

                            // Simulate auto response
                            coroutineScope.launch {
                                kotlinx.coroutines.delay(1000)
                                messages = messages + ChatMessage(
                                    message = "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi sớm nhất có thể.",
                                    isFromUser = false
                                )
                            }
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    backgroundColor = Color(0xFFFF6B35)
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Gửi",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser)
            Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CS",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isFromUser)
                Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                    bottomEnd = if (message.isFromUser) 4.dp else 16.dp
                ),
                color = if (message.isFromUser)
                    Color(0xFFFF6B35) else Color.White,
                elevation = 1.dp
            ) {
                Text(
                    text = message.message,
                    modifier = Modifier.padding(12.dp),
                    color = if (message.isFromUser) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }

            Text(
                text = timeString,
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(
                    top = 4.dp,
                    start = if (message.isFromUser) 0.dp else 8.dp,
                    end = if (message.isFromUser) 8.dp else 0.dp
                )
            )
        }

        if (message.isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
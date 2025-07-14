@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pizza_app.R
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.getFullImageUrl
import com.example.pizza_app.data.model.UserPreferences
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileDetailsScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: UserUpdateViewModel = viewModel()

    val user by UserManager.currentUser.collectAsState()
    val isUploadingAvatar by viewModel.isUploadingAvatar.collectAsState()
    val message by viewModel.message.collectAsState()
    val success by viewModel.success.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Log.d("ProfileDetails", "Email hiện tại: ${user?.email}")

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    // Animation cho nút reload
    val rotationState = remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = if (isRefreshing) rotationState.value + 360f else rotationState.value,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "refresh_rotation"
    )

    // Cập nhật rotation state khi refresh
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            rotationState.value += 360f
        }
    }

    // Pull refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshUserFromServer(context) }
    )

    // Xử lý kết quả upload ảnh và tự động refresh
    LaunchedEffect(success, message) {
        if (success && message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            // Tự động refresh sau khi upload thành công
            if (message.contains("ảnh đại diện")) {
                viewModel.refreshUserFromServer(context)
            }
            viewModel.resetState()
        } else if (!success && message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    // Launcher chọn ảnh từ gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            viewModel.updateAvatar(it, context)
        }
    }

    // Launcher chụp ảnh từ camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && selectedImageUri != null) {
            viewModel.updateAvatar(selectedImageUri!!, context)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshUserFromServer(context)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Format ngày sinh từ yyyy-MM-dd thành dd/MM/yyyy
    val formattedBirthDate = remember(user?.ngay_sinh) {
        user?.ngay_sinh?.let { dateStr ->
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(dateStr)
                date?.let { outputFormat.format(it) } ?: "Chưa cập nhật"
            } catch (e: Exception) {
                "Chưa cập nhật"
            }
        } ?: "Chưa cập nhật"
    }

    // Wrap toàn bộ content trong Box với pull refresh
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            TopAppBar(
                title = {
                    Text("Thông tin tài khoản", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (!isRefreshing) {
                                viewModel.refreshUserFromServer(context)
                            }
                        },
                        enabled = !isRefreshing
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = if (isRefreshing) Color.Gray else Color.Black,
                            modifier = Modifier.rotate(animatedRotation)
                                .size(30.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar với loading overlay
                        Box(
                            modifier = Modifier.size(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val imageUrl = user?.anh_dai_dien

                            if (!imageUrl.isNullOrBlank()) {
                                // Hiển thị ảnh đại diện nếu có
                                Image(
                                    painter = rememberAsyncImagePainter(getFullImageUrl(imageUrl)),
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                // Hiển thị icon Person nếu không có ảnh
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .background(
                                            Color(0xFFE0E0E0),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Person Icon",
                                        tint = Color(0xFF757575),
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            }

                            // Loading overlay khi đang upload
                            if (isUploadingAvatar) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.5f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showImageSourceDialog = true },
                            enabled = !isUploadingAvatar,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isUploadingAvatar) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.Black,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Đang tải lên...", color = Color.Black, fontWeight = FontWeight.Medium)
                            } else {
                                Text("Thay đổi ảnh", color = Color.Black, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ProfileInfoItem("Họ và tên", user?.ho_ten ?: "(Chưa cập nhật)") {
                            navController.navigate("update_name")
                        }
                        ProfileInfoItem("Số điện thoại", user?.so_dien_thoai ?: "(Chưa có)") {
                            navController.navigate("update_phone")
                        }
                        ProfileInfoItem("Email", user?.email ?: "(Chưa có)") {
                            navController.navigate("update_email")
                        }
                        ProfileInfoItem("Ngày sinh", formattedBirthDate) {
                            navController.navigate("update_dob")
                        }
                        ProfileInfoItem("Mật khẩu", "***", isLast = true) {
                            navController.navigate("update_password")
                        }
                    }
                }
            }
        }

        // Pull refresh indicator
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

    // Dialog chọn nguồn ảnh
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Chọn ảnh", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn muốn chọn ảnh từ đâu?") },
            confirmButton = {
                Row {
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        selectedImageUri = ImageUtils.createTempImageUri(context)
                        selectedImageUri?.let { cameraLauncher.launch(it) }
                    }) {
                        Text("Camera", color = Color(0xFFFFB700))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        imagePickerLauncher.launch("image/*")
                    }) {
                        Text("Thư viện", color = Color(0xFFFFB700))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showImageSourceDialog = false }) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun ProfileInfoItem(
    label: String,
    value: String,
    isLast: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = value,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                color = Color.Black
            )
            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
        }
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(top = 12.dp),
                color = Color(0xFFE0E0E0)
            )
        }
    }
}
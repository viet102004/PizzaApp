@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pizza_app.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pizza_app.R
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.data.source.getFullImageUrl
import com.example.pizza_app.data.model.UserPreferences

@Composable
fun ProfileDetailsScreen(navController: NavController) {
    val context = LocalContext.current

    val userState = produceState(initialValue = UserManager.currentUser) {
        while (true) {
            value = UserManager.currentUser
            kotlinx.coroutines.delay(500)
        }
    }
    val user = userState.value


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            uploadImage(it, user?.ma_nguoi_dung ?: -1) { newAvatarPath ->
                isUploading = false
                if (newAvatarPath != null) {
                    val updatedUser = user?.copy(anh_dai_dien = newAvatarPath)
                    if (updatedUser != null) {
                        UserManager.currentUser = updatedUser
                        UserPreferences(context).saveUser(updatedUser)
                        Toast.makeText(context, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show()
                }
            }
            isUploading = true
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && selectedImageUri != null) {
            uploadImage(selectedImageUri!!, user?.ma_nguoi_dung ?: -1) { newAvatarPath ->
                isUploading = false
                if (newAvatarPath != null) {
                    val updatedUser = user?.copy(anh_dai_dien = newAvatarPath)
                    if (updatedUser != null) {
                        UserManager.currentUser = updatedUser
                        UserPreferences(context).saveUser(updatedUser)
                        Toast.makeText(context, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Lỗi khi tải ảnh lên", Toast.LENGTH_SHORT).show()
                }
            }
            isUploading = true
        }
    }

    var showImageSourceDialog by remember { mutableStateOf(false) }
    val avatarUrl = user?.anh_dai_dien ?: ""

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
                    val imageUrl = selectedImageUri?.toString() ?: getFullImageUrl(avatarUrl)

                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Avatar",
                        modifier = Modifier.size(100.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showImageSourceDialog = true },
                        enabled = !isUploading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB700)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isUploading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp), color = Color.Black, strokeWidth = 2.dp
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
                    ProfileInfoItem("Ngày sinh", "Chưa cập nhật") {
                        navController.navigate("update_dob")
                    }
                    ProfileInfoItem("Mật khẩu", "***", isLast = true) {
                        navController.navigate("update_password")
                    }
                }
            }
        }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Chọn ảnh", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn muốn chọn ảnh từ đâu?") },
            confirmButton = {
                Row {
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        selectedImageUri = createImageUri(context)
                        selectedImageUri?.let { cameraLauncher.launch(it) }
                    }) { Text("Camera") }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        imagePickerLauncher.launch("image/*")
                    }) { Text("Thư viện") }
                }
            },
            dismissButton = {
                TextButton(onClick = { showImageSourceDialog = false }) {
                    Text("Hủy")
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
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp)
    ) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF666666))
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(value, modifier = Modifier.weight(1f), fontSize = 16.sp, color = Color.Black)
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
        if (!isLast) {
            HorizontalDivider(modifier = Modifier.padding(top = 12.dp), color = Color(0xFFE0E0E0))
        }
    }
}

private fun createImageUri(context: android.content.Context): Uri? {
    return try {
        val contentResolver = context.contentResolver
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI.let { uri ->
            contentResolver.insert(uri, android.content.ContentValues())
        }
    } catch (e: Exception) {
        null
    }
}

private fun uploadImage(uri: Uri, userId: Int, onComplete: (String?) -> Unit) {
    // TODO: Gọi API thực tế. Đây là bản giả lập
    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
        val fakePath = "/images/user_${userId}.jpg"
        onComplete(fakePath)
    }, 2000)
}

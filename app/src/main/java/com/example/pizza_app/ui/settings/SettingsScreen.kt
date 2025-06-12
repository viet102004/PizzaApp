package com.example.pizza_app.ui.settings


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = if (uiState.language == "vi") "Cài đặt" else "Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = if (uiState.language == "vi") "Quay lại" else "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingSection(
                    title = if (uiState.language == "vi") "Giao diện" else "Appearance",
                    items = listOf(
                        SettingItem(
                            icon = if (uiState.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            title = if (uiState.language == "vi") "Chế độ tối" else "Dark Mode",
                            subtitle = if (uiState.language == "vi") {
                                if (uiState.isDarkMode) "Đang bật" else "Đang tắt"
                            } else {
                                if (uiState.isDarkMode) "Enabled" else "Disabled"
                            },
                            hasSwitch = true,
                            switchState = uiState.isDarkMode,
                            onSwitchChange = { viewModel.toggleDarkMode() }
                        )
                    )
                )
            }

            item {
                SettingSection(
                    title = if (uiState.language == "vi") "Ngôn ngữ" else "Language",
                    items = listOf(
                        SettingItem(
                            icon = Icons.Default.Language,
                            title = if (uiState.language == "vi") "Ngôn ngữ" else "Language",
                            subtitle = if (uiState.language == "vi") "Tiếng Việt" else "English",
                            hasArrow = true,
                            onClick = { viewModel.showLanguageDialog() }
                        )
                    )
                )
            }

//            item {
//                SettingSection(
//                    title = if (uiState.language == "vi") "Thông tin ứng dụng" else "App Info",
//                    items = listOf(
//                        SettingItem(
//                            icon = Icons.Default.Info,
//                            title = if (uiState.language == "vi") "Phiên bản" else "Version",
//                            subtitle = "1.0.0",
//                            hasArrow = false
//                        ),
//                        SettingItem(
//                            icon = Icons.Default.Help,
//                            title = if (uiState.language == "vi") "Trợ giúp" else "Help & Support",
//                            subtitle = if (uiState.language == "vi") "Liên hệ hỗ trợ" else "Contact support",
//                            hasArrow = true,
//                            onClick = { /* Navigate to help */ }
//                        )
//                    )
//                )
//            }
        }
    }

    // Language Selection Dialog
    if (uiState.showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = uiState.language,
            onLanguageSelected = { language ->
                viewModel.setLanguage(language)
                viewModel.dismissLanguageDialog()
            },
            onDismiss = { viewModel.dismissLanguageDialog() }
        )
    }
}

@Composable
fun SettingSection(
    title: String,
    items: List<SettingItem>
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingItemRow(item = item)
                    if (index < items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingItemRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.onClick != null) {
                item.onClick?.invoke()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (item.subtitle.isNotEmpty()) {
                Text(
                    text = item.subtitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        when {
            item.hasSwitch -> {
                Switch(
                    checked = item.switchState,
                    onCheckedChange = item.onSwitchChange ?: {}
                )
            }
            item.hasArrow -> {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (currentLanguage == "vi") "Chọn ngôn ngữ" else "Select Language",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                LanguageOption(
                    language = "en",
                    displayName = "English",
                    isSelected = currentLanguage == "en",
                    onSelect = { onLanguageSelected("en") }
                )
                LanguageOption(
                    language = "vi",
                    displayName = "Tiếng Việt",
                    isSelected = currentLanguage == "vi",
                    onSelect = { onLanguageSelected("vi") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(if (currentLanguage == "vi") "Hủy" else "Cancel")
            }
        }
    )
}

@Composable
fun LanguageOption(
    language: String,
    displayName: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = displayName,
            fontSize = 16.sp
        )
    }
}

data class SettingItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String = "",
    val hasSwitch: Boolean = false,
    val switchState: Boolean = false,
    val onSwitchChange: ((Boolean) -> Unit)? = null,
    val hasArrow: Boolean = false,
    val onClick: (() -> Unit)? = null
)
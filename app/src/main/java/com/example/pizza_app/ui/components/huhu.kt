package com.example.pizza_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.pizza_app.data.VietnamAddressData
import com.example.pizza_app.data.model.Province
import com.example.pizza_app.data.model.District
import com.example.pizza_app.data.model.Ward

@Composable
fun ProvincePickerDialog(
    onDismiss: () -> Unit,
    onProvinceSelected: (Province) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chọn Tỉnh/Thành phố",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436)
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF666666)
                        )
                    }
                }

                Divider(color = Color(0xFFE0E0E0))

                // List
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(VietnamAddressData.provinces) { province ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onProvinceSelected(province) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = province.name,
                                fontSize = 16.sp,
                                color = Color(0xFF2D3436)
                            )
                        }

                        if (province != VietnamAddressData.provinces.last()) {
                            Divider(
                                color = Color(0xFFF0F0F0),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DistrictPickerDialog(
    province: Province,
    onDismiss: () -> Unit,
    onDistrictSelected: (District) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Chọn Quận/Huyện",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3436)
                        )
                        Text(
                            text = province.name,
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF666666)
                        )
                    }
                }

                Divider(color = Color(0xFFE0E0E0))

                // List
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(province.districts) { district ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDistrictSelected(district) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = district.name,
                                fontSize = 16.sp,
                                color = Color(0xFF2D3436)
                            )
                        }

                        if (district != province.districts.last()) {
                            Divider(
                                color = Color(0xFFF0F0F0),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WardPickerDialog(
    province: Province,
    district: District,
    onDismiss: () -> Unit,
    onWardSelected: (Ward) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Chọn Phường/Xã",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3436)
                        )
                        Text(
                            text = "${district.name}, ${province.name}",
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF666666)
                        )
                    }
                }

                Divider(color = Color(0xFFE0E0E0))

                // List
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(district.wards) { ward ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onWardSelected(ward) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ward.name,
                                fontSize = 16.sp,
                                color = Color(0xFF2D3436)
                            )
                        }

                        if (ward != district.wards.last()) {
                            Divider(
                                color = Color(0xFFF0F0F0),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
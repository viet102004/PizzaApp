package com.example.pizza_app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.source.getFullImageUrl

@Composable
fun ProductItem(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                navController.navigate("product_detail/${product.ma_san_pham}")
            },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Box để chứa ảnh và nhãn "Mới"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                AsyncImage(
                    model = getFullImageUrl(product.hinh_anh),
                    contentDescription = product.ten_san_pham,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Hiển thị nhãn "Mới" nếu sản phẩm là mới
                if (product.moi == 1) { // Giả sử có thuộc tính la_san_pham_moi
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(
                                color = Color.Red,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "MỚI",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tên sản phẩm với nhãn "Mới" inline (tùy chọn khác)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.ten_san_pham,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f)
                )

            }

            Text(
                text = product.gia_co_ban.formatCurrency(),
                style = MaterialTheme.typography.body2
            )
        }
    }
}


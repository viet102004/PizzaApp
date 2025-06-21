package com.example.pizza_app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
            AsyncImage(
                model = getFullImageUrl(product.hinh_anh),
                contentDescription = product.ten_san_pham,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.ten_san_pham, style = MaterialTheme.typography.body1)
            Text(text = "${product.gia_co_ban.toInt()}đ", style = MaterialTheme.typography.body2)
        }
    }
}

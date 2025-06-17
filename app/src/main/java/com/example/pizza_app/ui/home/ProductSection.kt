package com.example.pizza_app.ui.home
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pizza_app.data.model.Product
@Composable
fun ProductSection(products: List<Product>, navController: NavController) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Ăn gì hôm nay!!!",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                top = 0.dp,
                bottom = 80.dp // Thêm padding bottom để tránh bị che bởi bottom navigation
            ),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(products) { product ->
                ProductItem(product = product, navController = navController)
            }
        }
    }
}
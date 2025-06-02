package com.example.pizza_app.ui.cart

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CartScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Cart") }) }
    ) { padding ->
        Text(
            text = "Your cart is empty!",
            modifier = Modifier.padding(padding)
        )
    }
}

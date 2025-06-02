package com.example.pizza_app.ui.order

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OrderScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Orders") }) }
    ) { padding ->
        Text(
            text = "Your orders will appear here.",
            modifier = Modifier.padding(padding)
        )
    }
}

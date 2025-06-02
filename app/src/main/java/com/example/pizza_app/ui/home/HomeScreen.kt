package com.example.pizza_app.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Home") }) }
    ) { paddingValues ->
        Text(
            text = "Welcome to Pizza App!",
            modifier = Modifier.padding(paddingValues)
        )
    }

}

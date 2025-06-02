package com.example.pizza_app.ui.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { padding ->
        Text(
            text = "User profile screen",
            modifier = Modifier.padding(padding)
        )
    }
}

package com.example.pizza_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.pizza_app.ui.MainScreen
import com.example.pizza_app.ui.auth.LoginScreen
import com.example.pizza_app.ui.setFullScreen
import com.example.pizza_app.ui.theme.Pizza_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        //setFullScreen()
        setContent {
            Pizza_appTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}


package com.example.pizza_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.pizza_app.data.model.UserPreferences
import com.example.pizza_app.data.source.UserManager
import com.example.pizza_app.ui.MainScreen
import com.example.pizza_app.ui.auth.ForgotPasswordScreen
import com.example.pizza_app.ui.auth.LoginScreen
import com.example.pizza_app.ui.auth.RegisterScreen
import com.example.pizza_app.ui.cart.PayViewModel
import com.example.pizza_app.ui.theme.Pizza_appTheme
import dagger.hilt.android.HiltAndroidApp

class MainActivity : ComponentActivity() {
    private var payViewModel: PayViewModel? = null

    fun setPayViewModel(viewModel: PayViewModel) {
        this.payViewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userPreferences = UserPreferences(applicationContext)
        val savedUser = userPreferences.getUser()
        UserManager.setUser(savedUser)

        setContent {
            Pizza_appTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }

        handleDeeplink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Xử lý deeplink khi app đang chạy
        handleDeeplink(intent)
    }

    private fun handleDeeplink(intent: Intent?) {
        val data: Uri? = intent?.data
        data?.let { uri ->
            Log.d("Deeplink", "Received deeplink: $uri")

            when (uri.scheme) {
                "pizzaapp" -> {
                    // Custom scheme: pizzaapp://payment?orderId=xxx&resultCode=xxx
                    if (uri.host == "payment") {
                        handlePaymentCallback(uri)
                    }
                }
                "https" -> {
                    // HTTPS deeplink: https://yourdomain.com/payment-callback?orderId=xxx&resultCode=xxx
                    if (uri.host == "https://related-burro-selected.ngrok-free.app" && uri.path?.startsWith("/payment-callback") == true) {
                        handlePaymentCallback(uri)
                    }
                }
            }
        }
    }

    private fun handlePaymentCallback(uri: Uri) {
        val orderId = uri.getQueryParameter("orderId")
        val resultCode = uri.getQueryParameter("resultCode") ?: "1" // Default là thất bại
        val message = uri.getQueryParameter("message")

        Log.d("PaymentCallback", "orderId: $orderId, resultCode: $resultCode, message: $message")

        if (orderId != null) {
            // Gửi kết quả về ViewModel
            payViewModel?.handlePaymentReturn(orderId, resultCode)
        }
    }
}


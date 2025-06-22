package com.example.pizza_app.data.source

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.pizza_app.R
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.model.Order
import com.example.pizza_app.data.model.Product

object ItemXamp {
    val sampleOrders = listOf(
        Order(
            storeName = "Pizza KimChi",
            orderTime = "Hôm nay, 14:00",
            status = "Chờ xác nhận",
            items = listOf("Pizza Hải sản", "Pepsi", "Khoai tây chiên"),
            totalPrice = "199.000đ"
        ),
        Order(
            storeName = "The Pizza  - Quang Trung",
            orderTime = "Hôm qua, 18:30",
            status = "Đang đến",
            items = listOf("Pizza Phô mai", "Coca", "Gà rán"),
            totalPrice = "249.000đ"
        )
    )




}
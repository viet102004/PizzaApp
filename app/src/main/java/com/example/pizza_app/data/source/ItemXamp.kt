package com.example.pizza_app.data.source

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.pizza_app.R
import com.example.pizza_app.data.model.CartItem
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.model.Order
import com.example.pizza_app.data.model.Product

object ItemXamp {
    val sampleProducts = listOf(
        Product( 1, "Pizza Margherita", 99000, R.drawable.pizza1, "Pizza Tuyệt vời"),
        Product( 2,"Pepperoni Pizza", 119000, R.drawable.pizza2, "Pizza Tuyệt vời"),
        Product( 3,"Seafood Pizza", 139000, R.drawable.pizza3, "Pizza Tuyệt vời"),
        Product( 4,"Hawaiian Pizza", 109000, R.drawable.pizza4, "Pizza Tuyệt vời"),
        Product( 5,"Veggie Pizza", 95000, R.drawable.pizza5, "Pizza Tuyệt vời"),
        Product( 6,"BBQ Chicken Pizza", 129000, R.drawable.pizza6, "Pizza Tuyệt vời"),
        Product( 7,"BBQ Chicken Pizza", 129000, R.drawable.pizza6, "Pizza Tuyệt vời"),
        Product( 8,"BBQ Chicken Pizza", 129000, R.drawable.pizza6, "Pizza Tuyệt vời"),
        Product( 9,"BBQ Chicken Pizza", 129000, R.drawable.pizza6, "Pizza Tuyệt vời"),
        Product( 10,"BBQ Chicken Pizza", 129000, R.drawable.pizza6, "Pizza Tuyệt vời"),
    )
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
    val sampleCategories = listOf(
        Category("Pizza", R.drawable.ic_pizza),
        Category("Gà", R.drawable.ic_chicken),
        Category("Nước", R.drawable.ic_drink),
        Category("Mì Ý", R.drawable.ic_spaghetti),
        Category("Tráng miệng", R.drawable.ic_dessert),
        Category("Khuyến mãi", R.drawable.ic_discount)
    )

    val sampleCartItems = listOf(
        CartItem(
            id = "1",
            name = "Pizza Margherita",
            size = "M",
            thickness = "Mỏng",
            price = 149000.0,
            quantity = 1,
            img = R.drawable.pizza2
        ),
        CartItem(
            id = "2",
            name = "Pizza Pepperoni",
            size = "L",
            thickness = "Dày",
            price = 199000.0,
            quantity = 1,
            img = R.drawable.pizza2
        ),
        CartItem(
            id = "3",
            name = "Pizza Hải Sản",
            size = "XL",
            thickness = "Vừa",
            price = 299000.0,
            quantity = 1,
            img = R.drawable.pizza2
        ),
        CartItem(
            id = "4",
            name = "Pizza Thịt Nướng",
            size = "M",
            thickness = "Dày",
            price = 179000.0,
            quantity = 1,
            img = R.drawable.pizza2
        )
    )


}
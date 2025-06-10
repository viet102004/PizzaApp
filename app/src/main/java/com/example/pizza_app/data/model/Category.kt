package com.example.pizza_app.data.model

import androidx.annotation.DrawableRes

data class Category(
    val name: String,
    @DrawableRes val iconRes: Int
)
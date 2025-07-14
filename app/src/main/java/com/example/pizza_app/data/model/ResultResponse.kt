package com.example.pizza_app.data.model

data class ResultResponse(
    val message: String,
    val ma_don_hang: Int,
    val payment_url: String? = null,
    val phuong_thuc_thanh_toan: String? = null
)


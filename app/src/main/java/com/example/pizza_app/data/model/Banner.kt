package com.example.pizza_app.data.model

data class Banner(
    val ma_banner: Long,
    val url_hinh_anh: String,
    val ma_san_pham: Long? = null,
    val tieu_de: String?
)

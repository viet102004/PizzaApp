package com.example.pizza_app.data.model

import com.google.gson.annotations.SerializedName

data class ProductOption(
    val ma_loai_tuy_chon: Int,
    val ten_loai: String,
    val loai_lua_chon: String, // "radio", "checkbox", "single", "multiple"
    val bat_buoc: Boolean,
    val gia_tri: List<ProductOptionValue>
)

data class ProductOptionValue(
    val ma_gia_tri: Int,
    val ten_gia_tri: String,
    val gia_them: Double
)

data class ProductOptionsResponse(
    @SerializedName("tuy_chon")
    val tuy_chon: List<ProductOption>
)

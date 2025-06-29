package com.example.pizza_app.data.model

data class Province(
    val code: String,
    val name: String,
    val districts: List<District>
)

data class District(
    val code: String,
    val name: String,
    val wards: List<Ward>
)

data class Ward(
    val code: String,
    val name: String
)

package com.example.pizza_app.data.source

fun getFullImageUrl(relativePath: String?): String {
    val baseUrl = "https://related-burro-selected.ngrok-free.app"
    return if (relativePath.isNullOrBlank()) {
        ""
    } else {
        baseUrl + relativePath
    }
}
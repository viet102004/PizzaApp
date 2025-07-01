package com.example.pizza_app.data.source

fun mapDatabaseStatusToUI(dbStatus: String): String {
    return when (dbStatus) {
        "da_nhan", "da_xac_nhan" -> "cho_xac_nhan"
        "cho_xu_ly", "dang_chuan_bi", "dang_giao" -> "dang_giao"
        "hoan_thanh" -> "hoan_thanh"
        "da_huy" -> "da_huy"
        else -> "cho_xac_nhan"
    }
}

// Helper function to map UI status to database status
fun mapUIStatusToDatabase(uiStatus: String): List<String> {
    return when (uiStatus) {
        "cho_xac_nhan" -> listOf("da_nhan", "da_xac_nhan")
        "dang_giao" -> listOf("cho_xu_ly", "dang_chuan_bi", "dang_giao")
        "hoan_thanh" -> listOf("hoan_thanh")
        "da_huy" -> listOf("da_huy")
        else -> emptyList()
    }
}
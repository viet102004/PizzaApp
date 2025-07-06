package com.example.pizza_app.ui.home

// Thêm vào file utils hoặc ở đầu file ProductDetailScreen.kt

import java.text.SimpleDateFormat
import java.util.*

// Extension function để định dạng ngày tháng
// Sửa lại hàm formatDateTime để handle null
fun String?.formatDateTime(): String {
    if (this == null || this.isEmpty()) return "Không xác định"

    return try {
        // Thử với format không có 'T' trước
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        // Nếu parse lỗi, thử với ISO format (có 'T')
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

            val date = inputFormat.parse(this)
            outputFormat.format(date ?: Date())
        } catch (e2: Exception) {
            // Thử thêm format với milliseconds
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

                val date = inputFormat.parse(this)
                outputFormat.format(date ?: Date())
            } catch (e3: Exception) {
                // Nếu vẫn lỗi, trả về "Không xác định"
                "Không xác định"
            }
        }
    }
}

// Hàm chỉ lấy ngày (không có giờ)
fun String?.formatDateOnly(): String {
    if (this == null || this.isEmpty()) return "Không xác định"

    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val date = inputFormat.parse(this)
            outputFormat.format(date ?: Date())
        } catch (e2: Exception) {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val date = inputFormat.parse(this)
                outputFormat.format(date ?: Date())
            } catch (e3: Exception) {
                "Không xác định"
            }
        }
    }
}
// Hoặc nếu bạn muốn format khác:
fun String.formatDateTimeVN(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm, dd 'tháng' MM, yyyy", Locale.getDefault())

        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        this
    }
}

// Các format khác:
fun String.formatDateTimeShort(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        this
    }
}
fun String?.formatDateTimeByParsing(): String {
    if (this.isNullOrEmpty()) return "Không xác định"

    return try {
        // Xử lý các format phổ biến:
        // "2024-01-15T14:30:00" hoặc "2024-01-15 14:30:00"
        val cleanString = this.replace("T", " ") // Thay T bằng space

        // Tách ngày và giờ
        val parts = cleanString.split(" ")
        if (parts.size < 2) return "Không xác định"

        val datePart = parts[0] // "2024-01-15"
        val timePart = parts[1] // "14:30:00"

        // Tách ngày
        val dateComponents = datePart.split("-")
        if (dateComponents.size != 3) return "Không xác định"

        val year = dateComponents[0]
        val month = dateComponents[1]
        val day = dateComponents[2]

        // Tách giờ
        val timeComponents = timePart.split(":")
        if (timeComponents.size < 2) return "Không xác định"

        val hour = timeComponents[0]
        val minute = timeComponents[1]

        // Format lại: "HH:mm dd/MM/yyyy"
        "$hour:$minute $day/$month/$year"

    } catch (e: Exception) {
        "Không xác định"
    }
}

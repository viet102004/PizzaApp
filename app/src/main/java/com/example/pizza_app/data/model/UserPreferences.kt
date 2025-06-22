package com.example.pizza_app.data.model

import android.content.Context
import android.provider.Settings.Global.putInt
import android.provider.Settings.Global.putString

class UserPreferences(context: Context) {
    private val dataStore = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        dataStore.edit().apply {
            putInt("ma_nguoi_dung", user.ma_nguoi_dung)
            putString("email", user.email)
            putString("so_dien_thoai", user.so_dien_thoai)
            putString("vai_tro", user.vai_tro)
            putString("ho_ten", user.ho_ten)
            putString("anh_dai_dien", user.anh_dai_dien)
            putBoolean("hoat_dong", user.hoat_dong == 1)
            apply()
        }
    }

    fun getUser(): User? {
        val id = dataStore.getInt("ma_nguoi_dung", -1)
        if (id == -1) return null

        return User(
            ma_nguoi_dung = id,
            email = dataStore.getString("email", null),
            so_dien_thoai = dataStore.getString("so_dien_thoai", null),
            vai_tro = dataStore.getString("vai_tro", "") ?: "",
            ho_ten = dataStore.getString("ho_ten", null),
            anh_dai_dien = dataStore.getString("anh_dai_dien", null),
            hoat_dong = if (dataStore.getBoolean("hoat_dong", true)) 1 else 0
        )
    }

    fun clear() {
        dataStore.edit().clear().apply()
    }
}

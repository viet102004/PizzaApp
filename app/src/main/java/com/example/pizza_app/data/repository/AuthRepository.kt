// AuthRepository.kt
package com.example.pizza_app.repository

import com.example.pizza_app.data.model.ApiError
import com.example.pizza_app.data.source.remote.ApiService
import com.google.gson.Gson

class AuthRepository(
    private val apiService: ApiService
) {
    suspend fun forgotPassword(email: String): Result<String> {
        return try {
            val response = apiService.forgotPassword(email)

            if (response.isSuccessful) {
                val body = response.body()
                Result.success(body?.message ?: "Thành công")
            } else {
                // Parse error message from response
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    val gson = Gson()
                    val apiError = gson.fromJson(errorBody, ApiError::class.java)
                    apiError.detail
                } catch (e: Exception) {
                    "Có lỗi xảy ra, vui lòng thử lại"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.example.pizza_app.data.source.remote

import com.example.pizza_app.data.model.Banner
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.model.Product
import retrofit2.http.GET

interface ApiService {
    @GET("getSanPhamHienThi")
    suspend fun getSanPham(): List<Product>

    @GET("danh-muc")
    suspend fun getDanhMuc(): List<Category>

    @GET("banner-hoat-dong")
    suspend fun getActiveBanners(): List<Banner>


}

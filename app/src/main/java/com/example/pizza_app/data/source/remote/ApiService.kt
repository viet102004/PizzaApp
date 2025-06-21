package com.example.pizza_app.data.source.remote

import com.example.pizza_app.data.model.Banner
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.model.LoginResponse
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.model.ProductImage
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("getSanPhamHienThi")
    suspend fun getSanPham(): List<Product>

    @GET("danh-muc")
    suspend fun getDanhMuc(): List<Category>

    @GET("banner-hoat-dong")
    suspend fun getActiveBanners(): List<Banner>

    @GET("san-pham/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("hinh-anh-san-pham/{ma_san_pham}")
    suspend fun getHinhAnhSanPham(@Path("ma_san_pham") maSanPham: Int): List<ProductImage>

    @FormUrlEncoded
    @POST("dang-nhap")
    suspend fun dangNhap(
        @Field("tai_khoan") taiKhoan: String,
        @Field("mat_khau") matKhau: String
    ): LoginResponse

}

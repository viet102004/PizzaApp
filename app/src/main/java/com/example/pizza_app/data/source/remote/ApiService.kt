package com.example.pizza_app.data.source.remote

import com.example.pizza_app.data.model.Banner
import com.example.pizza_app.data.model.BaseResponse
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.model.LoginResponse
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.model.ProductImage
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @FormUrlEncoded
    @PUT("nguoi-dung/{ma_nguoi_dung}/doi-email")
    suspend fun updateEmail(
        @Path("ma_nguoi_dung") id: Int,
        @Field("email") email: String
    ): BaseResponse

    @FormUrlEncoded
    @PUT("nguoi-dung/{ma_nguoi_dung}/doi-so-dien-thoai")
    suspend fun updatePhone(
        @Path("ma_nguoi_dung") id: Int,
        @Field("so_dien_thoai") phone: String
    ): BaseResponse

    @FormUrlEncoded
    @PUT("nguoi-dung/{ma_nguoi_dung}/doi-mat-khau")
    suspend fun updatePassword(
        @Path("ma_nguoi_dung") id: Int,
        @Field("mat_khau") password: String
    ): BaseResponse

    @Multipart
    @PUT("nguoi-dung/{ma_nguoi_dung}/doi-anh-dai-dien")
    suspend fun updateAvatar(
        @Path("ma_nguoi_dung") id: Int,
        @Part anh_dai_dien: MultipartBody.Part
    ): BaseResponse

    @FormUrlEncoded
    @PUT("nguoi-dung/{ma_nguoi_dung}/doi-ten")
    suspend fun updateName(
        @Path("ma_nguoi_dung") id: Int,
        @Field("ho_ten") hoTen: String
    ): BaseResponse




}

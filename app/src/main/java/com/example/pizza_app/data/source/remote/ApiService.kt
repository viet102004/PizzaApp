package com.example.pizza_app.data.source.remote

import com.example.pizza_app.data.model.Banner
import com.example.pizza_app.data.model.BaseResponse
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.model.LoginResponse
import com.example.pizza_app.data.model.PopularSearchesResponse
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.model.ProductImage
import com.example.pizza_app.data.model.ProductOptionsResponse
import com.example.pizza_app.data.model.SaveSearchHistoryRequest
import com.example.pizza_app.data.model.SearchFiltersResponse
import com.example.pizza_app.data.model.SearchHistoryResponse
import com.example.pizza_app.data.model.SearchRequest
import com.example.pizza_app.data.model.SearchResponse
import com.example.pizza_app.data.model.SuggestionsResponse
import com.example.pizza_app.data.model.UserResponse
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.http.Body
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

    @GET("user/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): UserResponse

    @GET("san-pham/{ma_san_pham}/tuy-chon")
    suspend fun getProductOptions(
        @Path("ma_san_pham") maSanPham: Int
    ): ProductOptionsResponse

    @FormUrlEncoded
    @POST("dang-ky")
    suspend fun dangKy(
        @Field("email") email: String,
        @Field("so_dien_thoai") soDienThoai: String?,
        @Field("mat_khau") matKhau: String,
        @Field("ho_ten") hoTen: String?
    ): UserResponse

}

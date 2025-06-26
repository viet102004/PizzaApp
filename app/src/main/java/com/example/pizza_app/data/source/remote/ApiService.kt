package com.example.pizza_app.data.source.remote

import com.example.pizza_app.data.model.AapiResponse
import com.example.pizza_app.data.model.AddToCartResponse
import retrofit2.http.Query
import com.example.pizza_app.data.model.ApiResponse
import com.example.pizza_app.data.model.Banner
import com.example.pizza_app.data.model.BaseResponse
import com.example.pizza_app.data.model.Category
import com.example.pizza_app.data.model.FavoriteResponse
import com.example.pizza_app.data.model.GioHangResponse
import com.example.pizza_app.data.model.IsFavoriteResponse
import com.example.pizza_app.data.model.LoginResponse
import com.example.pizza_app.data.model.Product
import com.example.pizza_app.data.model.ProductImage
import com.example.pizza_app.data.model.ProductListResponse
import com.example.pizza_app.data.model.ProductOptionsResponse
import com.example.pizza_app.data.model.ThemGioHangResponse
import com.example.pizza_app.data.model.ThemVaoGioHangRequest
import com.example.pizza_app.data.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Header
import retrofit2.http.Body


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
        @Path("ma_nguoi_dung") maNguoiDung: Int,
        @Field("mat_khau_cu") matKhauCu: String,
        @Field("mat_khau_moi") matKhauMoi: String
    ): ApiResponse

    @Multipart
    @PUT("nguoi-dung/{ma_nguoi_dung}/doi-anh-dai-dien")
    suspend fun updateAvatar(
        @Path("ma_nguoi_dung") maNguoiDung: Int,
        @Part anh_dai_dien: MultipartBody.Part
    ): ApiResponse

    @FormUrlEncoded
    @PUT("nguoi-dung/{ma_nguoi_dung}/doi-ten")
    suspend fun updateName(
        @Path("ma_nguoi_dung") maNguoiDung: Int,
        @Field("ho_ten") hoTen: String
    ): ApiResponse

    @GET("user/{ma_nguoi_dung}")
    suspend fun getUserById(
        @Path("ma_nguoi_dung") maNguoiDung: Int
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

    @POST("themSanPhamYeuThich")
    suspend fun addToFavorites(
        @Query("ma_nguoi_dung") maNguoiDung: Int,
        @Query("ma_san_pham") maSanPham: Int
    ): Response<ApiResponse>

    @DELETE("xoaSanPhamYeuThich")
    suspend fun removeFromFavorites(
        @Query("ma_nguoi_dung") maNguoiDung: Int,
        @Query("ma_san_pham") maSanPham: Int
    ): Response<ApiResponse>

    @GET("san-pham-yeu-thich")
    suspend fun getFavoriteProducts(
        @Query("ma_nguoi_dung") maNguoiDung: Int
    ): List<Product>

    @GET("san-pham-yeu-thich-chi-tiet")
    suspend fun getFavoriteProductsWithDetails(
        @Query("ma_nguoi_dung") maNguoiDung: Int
    ): FavoriteResponse

    @GET("kiemTraYeuThich")
    suspend fun checkIsFavorite(
        @Query("ma_nguoi_dung") maNguoiDung: Int,
        @Query("ma_san_pham") maSanPham: Int
    ): IsFavoriteResponse

    @GET("san-pham/danh-muc/{ma_danh_muc}")
    suspend fun getSanPhamTheoDanhMuc(
        @Path("ma_danh_muc") maDanhMuc: Int
    ): ProductListResponse

    @POST("themVaoGioHang")
    suspend fun themVaoGioHang(
        @Body request: ThemVaoGioHangRequest
    ): AapiResponse<ThemGioHangResponse>

    @GET("layGioHang/{ma_nguoi_dung}")
    suspend fun getGioHang(
        @Path("ma_nguoi_dung") maNguoiDung: Int
    ): GioHangResponse

    @DELETE("xoaKhoiGioHang/{ma_mat_hang_gio_hang}")
    suspend fun xoaMatHangGioHang(
        @Path("ma_mat_hang_gio_hang") maMatHangGioHang: Int
    ): ApiResponse
}
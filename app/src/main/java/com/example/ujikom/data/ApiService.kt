package com.example.ujikom.data

import com.example.ujikom.model.auth.login.LoginRequest
import com.example.ujikom.model.auth.register.RegisterRequest
import com.example.ujikom.model.pengaduan.DetailPengaduanData
import com.example.ujikom.model.pengaduan.PengaduanResponse
import com.example.ujikom.model.profile.ProfileResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Accept: application/json")
    @POST("/api/login")
    fun postLogin(@Body request: LoginRequest) : Call<JsonObject>

    @Headers("Accept: application/json")
    @POST("/api/register")
    fun postRegister(@Body request: RegisterRequest) : Call<JsonObject>

    @Headers("Accept: application/json")
    @GET("/api/masyarakat/{param}")
    fun getProfile(
        @Path("param") userId : String
    ) : Call<ProfileResponse>

    @Multipart
    @Headers("Accept: application/json")
    @POST("/api/masyarakat/pengaduan")
    fun postPengaduan(
        @Part("nik") nik: String,
        @Part("judul_laporan") judul_laporan : String,
        @Part("isi_laporan") isi_laporan : String,
        @Part file : MultipartBody.Part
    ) : Call<ResponseBody>

    @Headers("Accept: application/json")
    @GET("/api/masyarakat/{param}/pengaduan")
    fun getUserPengaduan(
        @Path("param") userId : String
    ) : Call<PengaduanResponse>

    @Headers("Accept: application/json")
    @GET("/api/masyarakat/pengaduan/{param}")
    fun getDetailPengaduan(
        @Path("param") pengaduanId : String
    ) : Call<DetailPengaduanData>

}
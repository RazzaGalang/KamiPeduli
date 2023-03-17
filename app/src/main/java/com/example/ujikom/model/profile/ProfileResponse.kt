package com.example.ujikom.model.profile


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("created_at")
    val createdAt: Any,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("nik")
    val nik: Long,
    @SerializedName("password")
    val password: String,
    @SerializedName("telp")
    val telp: String,
    @SerializedName("updated_at")
    val updatedAt: Any,
    @SerializedName("username")
    val username: String
)
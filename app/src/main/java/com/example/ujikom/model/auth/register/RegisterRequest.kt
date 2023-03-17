package com.example.ujikom.model.auth.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nik")
    val nik: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("telp")
    val telp: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirmation")
    val password_confirmation: String
)
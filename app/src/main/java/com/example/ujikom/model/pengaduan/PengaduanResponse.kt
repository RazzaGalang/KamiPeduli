package com.example.ujikom.model.pengaduan


import com.google.gson.annotations.SerializedName

data class PengaduanResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String
)
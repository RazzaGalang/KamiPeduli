package com.example.ujikom.model.pengaduan


import com.google.gson.annotations.SerializedName

data class Tanggapan(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id_pengaduan")
    val idPengaduan: Int,
    @SerializedName("id_petugas")
    val idPetugas: Int,
    @SerializedName("id_tanggapan")
    val idTanggapan: Int,
    @SerializedName("tanggapan")
    val tanggapan: String,
    @SerializedName("tgl_tanggapan")
    val tglTanggapan: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
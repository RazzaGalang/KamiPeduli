package com.example.ujikom.model.pengaduan


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("foto")
    val foto: String,
    @SerializedName("id_pengaduan")
    val idPengaduan: Int,
    @SerializedName("isi_laporan")
    val isiLaporan: String,
    @SerializedName("judul_laporan")
    val judulLaporan: String,
    @SerializedName("nik")
    val nik: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tanggapan")
    val tanggapan: Tanggapan,
    @SerializedName("tgl_pengaduan")
    val tglPengaduan: String,
    @SerializedName("updated_at")
    val updatedAt: Any
)
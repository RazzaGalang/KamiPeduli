package com.example.ujikom.model.pengaduan

import com.google.gson.annotations.SerializedName

data class PengaduanRequest(
    @SerializedName("judul_laporan")
    var judul_laporan : String,
    @SerializedName("isi_laporan")
    var isi_laporan : String
)
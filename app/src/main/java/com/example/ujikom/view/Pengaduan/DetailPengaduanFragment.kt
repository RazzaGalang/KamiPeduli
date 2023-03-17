package com.example.ujikom.view.Pengaduan

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.ujikom.data.RetrofitBuilder
import com.example.ujikom.databinding.FragmentDetailPengaduanBinding
import com.example.ujikom.model.pengaduan.DetailPengaduanData
import com.example.ujikom.model.pengaduan.PengaduanResponse
import com.example.ujikom.view.Main.MainActivity
import com.example.ujikom.view.popup.ConditionConnectionErrorDetailFragment
import com.example.ujikom.view.popup.ConditionConnectionErrorFragment
import com.example.ujikom.view.popup.ConditionLoadingFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPengaduanFragment : Fragment() {

    private var _binding: FragmentDetailPengaduanBinding? = null
    private val binding get() = _binding!!

    private lateinit var valuesPengaduanID : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPengaduanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        valuesPengaduanID = arguments?.getString("ID_PENGADUAN").toString()

        setupOutput()
    }

    private fun setupOutput(){
        val loadingDialog = ConditionLoadingFragment()
        loadingDialog.show(parentFragmentManager, "Loading")

        val output = RetrofitBuilder.getRetrofit().getDetailPengaduan(valuesPengaduanID)
        output.enqueue(object : Callback<DetailPengaduanData>{
            override fun onResponse(
                call: Call<DetailPengaduanData>,
                response: Response<DetailPengaduanData>
            ) {
                loadingDialog.dismiss()

                binding.apply {
                    var statusLaporan = response.body()?.status
                    when (statusLaporan) {
                        "0" -> { statusLaporan = "Belum Terverifikasi" }
                    }

                    var tanggapanLaporan : String
                    if (response.body()?.tanggapan.toString() != "null"){
                        tanggapanLaporan = response.body()?.tanggapan?.tanggapan.toString()
                        when (tanggapanLaporan) {
                            "null" -> { tanggapanLaporan = "Belum Ditanggapi"}
                        }
                    } else {
                        tanggapanLaporan = "Belum Ditanggapi"
                    }

                    val gambarLaporan = response.body()?.foto.toString()

                    outputKeteranganID.text = response.body()?.idPengaduan.toString()
                    outputKeteranganTanggal.text = response.body()?.tglPengaduan
                    outputKeteranganStatus.text = statusLaporan
                    outputJudulPengaduan.text = response.body()?.judulLaporan
                    outputIsiPengaduan.text = response.body()?.isiLaporan
                    outputTanggapan.text = tanggapanLaporan

                    Glide.with(outputGambarPengaduan)
                        .load(gambarLaporan)
                        .into(outputGambarPengaduan)
                }
            }

            override fun onFailure(call: Call<DetailPengaduanData>, t: Throwable) {
                loadingDialog.dismiss()
                ConditionConnectionErrorDetailFragment().show(parentFragmentManager, "Connection Error")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
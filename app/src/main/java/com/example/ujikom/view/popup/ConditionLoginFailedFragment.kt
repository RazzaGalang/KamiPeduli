package com.example.ujikom.view.popup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.ujikom.R
import com.example.ujikom.databinding.FragmentConditionOneActionBinding

class ConditionLoginFailedFragment : DialogFragment() {

    private var _binding: FragmentConditionOneActionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConditionOneActionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCancelable(false)

        setOutput()
        setNavigation()
    }

    private fun setOutput(){
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.conditionImage.setImageResource(R.drawable.img_condition_data_not_found)
        binding.conditionTitle.text = "Gagal Masuk!"
        binding.conditionContent.text = "Periksa Nama Pengguna dan Kata Sandi"
        binding.conditionButton.text = "Kembali"
    }

    private fun setNavigation(){
        binding.conditionButton.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
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
import com.example.ujikom.databinding.FragmentConditionTwoActionBinding

class ConditionOnBackPressedFragment : DialogFragment() {

    private var _binding: FragmentConditionTwoActionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConditionTwoActionBinding.inflate(inflater, container, false)
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

        binding.conditionImage.setImageResource(R.drawable.img_condition_notification)
        binding.conditionTitle.text = "Keluar dari Aplikasi"
        binding.conditionContent.text = "Apakah Anda Yakin?"
        binding.conditionButtonPositif.text = "Keluar"
        binding.conditionButtonNegatif.text = "Kembali"
    }

    private fun setNavigation(){
        setButtonPositif()
        setButtonNegatif()
    }

    private fun setButtonPositif(){
        binding.conditionButtonPositif.setOnClickListener {
            requireActivity().finishAffinity()
        }
    }

    private fun setButtonNegatif(){
        binding.conditionButtonNegatif.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
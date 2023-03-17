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
import com.example.ujikom.view.Auth.LoginFragment
import com.example.ujikom.view.Auth.RegisterFragment

class ConditionRegisterSuccessFragment : DialogFragment() {

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

        binding.conditionImage.setImageResource(R.drawable.img_condition_succes)
        binding.conditionTitle.text = "Daftar Berhasil!"
        binding.conditionContent.text = "Anda akan diarahkan ke Halaman Masuk"
        binding.conditionButton.text = "Okay"
    }


    private fun setNavigation(){
        binding.conditionButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.authFrameLayout, LoginFragment())?.commit()
            dialog!!.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
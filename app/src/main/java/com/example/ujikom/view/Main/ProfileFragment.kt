package com.example.ujikom.view.Main

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ujikom.data.RetrofitBuilder
import com.example.ujikom.databinding.FragmentProfileBinding
import com.example.ujikom.model.profile.ProfileResponse
import com.example.ujikom.view.popup.ConditionConnectionErrorFragment
import com.example.ujikom.view.popup.ConditionLoadingFragment
import com.example.ujikom.view.popup.ConditionLogoutFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var values : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        values = arguments?.getString("AUTH_ID").toString()

        Log.e(ContentValues.TAG, "onViewCreated: $values",)

        setupOutput()
        setupLogout()
    }

    private fun setupOutput (){
        val loadingDialog = ConditionLoadingFragment()
        loadingDialog.show(parentFragmentManager, "Loading")

        val outputProfile: Call<ProfileResponse> = RetrofitBuilder.getRetrofit().getProfile(values)
        outputProfile.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                binding.apply {
                    textNIK.text = response.body()?.nik.toString()
                    textNama.text = response.body()?.nama.toString()
                    textUsername.text = response.body()?.username.toString()
                    textTelp.text = response.body()?.telp.toString()
                }

                loadingDialog.dismiss()
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                loadingDialog.dismiss()
                ConditionConnectionErrorFragment().show(parentFragmentManager, "Connection Error")
                refresh()
            }

        })
    }

    private fun refresh(){
        setupOutput()
    }

    private fun setupLogout(){
        binding.buttonLogout.setOnClickListener {
            ConditionLogoutFragment().show(parentFragmentManager, "Logut")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
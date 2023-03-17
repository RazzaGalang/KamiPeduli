package com.example.ujikom.view.Auth

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ujikom.R
import com.example.ujikom.data.RetrofitBuilder
import com.example.ujikom.databinding.FragmentLoginBinding
import com.example.ujikom.model.auth.login.LoginRequest
import com.example.ujikom.view.Main.MainActivity
import com.example.ujikom.view.popup.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextWatcher()
        submitData()
        navigateRegister()
    }

    private fun initTextWatcher() {
        textWatcherUsername()
        textWatcherPassword()
    }

    private fun submitData() {
        binding.buttonLogin.setOnClickListener {
            if (!checkingValidate()) {
                validateForm()
            } else {
                setupLogin()
            }
        }
    }

    private fun setupLogin(){
        val loadingDialog = ConditionLoadingFragment()
        loadingDialog.show(parentFragmentManager, "Loading")

        val login: Call<JsonObject> = RetrofitBuilder.getRetrofit().postLogin(
            LoginRequest(
                binding.inputUsername.text.toString(),
                binding.inputPassword.text.toString()
            )
        )
        login.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loadingDialog.dismiss()
                if (response.isSuccessful){
                    val AUTH = Gson().toJson(response.body()?.get("nik"))

                    val bundle = Bundle()
                    bundle.putString("AUTH_ID", AUTH)

                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    requireActivity().finish()
                } else if (response.code().toString() == "422"){
                    ConditionLoginFailedFragment().show(parentFragmentManager, "Login Failed")
                } else {
                    ConditionServerErrorFragment().show(parentFragmentManager, "Server Error")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loadingDialog.dismiss()
                ConditionConnectionErrorFragment().show(parentFragmentManager, "Connection Error")
            }
        })
    }

    private fun textWatcherUsername() {
        binding.inputUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateUsername()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun textWatcherPassword() {
        binding.inputPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validatePassword()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun checkingValidate(): Boolean {
        return !(!validateUsername() || !validatePassword())
    }

    private fun validateForm() {
        validateUsername()
        validatePassword()
    }

    private fun validateUsername(): Boolean {
        return if (binding.inputUsername.length() == 0) {
            binding.layoutUsername.error = "Nama Pengguna Tidak Boleh Kosong"
            false
        } else {
            clearUsername()
            true
        }
    }

    private fun validatePassword(): Boolean {
        return if (binding.inputPassword.length() == 0) {
            binding.layoutPassword.error = "Kata Sandi Tidak Boleh Kosong"
            false
        } else {
            clearPassword()
            true
        }
    }


    private fun clearUsername() {
        binding.layoutUsername.isErrorEnabled = false
    }

    private fun clearPassword() {
        binding.layoutPassword.isErrorEnabled = false
    }

    override fun onResume() {
        super.onResume()
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                ConditionOnBackPressedFragment().show(parentFragmentManager, "On Back Pressed")
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun navigateRegister() {
        val spannable = SpannableStringBuilder(binding.navigateToRegister.text.toString())
        val blueColor = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                parentFragmentManager.beginTransaction().replace(R.id.authFrameLayout, RegisterFragment()).commit()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spannable.setSpan(blueColor, 18, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(clickableSpan, 18, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.navigateToRegister.text = spannable
        binding.navigateToRegister.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
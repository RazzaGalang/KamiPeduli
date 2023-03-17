package com.example.ujikom.view.Auth

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.ujikom.R
import com.example.ujikom.data.RetrofitBuilder
import com.example.ujikom.databinding.FragmentRegisterBinding
import com.example.ujikom.model.auth.register.RegisterRequest
import com.example.ujikom.view.popup.*
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextWatcher()
        submitData()
        navigateLogin()
    }

    private fun initTextWatcher() {
        textWatcherNIK()
        textWatcherFullname()
        textWatcherNomorTelp()
        textWatcherUsername()
        textWatcherPassword()
        textWatcherConfirmPassword()
    }

    private fun submitData() {
        binding.buttonRegister.setOnClickListener {
            if (!checkingValidate()) {
                validateForm()
            } else {
                setupRegister()
            }
        }
    }

    private fun setupRegister() {
        val loadingDialog = ConditionLoadingFragment()
        loadingDialog.show(parentFragmentManager, "Loading")

        val register = RetrofitBuilder.getRetrofit().postRegister(
            RegisterRequest(
                binding.inputNIK.text.toString() ,
                binding.inputFullname.text.toString(),
                binding.inputNomorTelp.text.toString(),
                binding.inputUsername.text.toString(),
                binding.inputPassword.text.toString(),
                binding.inputConfirmPassword.text.toString()
            )
        )
        register.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loadingDialog.dismiss()

                if (response.isSuccessful){
                    ConditionRegisterSuccessFragment().show(parentFragmentManager, "Register Success")
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        if (errorBody.contains("The username has already been taken")){
                            ConditionRegisterSameUsernameFragment().show(parentFragmentManager, "Username Have Been Taken")
                            binding.layoutUsername.error = "Nama Pengguna Telah Digunakan"
                        } else if (errorBody.contains("The nik has already been taken")){
                            ConditionRegisterSameIdFragment().show(parentFragmentManager, "NIK Have Been Taken")
                            binding.layoutNIK.error = "Nomor Induk Kependudukan Telah Digunakan"
                        } else {
                            ConditionServerErrorFragment().show(parentFragmentManager, "Server Error")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loadingDialog.dismiss()
                ConditionConnectionErrorFragment().show(parentFragmentManager, "Connection Error")
            }

        })
    }

    private fun textWatcherNIK() {
        binding.inputNIK.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateNIK()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun textWatcherFullname() {
        binding.inputFullname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateFullname()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun textWatcherNomorTelp() {
        binding.inputNomorTelp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateNomorTelp()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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

    private fun textWatcherConfirmPassword() {
        binding.inputConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateConfirmPassword()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun checkingValidate(): Boolean {
        return !(!validateNIK() || !validateFullname() || !validateNomorTelp() || !validateUsername() || !validatePassword() || !validateConfirmPassword())
    }

    private fun validateForm() {
        validateNIK()
        validateFullname()
        validateNomorTelp()
        validateUsername()
        validatePassword()
        validateConfirmPassword()
    }

    private fun validateNIK(): Boolean {
        return if (binding.inputNIK.length() == 0) {
            binding.layoutNIK.error = "NIK Tidak Boleh Kosong"
            false
        } else if (binding.inputNIK.length() < 16) {
            binding.layoutNIK.error = "NIK Harus 16 Karakter"
            false
        } else {
            clearNIK()
            true
        }
    }

    private fun validateFullname(): Boolean {
        return if (binding.inputFullname.length() == 0) {
            binding.layoutFullname.error = "Nama Lengkap Tidak Boleh Kosong"
            false
        } else if (binding.inputFullname.length() < 3) {
            binding.layoutFullname.error = "Nama Lengkap Minimal 3 Karakter"
            false
        } else if (!(binding.inputFullname.text.toString().matches("[A-Za-z ']+".toRegex()))){
            binding.layoutFullname.error = "Tidak Boleh Mengandung Unsur Angka dan Simbol"
            false
        } else {
            clearFullname()
            true
        }
    }

    private fun validateNomorTelp(): Boolean {
        return if (binding.inputNomorTelp.length() == 0) {
            binding.layoutNomorTelp.error = "Nomor Telepon Tidak Boleh Kosong"
            false
        } else if (binding.inputNomorTelp.length() < 10) {
            binding.layoutNomorTelp.error = "Nomor Telepon Minimal 10 Karakter"
            false
        } else {
            clearNomorTelp()
            true
        }
    }

    private fun validateUsername(): Boolean {
        return if (binding.inputUsername.length() == 0) {
            binding.layoutUsername.error = "Nama Pengguna Tidak Boleh Kosong"
            false
        } else if (binding.inputUsername.length() < 5) {
            binding.layoutUsername.error = "Nama Pengguna Minimal 5 Karakter"
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
        } else if (binding.inputPassword.length() < 8) {
            binding.layoutPassword.error = "Kata Sandi Minimal 8 Karakter"
            binding.layoutConfirmPassword.error = "Konfirmasi Kata Sandi Tidak Sesuai"
            false
        } else if (binding.inputPassword.text.toString() != binding.inputConfirmPassword.text.toString()) {
            binding.layoutConfirmPassword.error = "Konfirmasi Kata Sandi Tidak Sesuai"
            clearPassword()
            false
        } else if (binding.inputPassword.text.toString() == binding.inputConfirmPassword.text.toString()) {
            clearPassword()
            clearConfirmPassword()
            true
        } else {
            false
        }
    }

    private fun validateConfirmPassword(): Boolean {
        return if (binding.inputConfirmPassword.length() == 0) {
            binding.layoutConfirmPassword.error = "Konfirmasi Kata Sandi Tidak Boleh Kosong"
            false
        } else if (binding.inputPassword.text.toString() != binding.inputConfirmPassword.text.toString()) {
            binding.layoutConfirmPassword.error = "Konfirmasi Kata Sandi Tidak Sesuai"
            false
        } else if (binding.inputPassword.text.toString() == binding.inputConfirmPassword.text.toString()) {
            clearPassword()
            clearConfirmPassword()
            true
        } else {
            false
        }
    }

    private fun clearNIK() {
        binding.layoutNIK.isErrorEnabled = false
    }

    private fun clearFullname() {
        binding.layoutFullname.isErrorEnabled = false
    }

    private fun clearNomorTelp() {
        binding.layoutNomorTelp.isErrorEnabled = false
    }

    private fun clearUsername() {
        binding.layoutUsername.isErrorEnabled = false
    }

    private fun clearPassword() {
        binding.layoutPassword.isErrorEnabled = false
    }

    private fun clearConfirmPassword() {
        binding.layoutConfirmPassword.isErrorEnabled = false
    }

    override fun onResume() {
        super.onResume()
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                parentFragmentManager.beginTransaction().replace(R.id.authFrameLayout, LoginFragment()).commit()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun navigateLogin() {
        val spannable = SpannableStringBuilder(binding.navigateToLogin.text.toString())
        val blueColor = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                parentFragmentManager.beginTransaction().replace(R.id.authFrameLayout, LoginFragment()).commit()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spannable.setSpan(blueColor, 18, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(clickableSpan, 18, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.navigateToLogin.text = spannable
        binding.navigateToLogin.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
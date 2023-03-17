package com.example.ujikom.view.Main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ujikom.R
import com.example.ujikom.data.RetrofitBuilder
import com.example.ujikom.databinding.FragmentPengaduanBinding
import com.example.ujikom.view.popup.ConditionConnectionErrorFragment
import com.example.ujikom.view.popup.ConditionLoadingFragment
import com.example.ujikom.view.popup.ConditionPengaduanSuccessFragment
import com.example.ujikom.view.popup.ConditionServerErrorFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import kotlin.math.log

class PengaduanFragment : Fragment() {

    private val REQUEST_CODE_PERMISSIONS = 101
    private val REQUEST_CODE_SELECT_IMAGE = 102


    private var _binding: FragmentPengaduanBinding? = null
    private val binding get() = _binding!!

    private lateinit var values: String
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengaduanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        values = arguments?.getString("AUTH_ID").toString()

        initInputImage()
        initTextWatcher()
        submitData()
    }

    private fun initTextWatcher() {
        textWatcherJudul()
        textWatcherIsiPengaduan()
    }

    private fun submitData() {
        binding.submitPengaduan.setOnClickListener {
            if (!checkingValidate()) {
                validateForm()
            } else {
                setupPostData()
            }
        }
    }

    private fun setupPostData() {
        val loadingDialog = ConditionLoadingFragment()
        loadingDialog.show(parentFragmentManager, "Loading")

        val file = File(selectedImageUri?.let { getPathFromMediaStore(requireContext(), it) }.toString())
        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        val part = MultipartBody.Part.createFormData("foto", file.name, requestFile)

        val postData: Call<ResponseBody> = RetrofitBuilder.getRetrofit().postPengaduan(
            values,
            binding.inputJudul.text.toString(),
            binding.inputIsiLaporan.text.toString(),
            part
        )

        Log.e(TAG, "setupPostData: $postData" )

        postData.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                loadingDialog.dismiss()

                if (response.isSuccessful) {
                    refreshForm()
                    ConditionPengaduanSuccessFragment().show(parentFragmentManager, "Pengaduan Success")
                } else {
                    ConditionServerErrorFragment().show(parentFragmentManager, "Server Error")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loadingDialog.dismiss()
                ConditionConnectionErrorFragment().show(parentFragmentManager, "Connection Error")
            }
        })
    }

    private fun refreshForm(){
        selectedImageUri = null
        binding.inputGambar.setImageURI(selectedImageUri)
        binding.inputGambar.setImageResource(R.drawable.img_logo_banner_blue_padding)
        binding.inputJudul.text = null
        binding.inputIsiLaporan.text = null
    }

    private fun initInputImage() {
        binding.inputGambar.setOnClickListener {
            selectImageFromGallery(REQUEST_CODE_SELECT_IMAGE)
        }
    }

    private fun textWatcherJudul() {
        binding.inputJudul.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateJudul()
            }
        })
    }

    private fun textWatcherIsiPengaduan() {
        binding.inputIsiLaporan.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateIsi()
            }
        })
    }

    private fun checkingValidate(): Boolean {
        return !(!validateJudul() || !validateIsi() || !validateGambar())
    }

    private fun validateForm() {
        validateGambar()
        validateJudul()
        validateIsi()
    }

    @SuppressLint("ResourceAsColor")
    private fun validateGambar(): Boolean {
        return if (selectedImageUri == null){
            binding.warningNullGambar.text = "Harus menyertakan Gambar"
            binding.inputGambar.strokeColor = ColorStateList.valueOf(Color.RED)
            false
        } else {
            val checkSize = isImageFileSize(requireContext(), selectedImageUri!!)
            return if (!checkSize){
                binding.warningNullGambar.text = "Size Gambar harus dibawah 2MB"
                binding.inputGambar.strokeColor = ColorStateList.valueOf(Color.RED)
                false
            } else {
                val checkType = isImageFileType(selectedImageUri!!)
                if (!checkType){
                    binding.warningNullGambar.text = "Tipe Gambar harus JPG/PNG"
                    binding.inputGambar.strokeColor = ColorStateList.valueOf(Color.RED)
                    false
                } else {
                    binding.warningNullGambar.text = ""
                    binding.inputGambar.strokeColor = ColorStateList.valueOf(Color.parseColor("#3473C8"))
                    true
                }
            }
        }
    }

    private fun validateJudul(): Boolean {
        return if (binding.inputJudul.length() < 20) {
            binding.layoutJudul.error = "Judul Laporan Minimal 20 Karakter"
            false
        } else {
            clearJudul()
            true
        }
    }

    private fun validateIsi(): Boolean {
        return if (binding.inputIsiLaporan.length() < 100) {
            binding.layoutIsi.error = "Isi Laporan Minimal 100 Karakter"
            false
        } else {
            clearIsi()
            true
        }
    }

    private fun clearJudul() {
        binding.layoutJudul.isErrorEnabled = false
    }

    private fun clearIsi() {
        binding.layoutIsi.isErrorEnabled = false
    }

    private fun selectImageFromGallery(code: Int) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, code)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data!!
            if (selectedImageUri != null) {
                binding.inputGambar.setImageURI(selectedImageUri)
                validateGambar()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
            }
        }
    }

    private fun getPathFromMediaStore(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return path
    }

    private fun getImageFileSize(contentUri: Uri, context: Context): Long {
        var inputStream: InputStream? = null
        var size: Long = 0
        try {
            inputStream = context.contentResolver.openInputStream(contentUri)
            if (inputStream != null) {
                size = inputStream.available().toLong()
            }
        } catch (e: IOException) {
            // handle exception
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                // handle exception
            }
        }
        return size
    }

    private fun isImageFileSize(context: Context, contentUri: Uri): Boolean {
        val fileSize = getImageFileSize(contentUri, context)
        return fileSize < 2000000L
    }

    private fun isImageFileType(uri: Uri): Boolean {
        val contentResolver = context?.contentResolver
        val type = contentResolver?.getType(uri)
        return type == "image/jpeg" || type == "image/png"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
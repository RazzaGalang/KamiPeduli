package com.example.ujikom.view.Main

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.ujikom.view.adapter.PengaduanAdapter
import com.example.ujikom.data.RetrofitBuilder
import com.example.ujikom.databinding.FragmentHomeBinding
import com.example.ujikom.model.pengaduan.Data
import com.example.ujikom.model.pengaduan.PengaduanResponse
import com.example.ujikom.view.Pengaduan.PengaduanActivity
import com.example.ujikom.view.popup.ConditionConnectionErrorFragment
import com.example.ujikom.view.popup.ConditionLoadingFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), PengaduanAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var values : String
    private val adapter : PengaduanAdapter = PengaduanAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        values = arguments?.getString("AUTH_ID").toString()

        setupAdapter()
        setupRecyclerView()
        refreshData()
    }

    private fun setupAdapter() {
        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(item: Data) {
        val intent = Intent(this.context, PengaduanActivity::class.java)
        intent.putExtra("ID_PENGADUAN", item.idPengaduan.toString())
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        val loadingDialog = ConditionLoadingFragment()
        loadingDialog.show(parentFragmentManager, "Loading")

        val emptyState = binding.emptyStateGroup

        val getUserPengaduan = RetrofitBuilder.getRetrofit().getUserPengaduan(values)
        getUserPengaduan.enqueue(object : Callback<PengaduanResponse>{
            override fun onResponse(
                call: Call<PengaduanResponse>,
                response: Response<PengaduanResponse>
            ) {
                loadingDialog.dismiss()

                adapter.items = response.body()?.data!!
                if (adapter.items.toString() == "[]"){
                    emptyState.isVisible = true
                }
            }

            override fun onFailure(call: Call<PengaduanResponse>, t: Throwable) {
                loadingDialog.dismiss()
                ConditionConnectionErrorFragment().show(parentFragmentManager, "Connection Error")
            }

        })
    }

    private fun refreshData(){
        binding.swipeRefresh.setOnRefreshListener {
            setupRecyclerView()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
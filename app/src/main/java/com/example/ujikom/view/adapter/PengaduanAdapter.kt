package com.example.ujikom.view.adapter

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ujikom.R
import com.example.ujikom.databinding.ItemRecyclerViewBinding
import com.example.ujikom.model.pengaduan.Data
import com.example.ujikom.view.Main.MainActivity
import com.google.android.material.card.MaterialCardView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PengaduanAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<PengaduanAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Data)
    }

    inner class ViewHolder(private val binding: ItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat", "SetTextI18n", "ResourceAsColor")
        fun setData(item: Data) {
            binding.recyclerCardView.setOnClickListener { listener.onItemClick(item) }

            binding.apply {
                var validateStatus = item.status
                if (validateStatus == "0") {
                    validateStatus = "Belum Terverifikasi"
                    Log.e(TAG, "setData: Belum Terverifikasi")
                } else if (validateStatus == "Selesai") {
                    itemStatusCardView.strokeColor = Color.parseColor("#28A745")
                    itemStatusText.setTextColor(Color.parseColor("#28A745"))
                    validateStatus = "Selesai"
                } else if (validateStatus == "Proses"){
                    itemStatusCardView.strokeColor = Color.parseColor("#3473C8")
                    itemStatusText.setTextColor(Color.parseColor("#3473C8"))
                    validateStatus = "Dalam Proses"
                }


                itemJudul.text = item.judulLaporan
                itemStatusText.text = validateStatus
                Glide.with(itemPicture)
                    .load(item.foto)
                    .into(itemPicture)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(
            oldItem: Data,
            newItem: Data
        ) = oldItem.idPengaduan == newItem.idPengaduan

        override fun areContentsTheSame(
            oldItem: Data,
            newItem: Data
        ) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var items : List<Data>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
        holder.setIsRecyclable(true)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )
}


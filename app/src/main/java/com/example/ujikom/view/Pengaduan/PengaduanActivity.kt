package com.example.ujikom.view.Pengaduan

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.ujikom.R
import com.example.ujikom.view.Main.HomeFragment
import com.example.ujikom.view.Main.PengaduanFragment
import com.example.ujikom.view.Main.ProfileFragment

class PengaduanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaduan)

        val value = intent.getStringExtra("ID_PENGADUAN")

        val detailPengaduanFragment = DetailPengaduanFragment()
        val args = Bundle()
        args.putString("ID_PENGADUAN", value)
        detailPengaduanFragment.arguments = args

        loadFragment(detailPengaduanFragment)
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.pengaduanFrameLayout,fragment)
        transaction.commit()
    }
}
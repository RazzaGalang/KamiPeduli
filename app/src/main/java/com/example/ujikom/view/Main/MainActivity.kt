package com.example.ujikom.view.Main

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ujikom.R
import com.example.ujikom.view.popup.ConditionOnBackPressedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        val value = bundle?.getString("AUTH_ID")

        val firstFragment = HomeFragment()
        val secondFragment = PengaduanFragment()
        val thirdFragment = ProfileFragment()

        val args = Bundle()
        args.putString("AUTH_ID", value)
        firstFragment.arguments = args
        secondFragment.arguments = args
        thirdFragment.arguments = args

        loadFragment(firstFragment)

        val bottomNav = findViewById<BottomNavigationView>(R.id.mainActivityBottomNavigation)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuBottomNavigationHome -> {
                    loadFragment(firstFragment)
                    true
                }
                R.id.menuBottomNavigationEvent -> {
                    loadFragment(secondFragment)
                    true
                }
                R.id.menuBottomNavigationProfile -> {
                    loadFragment(thirdFragment)
                    true
                }
                else -> false
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainActivityFrameLayout,fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        ConditionOnBackPressedFragment().show(supportFragmentManager, "On Back Pressed")
    }
}
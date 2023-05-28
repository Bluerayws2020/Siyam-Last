package com.example.siyam

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import com.example.siyam.databinding.ActivitySplashBinding
import com.example.siyam.helpers.HelperUtils
import java.util.*

class SplashActivity  : AppCompatActivity()  {

    private lateinit var binding: ActivitySplashBinding
    private var navController: NavController? = null

//    private val callback = object :OnBackPressedCallback(false){
//        override fun handleOnBackPressed() {
//            println("HELLO")
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.hide()

        setDefaultLanguage(this,"enx")
        Handler(Looper.getMainLooper()).postDelayed({
            if (HelperUtils.getUID(this) != "0" || !HelperUtils.getUID(this).isNullOrEmpty()) {
                openHome()
            }
            else
                openLogin()
        }, 2000)
//        openLogin()




    }

    private fun openHome() {
        val intentHome = Intent(this, HomeActivity::class.java)
        startActivity(intentHome)
        finish()
    }


    private fun openLogin() {
        val intentHome = Intent(this, MainActivity::class.java)
        startActivity(intentHome)
        finish()
    }
    fun setDefaultLanguage(context: Context, lang: String?) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }



}
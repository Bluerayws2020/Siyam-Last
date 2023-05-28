package com.example.siyam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.siyam.databinding.ActivityHomeBinding
import com.example.siyam.databinding.ActivityMainBinding
import com.example.siyam.helpers.ProgressDialogFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        binding.guestContinueBtn.setOnClickListener {
//            showProgress()

            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
//            hideProgress()

        }

        binding.signInButton.setOnClickListener {
//            showProgress()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//            hideProgress()
        }

    }
    private fun showProgress() {
        ProgressDialogFragment().show(supportFragmentManager, "progress_dialog")
    }

    private fun hideProgress() {
        supportFragmentManager.findFragmentByTag("progress_dialog")?.let {
            if (it.isAdded)
                (it as ProgressDialogFragment).dismiss()
        }
    }
}
package com.example.siyam


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.siyam.activities.AboutSiyamGroupFragment
import com.example.siyam.activities.ContactUsFragment
import com.example.siyam.activities.ProductsActivity
import com.example.siyam.databinding.ActivityHomeBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.ProgressDialogFragment
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)



        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navMenu, navController)

        binding.navMenu.setNavigationItemSelectedListener(this)




}

    fun openDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END))
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        else
            binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {

            R.id.home -> {
                startActivity(Intent(this@HomeActivity, HomeActivity::class.java))
                return true
            }
            R.id.ourProducts -> {

                startActivity(Intent(this@HomeActivity, ProductsActivity::class.java))

                return true
            }
            R.id.aboutSiyam -> {

                startActivity(Intent(this@HomeActivity, AboutSiyamGroupFragment::class.java))

                return true
            }
            R.id.contactUs -> {

                startActivity(Intent(this@HomeActivity, ContactUsFragment::class.java))


                return true
            }
            R.id.logout -> {
                val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
                sharedPreferences.edit().apply {
                    putString("uid", "0")

                }.apply()
                startActivity(Intent(this@HomeActivity, HomeActivity::class.java))


                return true
            }
            else -> {
                return false
            }
        }
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}
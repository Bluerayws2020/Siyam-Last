package com.example.siyam


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
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


companion object
{
    var index = 0
    val optionsList = listOf("HOME","Product","ABOUT SIYAM GROUP","CONTACT US","LOGOUT")
}

    @SuppressLint("MissingInflatedId")
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
                item.actionView.setBackgroundColor(ContextCompat.getColor(this,R.color.purple_200))
                selectItem(0,"HOME")
                return true
            }
            R.id.ourProducts -> {

                startActivity(Intent(this@HomeActivity, ProductsActivity::class.java))

                return true
            }
            R.id.aboutSiyam -> {
                selectItem(1,"ABOUT SIYAM GROUP")
                startActivity(Intent(this@HomeActivity, AboutSiyamGroupFragment::class.java))

                return true
            }
            R.id.contactUs -> {

                selectItem(2,"CONTACT US")
                startActivity(Intent(this@HomeActivity, ContactUsFragment::class.java))

                return true
            }
            R.id.logout -> {
                selectItem(3,"LOGOUT")
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
    fun selectItem(i:Int,text : String){
        val menu = binding.navMenu.menu
        val menuItem = menu.getItem(i+1)
        val customView = layoutInflater.inflate(R.layout.custom_nav_item,binding.navMenu, false)
        customView.setBackgroundColor(ContextCompat.getColor(this,R.color.purple_200))
        val menuTitle = customView.findViewById<TextView>(R.id.text)
        val backgroundView = customView.findViewById<LinearLayout>(R.id.backView)
        menuTitle.text = text
        menuTitle.setTextColor(Color.WHITE)
        menuItem.actionView = customView

        val menuItem1 = menu.getItem(index)
        val customView1 =
            layoutInflater.inflate(R.layout.custom_nav_item, binding.navMenu, false)
        customView1.setBackgroundColor(ContextCompat.getColor(this, R.color.nav_gray))
        // val menuIcon = customView.findViewById<ImageView>(R.id.menu_icon)
        val menuTitle1 = customView1.findViewById<TextView>(R.id.text)
        val backgroundView1 = customView1.findViewById<LinearLayout>(R.id.backView)
        //menuIcon.setImageResource(menuItem.icon)
        menuTitle1.text ="Home"
        menuItem1.title = null
        menuItem1.setActionView(customView1)

        }
    fun resetNavDrawer(){
        val menu = binding.navMenu.menu
        for (i in 0 until menu.size()) {
            if (i != index) {
                val menuItem = menu.getItem(i)
                val customView =
                    layoutInflater.inflate(R.layout.custom_nav_item, binding.navMenu, false)
                customView.setBackgroundColor(ContextCompat.getColor(this, R.color.nav_gray))
                // val menuIcon = customView.findViewById<ImageView>(R.id.menu_icon)
                val menuTitle = customView.findViewById<TextView>(R.id.text)
                val backgroundView = customView.findViewById<LinearLayout>(R.id.backView)
                //menuIcon.setImageResource(menuItem.icon)
                menuTitle.text = optionsList[i]
                menuItem.title = null
                menuItem.setActionView(customView)
            }else{
                val menuItem = menu.getItem(i)
                val customView =
                    layoutInflater.inflate(R.layout.custom_nav_item, binding.navMenu, false)
                customView.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
                // val menuIcon = customView.findViewById<ImageView>(R.id.menu_icon)
                val menuTitle = customView.findViewById<TextView>(R.id.text)
                val backgroundView = customView.findViewById<LinearLayout>(R.id.backView)
                //menuIcon.setImageResource(menuItem.icon)
                menuTitle.text = optionsList[i]
                menuTitle.setTextColor(Color.WHITE)
                menuItem.title = null
                menuItem.setActionView(customView)
            }
        }
    }

    override fun onResume() {
        resetNavDrawer()
        super.onResume()
    }
     fun showProgress() {
        ProgressDialogFragment().show(supportFragmentManager, "progress_dialog")
    }
     fun hideProgress() {
        supportFragmentManager.findFragmentByTag("progress_dialog")?.let {
            if (it.isAdded)
                (it as ProgressDialogFragment).dismiss()
        }
    }


}
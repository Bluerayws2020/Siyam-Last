package com.example.siyam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.util.Log.e
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.example.siyam.HomeActivity
import com.example.siyam.LoginActivity
import com.example.siyam.R
import com.example.siyam.RequestLoginActivity
import com.example.siyam.databinding.FragmentAboutSiyamBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.HelperUtils.toHTML
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.show
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class AboutSiyamGroupFragment: AppCompatActivity() {

    private lateinit var binding: FragmentAboutSiyamBinding

    private val appVM by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = FragmentAboutSiyamBinding.inflate(layoutInflater)


        HelperUtils.setDefaultLanguage(this,"en")

        binding.includeTab.tabTitle.text = "About Siyam Group"
        binding.includeTab.personImg.setOnClickListener{
            if (HelperUtils.getUID(this) == "0"){
                startActivity(Intent(this, LoginActivity::class.java))

            }else {
                startActivity(Intent(this, ViewProfile::class.java))

            }
        }
        showProgress()
appVM.retriveAboutUs()
        getAboutUs()
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        binding.includeTab.menuImg.setImageDrawable(resources.getDrawable(R.drawable.back))

        binding.includeTab.menuImg.setOnClickListener{
            onBackPressed()
        }



        binding.includeTab.personImg.setOnClickListener{
            if (HelperUtils.getUID(this) == "0"){
                startActivity(Intent(this, LoginActivity::class.java))

            }else {
                startActivity(Intent(this, ViewProfile::class.java))

            }
        }


    }


    fun getAboutUs(){

        appVM.getAbouus().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()

                    if (result.data.msg.status == 200) {

//


                        val data = result.data.data[0]
                        d("ayham",result.data.data[0].Image)
                        Glide.with(this).load(result.data.data[0].Image)
                        //https://www.siyamradiators.com/sites/default/files/2023-06/new-project-18-1.jpg
                        //https://www.siyamradiators.com/sites/default/files/2023-06/new-project-18-1.jpg
                            .placeholder(R.drawable.siyamlogo)
                            .into(binding.headerImg)
                        binding.body .setText(data.body.toHTML())

                    } else {
                        showMessage("err")

                    }
                }
                is NetworkResults.Error -> {
                    hideProgress()
                    result.exception.printStackTrace()
                    if (result.exception is HttpException)
                        showMessage(result.exception.message())
                    else
                        showMessage("No Internet connection")
                }
            }
        }







    }
    private fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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
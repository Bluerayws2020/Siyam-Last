package com.example.siyam.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.siyam.LoginActivity
import com.example.siyam.R
import com.example.siyam.RequestLoginActivity
import com.example.siyam.databinding.ActivityLoginBinding
import com.example.siyam.databinding.ActivitySearchResultBinding
import com.example.siyam.fragments.HomeFragment
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.show
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class SearchResultActivity : AppCompatActivity() {

    private val appVM by viewModels<AppViewModel>()
    private lateinit var binding: ActivitySearchResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)



        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"en")

        binding.includeTab.tabTitle.text = "Search Result"
        binding.includeTab.menuImg.setImageDrawable(resources.getDrawable(R.drawable.back))

        binding.includeTab.menuImg.setOnClickListener {
            onBackPressed()
        }


        binding.includeTab.personImg.setOnClickListener{
            if (HelperUtils.getUID(this) == "0"){
                startActivity(Intent(this, LoginActivity::class.java))

            }else {
                startActivity(Intent(this, ViewProfile::class.java))

            }
        }

getProudectByPartNumber()



        binding.showResultButton.setOnClickListener {
            startActivity(Intent(this@SearchResultActivity, SearchDetailsActivity::class.java))
        }

        showProgress()
        appVM.getProductByPartNum(
            HomeFragment.partNumber
        )

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

   fun getProudectByPartNumber(){

       appVM.getProductByPartNumResponse().observe(this) { result ->
           when (result) {
               is NetworkResults.Success -> {
                   hideProgress()
                   if (result.data.msg.status == 200) {

//
                       val data = result.data.data
                       binding.txtHeight2.text = data.width
                       binding.txtWidth2.text = data.width
                       binding.saerchNumber.text = data.part_number
                       binding.txtPart2.text = data.part_number
                       binding.txtFilter2.text = data.radiator_type
                       binding.txtOutlet2.text = data.outlet
                       binding.txtOilCooler2.text = data.oli_cooler
                       binding.pdRecycler.visibility = View.GONE
                       binding.txtTitle2.text = data.title
                       binding.txtSiyamRef2.text = data.siyam_ref
                       binding.Nots2.text = data.nots
                       binding.RadiatorType2.text = data.radiator_type
                       binding.CarType2.text = data.car_type
                       binding.modelFitment2.text = data.model_fitment
                       binding.Year.text = data.year
                       binding.CoreSize.text = data.core_size
                       binding.Rows.text = data.rows
                       binding.Transmission.text = data.transmission
                       binding.OME.text = data.OME
                       Glide.with(this).load(data.product_image).placeholder(R.drawable.siyamlogo).into(binding.productImage)
                       binding.inletPipe.text = data.inlet_pipe
                       binding.outletPipe.text = data.outlet_pipe

                   } else {
                       Toast.makeText(this, result.data.msg.msg, Toast.LENGTH_SHORT).show()
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
}
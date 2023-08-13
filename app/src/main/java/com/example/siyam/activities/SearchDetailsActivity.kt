package com.example.siyam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.siyam.LoginActivity
import com.example.siyam.R
import com.example.siyam.RequestLoginActivity
import com.example.siyam.databinding.ActivitySearchResult2Binding
import com.example.siyam.databinding.ActivitySearchResultBinding
import com.example.siyam.fragments.HomeFragment
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.HelperUtils.toHTML
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.show
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class SearchDetailsActivity: AppCompatActivity() {


    private lateinit var binding: ActivitySearchResult2Binding

    private val appVM by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResult2Binding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()
        HelperUtils.setDefaultLanguage(this,"en")

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.includeTab.tabTitle.text = "Search Result"
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
        showProgress()
        appVM.getProductByPartNum(
            HomeFragment.partNumber
        )
getProudectByPartNumber()







    }

    fun getProudectByPartNumber() {

        appVM.getProductByPartNumResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()
                    if (result.data.msg.status == 200) {

//
                        val data = result.data.data



                        binding.partNumber.text = "${data.part_number} - $title"?.toHTML()
                        binding.type.text = data.radiator_type
                        binding.fitmentModel.text = data.model_fitment
                        binding.coreSize.text = data.core_size
                        binding.noOfRows.text = data.rows
                        binding.inletPipe.text = data.inlet_pipe
                        binding.outletPipe.text = data.outlet_pipe
                        binding.transmission.text = data.transmission
                        binding.OME.text = data.OME
                        binding.year.text = data.year
//                        binding.hight.text =data.core_size.toHTML()
////                        height?.toHTML()
//                        binding.width.text = data.width.toHTML()
//                        binding.depth.text = data.radiator_type.toHTML()
//                        binding.overallhight.text = data.outlet.toHTML()
////                        binding.ptype.text = data.radiator_type.toHTML()
//                        binding.orintation.text = data.core_size?.toHTML()
//                        binding.filter.text = data.model_fitment?.toHTML()
//                        binding.nilkt.text = data.rows?.toHTML()
//                        binding.outlet.text = data.OME.toHTML()
//                        binding.oil.text = data.oli_cooler .toHTML()

                        Glide.with(this@SearchDetailsActivity)

                            .load(data.product_image)
                            .centerInside()

                            .placeholder(R.drawable.siyamlogo)
                            .error(R.drawable.user)
                            .into(binding.img33)





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
    fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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
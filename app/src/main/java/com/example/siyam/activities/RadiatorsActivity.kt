package com.example.siyam.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siyam.LoginActivity
import com.example.siyam.R
import com.example.siyam.RequestLoginActivity
import com.example.siyam.adapters.HomeItemsAdapter
import com.example.siyam.adapters.OnClickListener
import com.example.siyam.adapters.ProductItemAdapter
import com.example.siyam.adapters.RadiatorsItemAdapter
import com.example.siyam.databinding.ActivityProductsBinding
import com.example.siyam.databinding.ActivityRadiatorsBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.show
import com.example.siyam.model.GetContentList
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class RadiatorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRadiatorsBinding
    private var radiatorsItemAdapter : RadiatorsItemAdapter?= null
    private var dataList : List<GetContentList>  = arrayListOf()
    private val appVM by viewModels<AppViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRadiatorsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()
        HelperUtils.setDefaultLanguage(this,"en")

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        getContentByCatId()

//        staticData()


        binding.includeTab.tabTitle.text = "RADIATORS"
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


        val categoryId = intent.getStringExtra("category_id")
        Log.d("Category _ ID = ", " onCreate:  $categoryId")


        appVM.getContentByCatId(categoryId.toString())

        binding.searchBtn.setOnClickListener{
            if (HelperUtils.getUID(this).isNullOrEmpty()) {
            }else {
                startActivity(Intent(this@RadiatorsActivity, SearchResultActivity::class.java))
            }
            }


    }




//    private fun staticData() {
//        dataList.add("aya")
//        dataList.add("Zekra")
//        dataList.add("Batta")
//        dataList.add("Batta")
//        dataList.add("Batta")
//    }




    private fun getContentByCatId(){

        appVM.getContentByCatIdResponse().observe(this@RadiatorsActivity) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    showProgress()
                    if (result.data.msg.status == 200) {


                        dataList = result.data.data

//        Product Item Adapter

                        val lm = GridLayoutManager(this@RadiatorsActivity, 2)
                        radiatorsItemAdapter = RadiatorsItemAdapter(dataList, applicationContext, object : OnClickListener{
                            override fun onMove(position: Int) {

                                startActivity(Intent(this@RadiatorsActivity, RadiatorsInfoActivity::class.java))
                            }

                        })
                        binding.rvRadiators.layoutManager = lm
                        binding.rvRadiators.adapter = radiatorsItemAdapter



                    } else {
                        showMessage(result.data.msg.msg)
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
        Toast.makeText(this@RadiatorsActivity, message, Toast.LENGTH_LONG).show()

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
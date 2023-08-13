package com.example.siyam.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.example.siyam.LoginActivity
import com.example.siyam.R
import com.example.siyam.RequestLoginActivity
import com.example.siyam.adapters.OnClickListener
import com.example.siyam.adapters.ProductItemAdapter
import com.example.siyam.adapters.RadiatorsItemAdapter
import com.example.siyam.databinding.ActivityProductsBinding
import com.example.siyam.databinding.ActivityRadiatorsBinding
import com.example.siyam.fragments.HomeFragment
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.HelperUtils.CATALOGUE_LIST
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.show
import com.example.siyam.model.CatalogeList
import com.example.siyam.model.GetContentList
import com.example.siyam.model.ImageLink
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class ProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRadiatorsBinding
    private var productItemAdapter : ProductItemAdapter?= null
    private var dataList : List<GetContentList>  = arrayListOf()
    private val appVM by viewModels<AppViewModel>()


    companion object{
        var imageList : MutableList<ImageLink> = mutableListOf()
        var catalogueList : List<CatalogeList>  = arrayListOf()
        var mainImg: String ?= null
        var description: String ?= null
        var categoryId: String ?= null
        var link: String ?= null

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRadiatorsBinding.inflate(layoutInflater)
        HelperUtils.setDefaultLanguage(this,"en")

        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.partNum.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                if (HelperUtils.getUID(this) == "0") {
                    showMessage("you have to register or login if you have an account!")
                    startActivity(Intent(this@ProductsActivity, LoginActivity::class.java))
                } else {
                    appVM.getProductByPartNum(
                        binding.partNum.text.toString()
                    )
                    HomeFragment.partNumber = binding.partNum.text.toString()


            }
                return@setOnEditorActionListener true
            }
            false
        }




    getContentByCatId()

        // set the cat name
        val categoryName  = intent.getStringExtra("cat_name").toString()
        binding.text1.text = "Check All $categoryName Models By \n SIYAM RADIATORS"


        showProgress()
//        staticData()



//        binding.includeTab.tabTitle.text = "RADIATORS"

        val categoryId = intent.getStringExtra("category_id")
        Log.d("Category _ ID = ", " onCreate:  $categoryId")

        appVM.getContentByCatId(categoryId.toString())



        getProductByPartNumberApi()

        binding.searchBtn.setOnClickListener {
            if (HelperUtils.getUID(this) == "0") {
                showMessage("you have to register or login if you have an account!")
                startActivity(Intent(this@ProductsActivity, LoginActivity::class.java))
            } else {
                appVM.getProductByPartNum(
                    binding.partNum.text.toString()
                )
                HomeFragment.partNumber = binding.partNum.text.toString()

            }
        }



        val partNum = intent.getStringExtra("part_number")
        val height = intent.getStringExtra("Height")
        val width = intent.getStringExtra("Width")
        val filterNeck = intent.getStringExtra("Filter_Neck")
        val outlet = intent.getStringExtra("Outlet")
        val oilCooler = intent.getStringExtra("Oil_Cooler")


        Log.d("Category _ ID = ", " onCreate:  $partNum")






//        Product Item Adapter
//        val lm = GridLayoutManager(this@ProductsActivity, 2)
//        productItemAdapter = ProductItemAdapter(dataList, applicationContext)
//        binding.rvProducts.layoutManager = lm
//        binding.rvProducts.adapter = productItemAdapter


        binding.includeTab.tabTitle.text = "Our Products"
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




    private fun getProductByPartNumberApi(){
        appVM.getProductByPartNumResponse().observe(this@ProductsActivity) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()
                    if (result.data.msg.status == 200) {

                        val intent = Intent(this@ProductsActivity, SearchDetailsActivity::class.java)
                  
                        startActivity(intent)
                        Toast.makeText(this@ProductsActivity, result.data.msg.msg, Toast.LENGTH_SHORT).show()
//                        binding.progressBarUserInstitution.visibility = View.GONE


                    } else {
                        Toast.makeText(this@ProductsActivity, result.data.msg.msg, Toast.LENGTH_SHORT).show()
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



    private fun getContentByCatId(){
        appVM.getContentByCatIdResponse().observe(this@ProductsActivity) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()
                    if (result.data.msg.status == 200) {


                        dataList = result.data.data

                        //        Product Item Adapter

                        val lm = GridLayoutManager(this@ProductsActivity, 2)
                        productItemAdapter = ProductItemAdapter(dataList, applicationContext, object : OnClickListener {
                            override fun onMove(position: Int) {
if( result.data.data[position].Images.isNullOrEmpty()){
//    imageList = arrayListOf<String>()

}else {
    imageList.clear()
    imageList += result.data.data[position].Images!!
}

                                Log.d("LLAAA", imageList.toString())
                                mainImg = result.data.data[position].cover_Image
                                description = result.data.data[position].body
                                catalogueList = result.data.data[position].cataloge
                                 categoryId = intent.getStringExtra("category_id")
                                    link = result.data.data[position].file

                                val intent = Intent(this@ProductsActivity, RadiatorsInfoActivity::class.java)
                               // intent.putExtra("catalogueList",result.data.data[position].cataloge as ArrayList<CatalogeList>)
                                CATALOGUE_LIST = result.data.data[position].cataloge
                                startActivity(intent)

                                Log.d("CATA LIST = ", "onMove:  $imageList")
                            }

                        })
                        binding.rvRadiators.layoutManager = lm
                        binding.rvRadiators.adapter = productItemAdapter

//                        binding.progressBarUserInstitution.visibility = View.GONE


                    } else {
                        hideProgress()

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
        Toast.makeText(this@ProductsActivity, message, Toast.LENGTH_LONG).show()
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
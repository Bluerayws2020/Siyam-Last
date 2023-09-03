package com.example.siyam.activities

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.siyam.InterFace.CatlogeClick
import com.example.siyam.LoginActivity
import com.example.siyam.R
import com.example.siyam.RequestLoginActivity
import com.example.siyam.WebViewCatloge
import com.example.siyam.activities.ProductsActivity.Companion.catalogueList
import com.example.siyam.activities.ProductsActivity.Companion.link
import com.example.siyam.adapters.*
import com.example.siyam.databinding.ActivityRadiatorsInfoBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.HelperUtils.CATALOGUE_LIST
import com.example.siyam.helpers.HelperUtils.toHTML
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.show
import com.example.siyam.model.CatalogeList
import com.example.siyam.model.ImageLink
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException
import java.io.File

class RadiatorsInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRadiatorsInfoBinding
    private var imageAdapter : ImageAdapter?= null
    private var catalogueAdapter : CatalogueAdapter?= null
    private var dataList : List<ImageLink>  = arrayListOf()
    private val appVM by viewModels<AppViewModel>()
    var mpopup: PopupWindow? = null
    private var catalogueList :ArrayList<CatalogeList> = arrayListOf()

    private var productItemAdapter : ProductItemAdapter?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRadiatorsInfoBinding.inflate(layoutInflater)
        HelperUtils.setDefaultLanguage(this,"en")

        setContentView(binding.root)



        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val categoryId = intent.getStringExtra("category_id")

//        getContentByCatId()
//        appVM.getContentByCatId(categoryId.toString())

        binding.includeTab.tabTitle.text = "RADIATORS INFO"
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



        Log.d("Cata List22 = ", "downloadPopUp: =" + ProductsActivity.imageList)

Log.d("<M<!@#", ProductsActivity.imageList.toString())
        //        Product Item Adapter
        if (ProductsActivity.imageList.isNullOrEmpty()){

        }else {

            val lm = LinearLayoutManager(
                this@RadiatorsInfoActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            imageAdapter = ImageAdapter(ProductsActivity.imageList, object : CatlogeClick {
                override fun displayCatloge(link: String) {
                   Glide.with(this@RadiatorsInfoActivity).load(link).placeholder(R.drawable.glide_place_holder).into(binding.img1)
                }

                override fun displayCatlogeimage(link: String, img: ImageView) {
                    Glide.with(this@RadiatorsInfoActivity)
                        .load(link)
                        .centerCrop()

                        .placeholder(R.drawable.siyamlogo)
                        .error(R.drawable.user)
                        .into(img)

                }



            }, applicationContext)
            binding.rvImgs.layoutManager = lm
            binding.rvImgs.adapter = imageAdapter
            imageAdapter?.listener {
                link,image,pos ->
                Glide.with(this@RadiatorsInfoActivity).load(link).placeholder(R.drawable.glide_place_holder).into(binding.img1)
                Glide.with(this@RadiatorsInfoActivity).load(ProductsActivity.mainImg).placeholder(R.drawable.glide_place_holder).into(image)
                ProductsActivity.imageList[pos] = ImageLink(ProductsActivity.mainImg!!)
                ProductsActivity.mainImg = link
            }
        }


        Glide.with(this@RadiatorsInfoActivity)
            .load(ProductsActivity.mainImg)
            .placeholder(R.drawable.siyamlogo)
            .error(R.drawable.user)
            .into(binding.img1)


        // load the html text desc of the
        // radiator
        binding.body.text = removeHeaderFromHTML(ProductsActivity.description!!).toHTML()
        Log.w("ayham", "${ProductsActivity.description}")
        binding.rectangleTV.text = extractHeaderFromHTML(ProductsActivity.description!!)?.toHTML()
//        if (HelperUtils.getUID(this).isNullOrEmpty()){
//            binding.includeTab.personImg.hide()
//        }else {
//            binding.includeTab.personImg.show()
//
//        }
        if (HelperUtils.getUID(this) == "0"){

        }else{
            binding.signInButton.hide()

        }
        binding.signInButton.setOnClickListener {

            //if (HelperUtils.getUID(this).isNullOrEmpty()){
//                binding.pd.hide()
//                binding.signInButton.show()
                startActivity(Intent(this@RadiatorsInfoActivity, LoginActivity::class.java))

            //}
        }

        // init catalogue adapter and recycler
        if (ProductsActivity.catalogueList.isEmpty()) {
            binding.pd.hide()
            Toast.makeText(this, "Theres No File For This Catloge", Toast.LENGTH_SHORT).show()
            binding.rvCatalogue.hide()
        }else{
            binding.pd.hide()
            catalogueAdapter = CatalogueAdapter(CATALOGUE_LIST,this,object :CatlogeClick{
            override fun displayCatloge(link: String) {

                if(link.isNullOrEmpty()){
                    showMessage("Can't Download")
                }else{
                    val url = link

                    // Convert the PDF URL to a Google Drive URL
                    val driveUrl = url
                    val intent = Intent(this@RadiatorsInfoActivity,WebViewActivity::class.java)
                    intent.putExtra("url",driveUrl)
                    startActivity(intent)

                }
            }

            override fun displayCatlogeimage(link: String, img: ImageView) {

            }

        })
            val lm2 = LinearLayoutManager(this@RadiatorsInfoActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.rvCatalogue.layoutManager = lm2
            binding.rvCatalogue.adapter = catalogueAdapter
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


    // tke the header only from the html
    private fun extractHeaderFromHTML(html: String): String? {
        // Regular expression pattern to match the header text within <h3> tags
        val pattern = Regex("<h3>(.+?)</h3>")

        // Find the first match of the pattern in the HTML
        val matchResult = pattern.find(html)

        // If a match is found, extract and return the header text
        return matchResult?.value
    }

    // remove the header from the html
    private fun removeHeaderFromHTML(html: String): String {
        // Regular expression pattern to match the header text within <h3> tags
        val pattern = Regex("<h3>(.+?)</h3>")

        // Replace the header text with an empty string
        return pattern.replace(html, "")
    }




}
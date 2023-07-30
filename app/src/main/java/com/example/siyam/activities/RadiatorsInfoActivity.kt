package com.example.siyam.activities

import android.app.ActionBar
import android.app.DownloadManager
import android.content.*
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Log.e
import android.util.Log.w
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
                   Glide.with(this@RadiatorsInfoActivity)
                       .load(link)
                       .placeholder(R.drawable.glide_place_holder)
                       .error(R.drawable.user)
                       .into(binding.img1)
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
            .centerCrop()

            .placeholder(R.drawable.siyamlogo)
            .error(R.drawable.user)
            .into(binding.img1)



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

        binding.downloadCatalogueBtn.setOnClickListener {
            if (ProductsActivity.catalogueList.isEmpty()) {
                Toast.makeText(this, "Theres No File For This Catloge", Toast.LENGTH_SHORT).show()
            }else{
                binding.pd.hide()
            binding.downloadCatalogueBtn.show()
            downloadPopUp()
//                intent = Intent(this, WebViewCatloge::class.java)
//                intent.putExtra("link", ProductsActivity.link)
//                startActivity(intent)

//                val url = link
//                val  request = DownloadManager.Request(Uri.parse(url))
//                    .setTitle("syiam$link")
//                    .setDescription("Wating")
//                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
//                    .setAllowedOverMetered(true)
//                val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//                dm.enqueue(request)
//showMessage("Download successfully")


//            }
            }}
//            showMessage("Download Failed You May download file before")

        }








    private fun downloadPopUp() {
//        hideProgress()

        val popUpView: View = layoutInflater.inflate(
            R.layout.popup_window_catalogue,
            null
        ) // inflating popup layout

        mpopup = PopupWindow(
            popUpView, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT, true
        ) // Creation of popup

        mpopup!!.animationStyle = android.R.style.Animation_Dialog
        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup

        val progressBar = popUpView.findViewById<View>(R.id.progressBarStationItem) as ProgressBar
        progressBar.visibility = View.GONE

        catalogueAdapter = CatalogueAdapter(CATALOGUE_LIST,this,object :CatlogeClick{
            override fun displayCatloge(link: String) {

                if(link.isNullOrEmpty()){
                    showMessage("Can't Download")
                }else{
                    val url = link
//                    val pdfUrl = "https://www.siyamradiators.com/sites/default/files/2023-06/american-trucks-aluminum-models-no.-1_1.pdf"

                    // Convert the PDF URL to a Google Drive URL
                    val driveUrl = "https://drive.google.com/viewerng/viewer?url=${url}"
                    val intent = Intent(this@RadiatorsInfoActivity,WebViewActivity::class.java)
                    intent.putExtra("url",driveUrl)
                    startActivity(intent)

//                    e("ayham",driveUrl)
//
//                    val intent = Intent(Intent.ACTION_VIEW)
//                    intent.data = Uri.parse(driveUrl)
//
//                    try {
//                        startActivity(intent)
//                    } catch (e: ActivityNotFoundException) {
//                        // Handle case when no browser app is installed
//                        Toast.makeText(this@RadiatorsInfoActivity, "No browser application found",Toast.LENGTH_SHORT).show()
//                    }

//                    showMessage("Download initiated")
                    mpopup!!.dismiss()



                }
            }

            override fun displayCatlogeimage(link: String, img: ImageView) {

            }

        })

        val catalogueRV = popUpView.findViewById<RecyclerView>(R.id.rvCatalogue) as RecyclerView
        val dismiss = popUpView.findViewById<ConstraintLayout>(R.id.dismiss) as ConstraintLayout

        val lm2 = LinearLayoutManager(this@RadiatorsInfoActivity, LinearLayoutManager.VERTICAL, false)
        catalogueRV.layoutManager = lm2
        catalogueRV.adapter = catalogueAdapter

//        catalogueAdapter = CatalogueAdapter(ProductsActivity.catalogueList, applicationContext,object : CatlogeClick{
//            override fun displayCatloge(link: String) {
//
////                val intent = Intent(this@RadiatorsInfoActivity, WebViewCatloge::class.java)
////            intent.putExtra("link",link)
////                startActivity(intent)
//
//                    val webpage: Uri = Uri.parse(link)
//                    val intent = Intent(Intent.ACTION_VIEW, webpage)
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }

//---------------------this is commented all but must be relooked at -----------------------------------------------
//                intent = Intent(this, WebViewCatloge::class.java)
//                intent.putExtra("link", ProductsActivity.link)
//                startActivity(intent)
//            }
//
//            override fun displayCatlogeimage(link: String, img: ImageView) {
//                TODO("Not yet implemented")
//            }
//
//        })


        dismiss.setOnClickListener{
            mpopup!!.dismiss()
        }





    }


    private fun showMessage(message: String?) =
        Toast.makeText(this@RadiatorsInfoActivity, message, Toast.LENGTH_LONG).show()
    private fun showProgress() {
        ProgressDialogFragment().show(supportFragmentManager, "progress_dialog")
    }

    private fun hideProgress() {
        supportFragmentManager.findFragmentByTag("progress_dialog")?.let {
            if (it.isAdded)
                (it as ProgressDialogFragment).dismiss()
        }
    }

//    private fun openDownloadedPDFFile() {
//        // Open the downloaded PDF file
//        val file = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            "filename.pdf"
//        )
//        val uri = FileProvider.getUriForFile(
//            this,
//            "com.example.siyam.fileprovider",
//            file
//        )
//
//        val pdfIntent = Intent(Intent.ACTION_VIEW)
//        pdfIntent.setDataAndType(uri, "application/pdf")
//        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//        // Verify if there is an activity available to handle the intent
//        val activities: List<ResolveInfo> = packageManager.queryIntentActivities(pdfIntent, 0)
//        if (activities.isNotEmpty()) {
//            startActivity(pdfIntent)
//        } else {
//            // Handle case when no PDF viewer application is installed
//            Toast.makeText(this,"No Pdf Reader Found",Toast.LENGTH_SHORT).show()
//        }
//    }






}
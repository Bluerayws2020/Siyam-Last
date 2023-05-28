package com.example.siyam.activities

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.siyam.LoginActivity
import com.example.siyam.R
import com.example.siyam.RequestLoginActivity
import com.example.siyam.databinding.FragmentContactUsBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class ContactUsFragment: AppCompatActivity() {

    private lateinit var binding: FragmentContactUsBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val appVM by viewModels<AppViewModel>()

    private var PHOEN = ""
    private var EMAIL = ""
    private var FACEBOOK = ""
    private var TWITTER = ""
    private var ADDRESS = ""
//    private var WHATS_UP_NUMBER = ""
//    private var MAP_LOCATION =

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentContactUsBinding.inflate(layoutInflater)
        HelperUtils.setDefaultLanguage(this,"en")

        appVM.retreveContactInfo()

        binding.includeTab.tabTitle.text = "Contact Us"
        if (HelperUtils.getUID(this).isNullOrEmpty()){



        }else {

        }

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

        getContactInfo()
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.facebookLogo.setOnClickListener{
            try {
                openApp(
                    url = "fb://facewebmodal/f?href=$FACEBOOK",
                    mPackage = "com.facebook.katana",
                    alternativeUrl = FACEBOOK,
                )
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                openBrowser(FACEBOOK)
            }
        }

        binding.instagramLogo.setOnClickListener{
            openBrowser(TWITTER)

        }

        binding.email.setOnClickListener{
            sendEmail(EMAIL)
        }
        binding.pnum.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPhoneCallPermission.launch(Manifest.permission.CALL_PHONE)
            } else
                phoneCall(PHOEN)
        }
        binding.address.setOnClickListener{
            openApp(ADDRESS, "com.google.android.apps.maps")

        }

    }

    private val requestPhoneCallPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showMessage("done")
                phoneCall(PHOEN)
            } else
                showMessage("denied")
        }
    fun getContactInfo(){

        appVM.getContactInfo().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()

                    hideProgress()
                    if (result.data.msg.status == 200) {

//
val data = result.data.data
                        binding.email.text = data.Email
                        binding.fax.text = data.fax
                        binding.pnum.text = data.Phone
                        binding.pobox.text = data.PO_BOX
                        binding.address.text = data.Address

PHOEN = data.Phone
                        ADDRESS = data.Address
                        FACEBOOK = data.facebook
                        TWITTER = data.twitter


                    } else {
                        showMessage("err")

                    }
                }
                is NetworkResults.Error -> {
                    hideProgress()
                    result.exception.printStackTrace()
                    Log.d("LLLKKL", result.exception.message.toString())
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

    fun openBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (browserIntent.resolveActivity(packageManager) != null) startActivity(
            browserIntent
        ) else showMessage("err")
    }
    //open url in app
    @Throws(PackageManager.NameNotFoundException::class)
    fun openApp(url: String, mPackage: String, alternativeUrl: String = url) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(mPackage)
        val applicationInfo: ApplicationInfo =
            packageManager.getApplicationInfo(mPackage, 0)
        if (applicationInfo.enabled) {
            when {
                intent.resolveActivity(packageManager) != null -> startActivity(
                    intent
                )
                else -> openBrowser(alternativeUrl)
            }
        }
    }
    fun phoneCall(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }
    fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$email"))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            showMessage("Clint Have Not Valid Email")
        }
    }

}
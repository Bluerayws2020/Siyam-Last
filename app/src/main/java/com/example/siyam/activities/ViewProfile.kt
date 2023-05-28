package com.example.siyam.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.siyam.R
import com.example.siyam.databinding.ActivitySearchResult2Binding
import com.example.siyam.databinding.PersonalprofileBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class ViewProfile : AppCompatActivity() {
    private lateinit var binding: PersonalprofileBinding
    private val appVM by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PersonalprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.includeTab.tabTitle.text = "User Profile"
        binding.includeTab.menuImg.setImageDrawable(resources.getDrawable(R.drawable.back))
        HelperUtils.setDefaultLanguage(this,"en")

        binding.includeTab.menuImg.setOnClickListener{
            onBackPressed()
        }
showProgress()
        appVM.retreveUserProfile()

        getUserProfile()

    }

    fun getUserProfile(){

        appVM.getUserProfile().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()

                    hideProgress()
                    if (result.data.status == 200) {

//


                        val data = result.data.userprofile
                        binding.name.setText(data.name.toString())
                        binding.email.text = data.email.toString()

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
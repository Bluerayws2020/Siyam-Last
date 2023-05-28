package com.example.siyam

import android.app.ActionBar
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siyam.InterFace.CatlogeClick
import com.example.siyam.activities.ProductsActivity
import com.example.siyam.adapters.CatalogueAdapter
import com.example.siyam.databinding.ActivityLoginBinding
import com.example.siyam.databinding.ActivityRequestLoginBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class RequestLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestLoginBinding
    private val appVM by viewModels<AppViewModel>()
    var mpopup: PopupWindow? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        getRegisterApi()
        binding.signInButton.setOnClickListener{
            showProgress()
            appVM.userRegister(
                binding.firstNameET.text.toString(),
                binding.companyNameET.text.toString(),
                binding.emailAddressET.text.toString(),
                binding.phoneNumberET.text.toString(),
                binding.passwordET.text.toString()
                )
        }

        LoginActivity.flag = true

    }


    private fun getRegisterApi()
    {
//        showProgress()
        appVM.getRegisterResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.data.status == 200) {
hideProgress()
//                        startActivity(Intent(this@RequestLoginActivity, HomeActivity::class.java))
//                        Toast.makeText(applicationContext, result.data.data.msg, Toast.LENGTH_SHORT).show()

                 thankyouPopup(result.data.data.msg)

                    } else {
                        hideProgress()

                        Toast.makeText(this@RequestLoginActivity, result.data.data.msg, Toast.LENGTH_SHORT).show()
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

    private fun thankyouPopup(msg:String) {
//        hideProgress()

        val popUpView: View = layoutInflater.inflate(
            R.layout.popupregester,
            null
        ) // inflating popup layout

        mpopup = PopupWindow(
            popUpView, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT, true
        ) // Creation of popup

        mpopup!!.animationStyle = android.R.style.Animation_Dialog
        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup

//mpopup.background = resources.getColor(R.color.purple_200)


        val txt = popUpView.findViewById<TextView>(R.id.msg) as TextView
        txt.text = msg
        Handler(Looper.getMainLooper()).postDelayed({
            mpopup?.dismiss()
            startActivity(Intent(this@RequestLoginActivity, HomeActivity::class.java))
finishAffinity()
        }, 2000)


    }


    private fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()




//    private fun saveUserDataRegister(user: UserModel) {
//        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
//        sharedPreferences.edit().apply {
//            putString("uid", user.uid.toString())
//            putString("user_type", user.type)
//
//        }.apply()
//    }

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
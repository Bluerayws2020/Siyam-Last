package com.example.siyam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.siyam.databinding.ActivityHomeBinding
import com.example.siyam.databinding.ActivityLoginBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.isInputEmpty
import com.example.siyam.helpers.ViewUtils.show
import com.example.siyam.model.NetworkResults
import com.example.siyam.viewModel.AppViewModel
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val appVM by viewModels<AppViewModel>()


    companion object{
        var flag = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.registerButton.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RequestLoginActivity::class.java))
        }


        getLoginApi()



        binding.signInButton.setOnClickListener{


            if(binding.userNameET.isInputEmpty()) {
                showMessage("Username is required!")
            }
            else if(binding.passwordET.isInputEmpty()) {
                showMessage("Password is required!" )
            }
            else if(binding.userNameET.isInputEmpty() && binding.passwordET.isInputEmpty()){
                showMessage("Username and Password is required!" )
            }
            else {

                binding.pd.show()
                binding.signInButton.hide()
                appVM.userLogin(
                    binding.userNameET.text.toString(),
                    binding.passwordET.text.toString()
                )

                flag = true
            }
        }


    }



    private fun getLoginApi(){
//showProgress()
        appVM.getLoginResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {



                    binding.pd.hide()
                    binding.signInButton.show()



                    if (result.data.data.status == 200) {
//hideProgress()
                        saveCarData(result.data.data.user_info.uid,result.data.data.msg)

                    } else {
                        Toast.makeText(this@LoginActivity," ${result.data.data.msg}", Toast.LENGTH_SHORT).show()

                        binding.pd.hide()
                        binding.signInButton.show()

                    }
                }
                is NetworkResults.Error -> {

                    binding.pd.hide()
                    binding.signInButton.show()

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

//    private fun showProgress() {
//        ProgressDialogFragment().show(supportFragmentManager, "progress_dialog")
//    }
//
//    private fun hideProgress() {
//        supportFragmentManager.findFragmentByTag("progress_dialog")?.let {
//            if (it.isAdded)
//                (it as ProgressDialogFragment).dismiss()
//        }
//    }


    private fun saveCarData(uid:String,msg:String) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("uid", uid)

        }.apply()

        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        finishAffinity()
    }
}
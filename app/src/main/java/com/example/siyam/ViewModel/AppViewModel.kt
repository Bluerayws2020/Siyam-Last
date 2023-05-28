package com.example.siyam.viewModel

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.model.*
import com.example.siyam.repo.NetworkRepository
import kotlinx.coroutines.launch

class AppViewModel(application: Application): AndroidViewModel(application)  {

    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)
    private val repo = NetworkRepository
    private val language = "ar"
    private val uid = HelperUtils.getUID(application.applicationContext)
    private val sharedpreferences: SharedPreferences =
        application.getSharedPreferences(HelperUtils.SHARED_PREF, AppCompatActivity.MODE_PRIVATE)



    private val loginUserMessageLiveData = MutableLiveData<NetworkResults<UserLoginModel>>()
    private val registerUserMessageLiveData = MutableLiveData<NetworkResults<UserRegisterModel>>()
    private val getProductByPartNumMessageLiveData = MutableLiveData<NetworkResults<GetProductByPartNumber>>()
    private val getAllCategoryMessageLiveData = MutableLiveData<NetworkResults<GetAllCategory>>()
    private val getContentByCatIdMessageLiveData = MutableLiveData<NetworkResults<GetContentByCatId>>()

    private val getUserProfileLivedata = MutableLiveData<NetworkResults<ViewUserProfile>>()


    private val getContactinfoLiveData = MutableLiveData<NetworkResults<ContactInfo>>()

    private val getAboutUsLiveData = MutableLiveData<NetworkResults<AboutUs>>()




    fun userLogin(userName: String, password: String){
        viewModelScope.launch{
            loginUserMessageLiveData.value = repo.userLogin(userName, password)
        }
    }



//    userRegister


    fun userRegister(userName: String, companyName: String, email: String, phoneNumber: String, password: String){
        viewModelScope.launch{
            registerUserMessageLiveData.value = repo.userRegister(userName, companyName, email, phoneNumber, password)
        }
    }


//    getProductByPartNum


    fun getProductByPartNum(part_number: String){
        viewModelScope.launch{
            getProductByPartNumMessageLiveData.value = repo.getProductByPartNum(part_number)
        }
    }


//    getAllCategory

    fun getAllCategory(){
        viewModelScope.launch{
            getAllCategoryMessageLiveData.value = repo.getAllCategory()
        }
    }


//    getContentByCatId
    // here we go

    fun getContentByCatId(category_id: String){
        viewModelScope.launch{
            getContentByCatIdMessageLiveData.value = repo.getContentByCatId(category_id)
        }
    }

    fun retreveUserProfile(){
        viewModelScope.launch{
            getUserProfileLivedata.value = repo.getUserProfile(uid)
        }
    }
    fun retreveContactInfo(){
        viewModelScope.launch{
            getContactinfoLiveData.value = repo.getContactInfo()
        }
    }
    fun retriveAboutUs(){
        viewModelScope.launch{
            getAboutUsLiveData.value = repo.getAboutUs()
        }
    }


    fun getLoginResponse() = loginUserMessageLiveData
    fun getRegisterResponse() = registerUserMessageLiveData
    fun getProductByPartNumResponse() = getProductByPartNumMessageLiveData
    fun getAllCategoryResponse() = getAllCategoryMessageLiveData
    fun getContentByCatIdResponse() = getContentByCatIdMessageLiveData

    fun getUserProfile() = getUserProfileLivedata
    fun getContactInfo() = getContactinfoLiveData
    fun getAbouus() = getAboutUsLiveData


}
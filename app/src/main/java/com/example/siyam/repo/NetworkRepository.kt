package com.example.siyam.repo

import com.example.siyam.api.ApiClient
import com.example.siyam.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import java.lang.Exception

object NetworkRepository {


    suspend fun userLogin(
        user_name: String,
        password: String

    ): NetworkResults<UserLoginModel> {
        return withContext(Dispatchers.IO){
            val userNameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.userLogin(
                    userNameBody,
                    passwordBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun userRegister(
        user_name: String,
        company_name: String,
        email: String,
        phone_number: String,
        password: String
    ): NetworkResults<UserRegisterModel> {
        return withContext(Dispatchers.IO){
            val userNameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val companyNameBody = company_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneNumBody = phone_number.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.userRegister(
                    userNameBody,
                    companyNameBody,
                    emailBody,
                    phoneNumBody,
                    passwordBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

//    getProductByPartNum


    suspend fun getProductByPartNum(
        part_number: String

    ): NetworkResults<GetProductByPartNumber> {
        return withContext(Dispatchers.IO){
            val partNumBody = part_number.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getProductByPartNum(
                    partNumBody

                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

//    getAllCategory

    suspend fun getAllCategory(
    ): NetworkResults<GetAllCategory> {
        return withContext(Dispatchers.IO){
            try {
                val results = ApiClient.retrofitService.getAllCategory()
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


//    GetContentByCatId


    suspend fun getContentByCatId(
        category_id: String

    ): NetworkResults<GetContentByCatId> {
        return withContext(Dispatchers.IO){

            val categoryIdBody = category_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getContentByCatId(
                    categoryIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getUserProfile(
        uid: String,

    ): NetworkResults<ViewUserProfile> {
        return withContext(Dispatchers.IO){
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getUserInfo(
               uidBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }
    suspend fun getContactInfo(


        ): NetworkResults<ContactInfo> {
        return withContext(Dispatchers.IO){

            try {
                val results = ApiClient.retrofitService.getContentInfo(

                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getAboutUs(


    ): NetworkResults<AboutUs> {
        return withContext(Dispatchers.IO){

            try {
                val results = ApiClient.retrofitService.getAboutUs(

                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }
}
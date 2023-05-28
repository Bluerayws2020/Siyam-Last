package com.example.siyam.api

import okhttp3.RequestBody
import retrofit2.http.Multipart
import com.example.siyam.model.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiServices {

    @Multipart
    @POST("login")
    suspend fun userLogin(
        @Part("user_name") user_name: RequestBody,
        @Part("password") password: RequestBody,

    ): UserLoginModel



    @Multipart
    @POST("registration")
    suspend fun userRegister(
        @Part("name") name: RequestBody,
        @Part("company_name") company_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone_number") phone_number: RequestBody,
        @Part("password") password: RequestBody,

        ): UserRegisterModel

//    getProductByPartNumber

    @Multipart
    @POST("get-user-info")
    suspend fun getUserInfo(
        @Part("uid") uid: RequestBody,


        ): ViewUserProfile


    @POST("get-contact-info")
    suspend fun getContentInfo(


        ): ContactInfo

    @POST("get-about-us")
    suspend fun getAboutUs(


    ): AboutUs

    @Multipart
    @POST("get-produact-by-part-number")
    suspend fun getProductByPartNum(
        @Part("part_number") part_number: RequestBody

        ): GetProductByPartNumber


//    getAllCategory

    @POST("get-all-category")
    suspend fun getAllCategory(): GetAllCategory



//    GetContentByCatId

    @Multipart
    @POST("get-content-by-category-id")
    suspend fun getContentByCatId(
    @Part("category_id") category_id: RequestBody

    ): GetContentByCatId


}
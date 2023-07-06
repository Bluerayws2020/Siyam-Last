package com.example.siyam.model

import com.google.gson.annotations.SerializedName


sealed class NetworkResults<out R> {
        data class Success<out T>(val data: T) : NetworkResults<T>()
        data class Error(val exception: Exception) : NetworkResults<Nothing>()
    }



    data class MessageModel(
        @SerializedName("status") val status: Int,
        @SerializedName("message") val msg: String,
        @SerializedName("user_info") val user_info: UserInfo

    )


    data class UserLoginModel(
        @SerializedName("data") val data: MessageModel,


        )
data class UserInfo(
    @SerializedName("uid") val uid: String,
    @SerializedName("name") val name: String,
@SerializedName("email") val email: String

)
    data class UserRegisterModel(
        @SerializedName("data") val data: MessageModel

    )


    data class GetProductByPartNumber(
        @SerializedName("msg") val msg: MessageModel,
        @SerializedName("data") val data: GetProductList

        )

    data class GetProductList(
        val OME: String,
        val body: Any,
        val car_type: String,
        val cataloge: String,
        val core_size: String,
        val filler_neck: String,
        val height: String,
        val inlet_pipe: String,
        val model_fitment: String,
        val nid: String,
        val nots: String,
        val oli_cooler: String,
        val outlet: String,
        val outlet_pipe: String,
        val part_number: String,
        val product_image: String,
        val radiator_type: String,
        val rows: String,
        val siyam_ref: String,
        val title: String,
        val transmission: String,
        val width: String,
        val year: String


    )


    // get All Category


    data class GetAllCategory(
        @SerializedName("msg") val msg: MessageModel,
        @SerializedName("data") val data: ArrayList<GetCategoryList>

        )

    data class GetCategoryList(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("image") val image: String,
        @SerializedName("inner_image") val inner_image: String,

        )


// get content by cat-id

    data class GetContentByCatId(
        @SerializedName("msg") val msg: MessageModel,
        @SerializedName("data") val data: ArrayList<GetContentList>
    )



    data class GetContentList(
        @SerializedName("title") val title: String,
        @SerializedName("body") val body: String,
        @SerializedName("cover_Image") val cover_Image: String,
        @SerializedName("Images") val Images: List<ImageLink>? = null,
        @SerializedName("img_inner") val img_inner: String,
        @SerializedName("file") val file: String,
        @SerializedName("cataloge") val cataloge: List<CatalogeList>

        )

    data class CatalogeList(
        @SerializedName("img") val img: String,
        @SerializedName("pdf") val pdf: String,
        @SerializedName("title")val title: String

    )

    data class ImageLink(
        @SerializedName("img_link") val img_link: String

        )


data class ViewUserProfile(
    @SerializedName("status") val status: Int,
    @SerializedName("user") val userprofile: UserProfile

)
data class ContactInfo(
    @SerializedName("msg") val msg: MessageModel,
    @SerializedName("data") val data:ContactInfoData


)

data class ContactInfoData(
    @SerializedName("Title") val Title: String,
    @SerializedName("Address") val Address: String,
    @SerializedName("Email") val Email: String,
    @SerializedName("Phone") val Phone: String,
    @SerializedName("PO_BOX") val PO_BOX: String,
    @SerializedName("facebook") val facebook: String,
    @SerializedName("twitter") val twitter: String,
    @SerializedName("fax") val fax: String,


)
data class AboutUs(
    @SerializedName("msg") val msg: MessageModel,
    @SerializedName("data") val data:ArrayList<AboutUsDetails>




)

data class AboutUsDetails(
    @SerializedName("Title") val Title: String,
    @SerializedName("Body") val body: String,
    @SerializedName("Image") val Image:String

)


data class UserProfile(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)


//
//"Title": "Contact info",
//"Title": "<p>Don’t hesitate to<br />\r\n<strong>Contact Us</strong> if you need help.</p>\r\n\r\n<p>&nbsp;</p>\r\n",
//"Address": "https://goo.gl/maps/CnU6ZFXxGChLAPbd6",
//"Email": "mailto:sales@siyam-group.com",
//"Phone": "tel:0096264020130",
//"PO_BOX": null,
//"facebook": "https://www.facebook.com/siyamradiators",
//"twitter": "https://twitter.com/login"
package com.fabwalley.food.webservices.responsebean
import android.annotation.SuppressLint
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.google.gson.annotations.SerializedName


/**
 * Created By Rahul Kansara (Rk) \n Email : rahul.kansara10@gmail.com on 04-Aug-20.
 */

data class SearchResponse(
    val d: D
) {
    data class D(
        val responseCode: String, // 200
        val responseContainer: Any, // null
        val responseData: ArrayList<ResponseData>,
        val responseMessage: String // Success
    ) {
        data class ResponseData(
            val Price: Double, // 4.99
            val ProductAutoId: Int, // 29
            val ProductImage100px: String, // http://demoimage.mywaypantry.com/100_100_Thumbnail_1593688930000_2.jpg
            val ProductImage400px: String, // http://demoimage.mywaypantry.com/400_400_Thumbnail_1593688930000_2.jpg
            val ProductName: String, // Turkey & Cheese
            val Product_Detail: String, // Boar's Head cold cuts.
            val ShortDescription: String, // Turkey & Cheese
            val Discount: Double // Turkey & Cheese
        )
    }
}
package com.fabwalley.food.webservices.responsebean
import android.annotation.SuppressLint
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.google.gson.annotations.SerializedName


 data class CateResonse(
    val d: D
) {

     data class D(
         val responseCode: String,
         val responseContainer: Any,
         val responseData: ArrayList<ResponseData>,
         val responseMessage: String
     )

     data class ResponseData(
         val AutoId: Int,
         val CategoryName: String,
         val Thumbimg: String
     )
 }
package com.fabwalley.food.webservices.requestbean

import android.annotation.SuppressLint
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.google.gson.annotations.SerializedName


/**
 * Created By Rahul Kansara (Rk) \n Email : rahul.kansara10@gmail.com on 04-Aug-20.
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class SearchRequest(
    @SerializedName("requestContainer")
    val requestContainer: RequestContainer,
    @SerializedName("requestData")
    val requestData: RequestData
) : Parcelable {


    @SuppressLint("ParcelCreator")
    @Parcelize
    data class RequestContainer(
        @SerializedName("CartToken")
        val cartToken: String,
        @SerializedName("DeviceId")
        val deviceId: String,
        @SerializedName("LatLong")
        val latLong: String,
        @SerializedName("SecurityToken")
        val securityToken: String
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class RequestData(
        @SerializedName("CategoryId")
        val categoryId: Int,
        @SerializedName("ProductName")
        val productName: String
    ) : Parcelable
}
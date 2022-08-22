package com.fabwalley.food.webservices.requestbean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CompanyDetail(
    @SerializedName("requestContainer")
    var requestContainer: RequestContainer
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class RequestContainer(
        @SerializedName("CartToken")
        var cartToken: String,
        @SerializedName("appVersion")
        var appVersion: String,
        @SerializedName("RequestSource")
        var RequestSource: String,
        @SerializedName("DeviceId")
        var deviceId: String,
        @SerializedName("LatLong")
        var latLong: String,
        @SerializedName("SecurityToken")
        var securityToken: String
    ) : Parcelable
}
package com.fabwalley.food.webservices.requestbean

import android.annotation.SuppressLint
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.google.gson.annotations.SerializedName


@SuppressLint("ParcelCreator")
@Parcelize
data class CateRequest(
    @SerializedName("requestContainer")
    var requestContainer: RequestContainer
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class RequestContainer(
        @SerializedName("CartToken")
        var cartToken: String,
        @SerializedName("DeviceId")
        var deviceId: String,
        @SerializedName("LatLong")
        var latLong: String,
        @SerializedName("SecurityToken")
        var securityToken: String
    ) : Parcelable
}
package com.fabwalley.food.webservices.responsebean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created By Rahul Kansara (Rk) \n Email : rahul.kansara10@gmail.com on 04-Aug-20.
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class BaseResponse(
    @SerializedName("responseCode")
    val responseCode: String,
    @SerializedName("responseContainer")
    val responseContainer: String,
    @SerializedName("responseMessage")
    val responseMessage: String
) : Parcelable
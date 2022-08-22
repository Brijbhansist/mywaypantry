package com.fabwalley.food.webservices.responsebean
import android.annotation.SuppressLint
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.google.gson.annotations.SerializedName


/**
 * Created By Rahul Kansara (Rk) \n Email : rahul.kansara10@gmail.com on 05-Aug-20.
 */

data class ProDetailResponse(
    val d: D
) {

    data class D(
        val responseCode: String,
        val responseContainer: Any,
        val responseData: ArrayList<ResponseData>,
        val responseMessage: String
    )

    data class ResponseData(
        val ProductAutoId: Int,
        val ProductImage100px: String,
        val ProductImage400px: String,
        val ProductName: String,
        val ProductUnit: ArrayList<ProductUnit>,
        val Product_Detail: String,
        val ShortDescription: String,
        val Discount: Double
    )

    data class ProductUnit(
        val Category: ArrayList<Category>,
        var IsDefault: Int = 0,
        val Price: Double,
        val UnitAutoId: Int,
        val UnitName: String
    )

    data class Category(
        val CategoryAutoId: Int,
        val CategoryOption: ArrayList<CategoryOption>,
        val OptionCategory: String,
        val OptionIsMultiple: Int,
        val OptionIsRequired: Int
    )

    data class CategoryOption(
        val CategoryOptionAutoId: Int,
        val CategoryOptionName: String,
        var Price: Double
    )
}
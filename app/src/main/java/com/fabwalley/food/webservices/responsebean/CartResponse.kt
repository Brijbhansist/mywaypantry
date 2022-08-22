package com.fabwalley.food.webservices.responsebean
import com.google.gson.annotations.SerializedName


/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class CartResponse(
    val d: D? = D()
) {

    data class D(
        val responseMessage: String? = "",
        val responseCode: String? = "",
        val responseContainer: Any? = Any(),
        val responseData: List<ResponseData>? = listOf()
    )

    data class ResponseData(
        @SerializedName("CartAutoId")
        val cartAutoId: Int? = 0,
        @SerializedName("CartItemAutoId")
        val cartItemAutoId: Int? = 0,
        @SerializedName("ReferenceNumber")
        val referenceNumber: String? = ""
    )
}
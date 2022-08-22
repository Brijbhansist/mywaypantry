package com.fabwalley.food.webservices.responsebean

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class PlaceOrderResponse(
    val d: D
) {

    data class D(
        val responseCode: String,
        val responseData: ArrayList<ResponseData>,
        val responseMessage: String
    )

    data class ResponseData(
        val OrderId: String,
        val ReferenceNumber: String,
        val OrderDate: String,
        val OrderNote: String,
        val PrintOrder:String,
        val OrderTotal:Double
    )
}
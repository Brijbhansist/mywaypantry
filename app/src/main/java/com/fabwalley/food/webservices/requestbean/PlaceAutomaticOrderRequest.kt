package com.fabwalley.food.webservices.requestbean

import com.fabwalley.food.webservices.responsebean.OrderItem
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.io.SerializablePermission

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class PlaceAutomaticOrderRequest(
    var requestContainer: RequestContainer,
    var requestData: RequestData
):Serializable {

    data class RequestContainer(
        var appVersion: String,
        var CartToken: String,
        var DeviceId: String,
        var LatLong: String,
        var RequestSource: String,
        var SecurityToken: String
    )

    data class RequestData(
            var TimeStamp: String
    )

}
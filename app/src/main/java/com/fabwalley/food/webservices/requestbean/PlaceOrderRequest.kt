package com.fabwalley.food.webservices.requestbean

import com.fabwalley.food.webservices.responsebean.OrderItem
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.io.SerializablePermission

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class PlaceOrderRequest(
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
            var OrderInfo: OrderInfo
    )

    data class OrderInfo(
        var CoupanCode: String,
        var DeliveryType: String,
        var Email: String,
        var FirstName: String,
        var LastName: String,
        var NotifyEmail: String,
        var NotifySMS: String,
        var OrderItem: ArrayList<OrderItem>,
        var PaymentType: String,
        var Phoneno: String,
        var ReferenceNumber: String,
        var CartAutoId: String
    )



    data class OrderAddOnItem(
        var OptionAutoId: String,
        var Price: String,
        var addOnName:String,
        var addOnParentName:String=""
    )
}
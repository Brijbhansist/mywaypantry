package com.fabwalley.food.webservices.requestbean
import com.google.gson.annotations.SerializedName


/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class AddToCartRequest(
    val requestContainer: RequestContainer? = RequestContainer(),
    val requestData: RequestData? = RequestData()
) {

    data class RequestContainer(
        @SerializedName("SecurityToken")
        val securityToken: String? = "",
        @SerializedName("CartToken")
        val cartToken: String? = "",
        @SerializedName("DeviceId")
        val deviceId: String? = "",
        @SerializedName("LatLong")
        val latLong: String? = "",
        val appVersion: String? = "",
        @SerializedName("RequestSource")
        val requestSource: String? = ""
    )

    data class RequestData(
        @SerializedName("OrderItem")
        val orderItem: OrderItem? = OrderItem()
    )

    data class OrderItem(
        @SerializedName("CartAutoId")
        val cartAutoId: String? = "",
        @SerializedName("CartItemAutoId")
        val cartItemAutoId: String? = "",
        @SerializedName("ReferenceNumber")
        val referenceNumber: String? = "",
        @SerializedName("ProductAutoId")
        val productAutoId: String? = "",
        @SerializedName("UnitAutoId")
        val unitAutoId: String? = "",
        @SerializedName("Qty")
        val qty: String? = "",
        @SerializedName("OrderAddOnItems")
        val orderAddOnItems: List<OrderAddOnItem>? = listOf(),
        @SerializedName("SpecialInstruction")
        val specialInstruction: String? = ""
    )

    data class OrderAddOnItem(
        @SerializedName("OptionAutoId")
        val optionAutoId: String? = "",
        @SerializedName("OptionCategoryAutoId")
        val optionCategoryAutoId: String? = "",
        val Price :String=""
    )
}
package com.fabwalley.food.webservices.requestbean
import com.google.gson.annotations.SerializedName


/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class GetCartDetails(
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
        val latLong: String? = ""
    )

    data class RequestData(
        @SerializedName("CartAutoId")
        val cartAutoId: Int? = 0
    )
}
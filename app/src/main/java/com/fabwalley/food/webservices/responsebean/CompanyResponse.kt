package com.fabwalley.food.webservices.responsebean

data class CompanyResponse(
    val d: D
) {

    data class D(
        val responseCode: String,
        val responseContainer: Any,
        val responseData: ArrayList<ResponseData>,
        val responseMessage: String
    )

    data class ResponseData(
        val CompanyName: String,
        val CompanyTagLine: String,
        val Logo: String,
        val Banner: String,
        val FooterName: String,
        val FooterNote: String,
        val StorePromotion: String,
        val PhoneNo1: String,
        val KitchenPrinterQR:String,
        val ShowKitchenPrinterQR:Int,
        val Website: String,
        val Address:  ArrayList<Address>,
        val PrinterIP: String,
        val PrinterPort: String
    )

    data class Address(
        val Address: String,
        val City: String,
        val State: String,
        val country: String,
        val ZipCode: String
    )
}


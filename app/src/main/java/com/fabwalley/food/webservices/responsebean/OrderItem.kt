package com.fabwalley.food.webservices.responsebean

import com.fabwalley.food.webservices.requestbean.PlaceOrderRequest
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created By Rahul Kansara (Rk) \n Email : rahul.kansara10@gmail.com on 07-Aug-20.
 */

data class OrderItem(
    var AddOnAmount: String,
    var Discount: String,
    var ItemTotal: String,
    var OrderAddOnItems: ArrayList<PlaceOrderRequest.OrderAddOnItem>,
    var Price: String,
    var ProductAutoId: String,
    var Qty: String,
    var SpecialInstruction: String,
    var UnitAutoId: String,
    var productName: String,
    var productDes: String,
    var productImg: String
): Serializable
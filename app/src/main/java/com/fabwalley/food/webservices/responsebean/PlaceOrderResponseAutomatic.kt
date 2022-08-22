package com.fabwalley.food.webservices.responsebean

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class PlaceOrderResponseAutomatic(
    val d: D
) {

    data class D(
        val responseCode: String,
        val responseData: ArrayList<ResponseData>,
        val responseMessage: String
    )

    data class ResponseData(
        val OrderList: ArrayList<OrderList>
    )
    data class OrderList(
        val OrderDate: String,
        val OrderTime: String,
        val OrderId: String,
        val CustomerName: String,
        val Email: String,
        val MobileNo: String,
        val ItemDetail:ArrayList<ItemDetail>,
        val SubTotal: Double,
        val TaxPer: Double,
        val TaxAmount: Double,
        val GrandTotal: Double,
        val OrderType:String
    )
    data class ItemDetail(
        val ProductName: String,
        val UnitName: String,
        val Qty: Int,
        val DiscountPer: Double,
        val ProductTotal:Double,
        val Note: String,
        val AddOnCategoryDetail:ArrayList<AddOnCategoryDetail>
    )
    data class AddOnCategoryDetail(
        val OptionCategory: String,
        val AddOnDetail:ArrayList<AddOnDetail>
    )

    data class AddOnDetail(
        val AddonName: String,
        val Price: Double
    )
}
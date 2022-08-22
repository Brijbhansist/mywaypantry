package com.fabwalley.food.webservices.responsebean

/**
 * Created By Rahul Kansara (Rk) \n Email : rahul.kansara10@gmail.com on 05-Aug-20.
 */

data class PopularProduct(
    val Image: String,
    val ProductAutoId: Int,
    val ProductName: String,
    val Product_Detail: String,
    val Thumbimg: String,
    val UnitPrice: Double
)
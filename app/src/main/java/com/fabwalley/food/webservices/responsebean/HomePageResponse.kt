package com.fabwalley.food.webservices.responsebean

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class HomePageResponse(
    val d: D
) {

    data class D(
        val responseCode: String,
        val responseContainer: Any,
        val responseData: List<ResponseData>,
        val responseMessage: String
    )

    data class ResponseData(
    val CategoryList: ArrayList<Category>,
    val PopularProduct: ArrayList<PopularProduct>
    )

    data class Category(
        val AutoId: Int,
        val CategoryName: String,
        val Thumbimg: String
    )


}
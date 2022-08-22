package com.fabwalley.food.webservices.responsebean

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class ProductDetailsResponse(
    val d: D,
    val responseData: List<ResponseData>
    )

 {

     data class ResponseData(
        val productAutoId: Int,
        val productDetail: String,
        val productImage100px: String,
        val productImage400px: String,
        val productName: String,
        val productUnit: List<ProductUnit>,
        val shortDescription: String
    )

    data class ProductUnit(
        val category: List<Category>,
        val isDefault: Int,
        val price: Double,
        val unitAutoId: Int,
        val unitName: String
    )

    data class Category(
        val categoryAutoId: Int,
        val categoryOption: List<CategoryOption>,
        val optionCategory: String,
        val optionIsMultiple: Int,
        val optionIsRequired: Int
    )

    data class CategoryOption(
        val categoryOptionAutoId: Int,
        val categoryOptionName: String,
        val price: Double
    )
}
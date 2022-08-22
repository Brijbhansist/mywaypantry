package com.fabwalley.food.webservices.responsebean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
data class CartDetailResponse(
    val d: D? = D()
) : Serializable{

    data class D(
        val responseMessage: String? = "",
        val responseCode: String? = "",
        val responseContainer: Any? = Any(),
        val responseData: ArrayList<ResponseData>? = arrayListOf()
    ): Serializable

    data class ResponseData(
        @SerializedName("ProductAutoId")
        val productAutoId: Int? = 0,
        @SerializedName("CartItemAutoId")
        val cartItemAutoId: Int? = 0,
        @SerializedName("ProductName")
        val productName: String? = "",
        @SerializedName("Product_Detail")
        val productDetail: String? = "",
        @SerializedName("SpecialInstruction")
        val specialInstruction: String? = "",
        @SerializedName("UnitAutoId")
        val unitAutoId: Int? = 0,
        @SerializedName("UnitName")
        val unitName: String? = "",
        @SerializedName("TaxPer")
        val TaxPer: Double? = 0.0,
        @SerializedName("Qty")
        val qty: Int? = 0,
        @SerializedName("UnitPrice")
        val unitPrice: Double? = 0.0,
        @SerializedName("Discount")
        val discount: Double? = 0.0,
        @SerializedName("ItemTotal")
        val itemTotal: Double? = 0.0,
        @SerializedName("ProductImage100px")
        val productImage100px: String? = "",
        @SerializedName("ProductImage400px")
        val productImage400px: String? = "",
        @SerializedName("CategoryOptionList")
        val categoryOptionList: List<CategoryOption>? = listOf()
    ): Serializable

    data class CategoryOption(
        @SerializedName("CatgoryAutoId")
        val catgoryAutoId: Int? = 0,
        @SerializedName("OptionCategory")
        val optionCategory: String? = "",
        @SerializedName("OptionList")
        val optionList: List<Option>? = listOf()
    ): Serializable

    data class Option(
        @SerializedName("CategoryOptionAutoId") val categoryOptionAutoId: Int? = 0,
        @SerializedName("CategoryOptionName") val categoryOptionName: String? = "",
        @SerializedName("OptionPrice")
        val optionPrice: Double? = 0.0
    ): Serializable

}
package com.fabwalley.food.webservices

import com.fabwalley.food.activity.ProductDetailActivity
import com.fabwalley.food.webservices.requestbean.*
import com.fabwalley.food.webservices.responsebean.*
import retrofit2.http.*


interface ApiService {

    @POST("APILogin.asmx/CompanyDetail")
    suspend fun getCompanyDetail(
        @Body() category: CompanyDetail
    ): CompanyResponse

    @POST("APIOrder.asmx/viewCategory")
    suspend fun getCate(
        @Body() category: CateRequest
    ): CateResonse

    @POST("APIOrder.asmx/SearchProduct")
    suspend fun searchPro(
        @Body() category: SearchRequest
    ): SearchResponse

    @POST("APIOrder.asmx/ProductDetail")
    suspend fun proDetail(
        @Body() category: ProdDetailRequest
    ): ProDetailResponse

    @POST("APIOrder.asmx/viewHomePage")
    suspend fun getHomePage(
        @Body() category: CateRequest
    ): HomePageResponse

    @POST("APIOrder.asmx/ProductDetail")
    suspend fun getProductDetail(
        @Body() category: CateRequest
    ): HomePageResponse

    @POST("APISubmitOrder.asmx/SubmitOrderByCartId")
    suspend fun placeOrder(
        @Body() placeOrderRequest: PlaceOrderRequest
    ): PlaceOrderResponse

    @POST("APIAddCartItem.asmx/AddCartItem")
    suspend fun addToCart(
        @Body() placeOrderRequest: AddToCartRequest
    ): CartResponse


    @POST("APIAddCartItem.asmx/CartDetails")
    suspend fun cartDetails(
        @Body() placeOrderRequest: GetCartDetails
    ): CartDetailResponse

    @POST("APIAddCartItem.asmx/DeleteCartItem")
    suspend fun deleteCart(
        @Body() placeOrderRequest: DeleteRequest
    ): CartDetailResponse


    @POST("APIAddCartItem.asmx/UpdateCartItem")
    suspend fun updateCart(
        @Body() placeOrderRequest: UpdateCartRequest
    ): CartResponse




}

enum class ReviewableType {
    B, // Barbershop
    H, //Hairstylist
    O, // Ownshop
}
package com.fabwalley.food.webservices.requestbean

data class DeleteRequest(
    val requestContainer: RequestContainer,
    val requestData: RequestData
){


data class RequestContainer(
    val CartToken: String,
    val DeviceId: String,
    val LatLong: String,
    val RequestSource: String,
    val SecurityToken: String,
    val appVersion: String
)

data class RequestData(
    val OrderItem: OrderItem
)

data class OrderItem(
    val CartAutoId: String,
    val CartItemAutoId: String
)}
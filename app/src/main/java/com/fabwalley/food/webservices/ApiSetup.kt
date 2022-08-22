package com.fabwalley.food.webservices

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val error: String? = null): ResultWrapper<Nothing>()
    data class NetworkError(val error: String?): ResultWrapper<Nothing>()
    data class LoggedError(val error: String?) : ResultWrapper<Nothing>()

}

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            val result = apiCall.invoke()
            Log.e("RkApi", "api result  "+ Gson().toJson(result))

//            if(result.code != 1){
//                ResultWrapper.GenericError(result.message)
//            }else{
//                Log.e("Rk",Gson().toJson(result))
            ResultWrapper.Success(result)
//            }


        } catch (throwable: Throwable) {
            Log.e("RkApi", "api", throwable)
            ResultWrapper.NetworkError(throwable.message)
        }
    }
}


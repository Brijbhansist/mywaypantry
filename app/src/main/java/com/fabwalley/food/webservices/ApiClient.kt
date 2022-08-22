package com.fabwalley.food.webservices

import android.app.Application
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {

    private val readTimeOut: Long = 50
    private val connectionTimeOut: Long = 50
    //  val BASE_URL =" https://mypbelleville.com/api/" // Belleville Live  API 28/07/2022
      val BASE_URL="http://testmyp02.progultra.com/api/" // Belleville Test  API 28/07/2022
    // val BASE_URL="https://mypglendora.com/api/" // Glendora Live API 28/07/2022
    //val BASE_URL=" https://test.mypglendora.com/api/" // Glendora Test API 28/07/2022
    // val BASE_URL="https://pantry1foodmart.com/api/" // foodMart Live API 28/07/2022
    //val BASE_URL = "http://testmyp02.progultra.com/API/" // Not avilabile foodMart Test  API 28/07/2022
    //val BASE_URL="https://mywaypantry.com/api/" // Manalapan Live API 28/07/2022
    //val  BASE_URL=" http://testmyp01.progultra.com/api/" // Manalapan Test API 28/07/2022
      fun getClient(context: Application): ApiService {
        val okHttpClient = OkHttpClient.Builder().run {
            addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
//                    val token: String? = AppPreferences.token
                    val requestBuilder = chain.request().newBuilder()
//                    token?.let {
//                        requestBuilder.addHeader("Authorization", "Bearer $it")
//                    }
//                    requestBuilder.addHeader("Content-Type", "application/json")
                    return chain.proceed(requestBuilder.build())
                }
            })
            connectTimeout(connectionTimeOut, TimeUnit.SECONDS)
            readTimeout(readTimeOut, TimeUnit.SECONDS)
            build()
        }

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}
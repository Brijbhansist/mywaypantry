package com.fabwalley.food.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.fabwalley.food.webservices.responsebean.AddProductResponse
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import com.fabwalley.food.webservices.responsebean.CompanyResponse
import com.fabwalley.food.webservices.responsebean.PrefCart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.paperdb.BuildConfig
import io.paperdb.Paper
import java.lang.reflect.Type


object AppPreferences {
    const val dollor = "$"
    const val BASEURL = "http://3.7.224.122/talli/"
    private const val NAME = "tallicustomer123"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private const val IsLogin = "IsLoginPref"
    private const val JwtToken = "token"
    private const val addkey = "add"
    private const val prefKey = "prefCart"
    private const val UserData = "UserData"
    private const val AddProducts = "AddProducts"
    private const val ADD_CART_RESPONSE = "AddCartResponse"
    private const val ADD_ITEM_RESPONSE = "AddItemResponse"
    private const val CARTCOUNT = "cartCOunt"
    private const val SAVE_CART = "savecart"
    private const val SAVE_CARTAPI = "apisavecart"
    private const val COMPANY_DETAIL = "compdetail"
    private const val ORDER_TIME = "otime"

    var manufacturer = Build.MANUFACTURER
    var model = Build.MODEL
    var version = Build.VERSION.SDK_INT
    var versionRelease = Build.VERSION.RELEASE
    const val appVersion=com.fabwalley.food.BuildConfig.VERSION_NAME
    const val FcmToken = "PushToken"
     const val latlng = "123"
//     const val appVersion = "V3.0"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation:
                                                  (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)

        editor.apply()
    }

/*
   var SharedPreferences.clearValues
        get() = { }
        set(value) {
            edit {
                it.clear()
            }
        }
*/

    fun clearPref() {
        val editor = preferences.edit()
        editor.remove(IsLogin)
        editor.remove(JwtToken)
        editor.remove(UserData)
        editor.remove(addkey)
        editor.clear()
        editor.apply()
//        preferences.edit().clear().apply()
    }

    var isLogin: Boolean
        get() = preferences.getBoolean(IsLogin, false)
        set(value) = preferences.edit { it.putBoolean(IsLogin, value) }

    var token: String?
        get() = preferences.getString(JwtToken, "")
        set(value) = preferences.edit { it.putString(JwtToken, value) }
    var Latitude: String?
        get() = preferences.getString(Latitude, "")
        set(value) = preferences.edit { it.putString(Latitude, value) }

    var Longitude: String?
        get() = preferences.getString(Longitude, "")
        set(value) = preferences.edit { it.putString(Longitude, value) }

    var addProductResponse: AddProductResponse
        get() = Gson().fromJson(preferences.getString(AddProducts, ""), AddProductResponse::class.java)
        set(value) = preferences.edit {
            it.putString(AddProducts, Gson().toJson(value))
        }
//
    var prefCart: PrefCart?
        get() = Gson().fromJson(preferences.getString(prefKey, ""),PrefCart::class.java)
        set(value) = preferences.edit {
            it.putString(prefKey, Gson().toJson(value))
        }
    var saveCart: CartDetailResponse?
        get() = Gson().fromJson(preferences.getString(SAVE_CART, ""),CartDetailResponse::class.java)
        set(value) = preferences.edit { it.putString(SAVE_CART, Gson().toJson(value))
        }
    var type: Type = object : TypeToken<ArrayList<CartDetailResponse.ResponseData>?>() {}.type
    var saveCartFromApi: ArrayList<CartDetailResponse.ResponseData>?
        get() = Gson().fromJson(preferences.getString(SAVE_CARTAPI, ""), type)
        set(value) = preferences.edit {
            it.putString(SAVE_CARTAPI, Gson().toJson(value))
        }


    var pushToken: String?
        get() = preferences.getString(JwtToken, "")
        set(value) = preferences.edit {
            it.putString(JwtToken, value)
        }

    var cartAutoId :String?

    get() = preferences.getString(ADD_CART_RESPONSE,"0")
    set(value) {
        preferences.edit {
            it.putString(ADD_CART_RESPONSE,value)
        }
    }
    var cartItemAutoId :String? get() = preferences.getString(ADD_ITEM_RESPONSE,"0")
    set(value) {
        preferences.edit {
            it.putString(ADD_ITEM_RESPONSE,value)
        }
    }

    var cartCount :Int?
    get() = preferences.getInt(CARTCOUNT,0)
    set(value) {
        preferences.edit {
            it.putInt(CARTCOUNT, value!!)
        }
    }
    var compDetail: ArrayList<CompanyResponse.ResponseData>
        get() = Paper.book().read(COMPANY_DETAIL, ArrayList())
        set(value) {
            Paper.book().write(COMPANY_DETAIL, value)

        }

    var orderTime: String
        get() = Paper.book().read(ORDER_TIME, "")
        set(value) { Paper.book().write(ORDER_TIME, value)}

}
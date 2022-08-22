package com.fabwalley.food

import android.app.Application
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.fabwalley.food.utils.AppPreferences
import com.fabwalley.food.utils.SunmiPrintHelper
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.ApiClient
import com.fabwalley.food.webservices.requestbean.PlaceAutomaticOrderRequest
import com.fabwalley.food.webservices.responsebean.PlaceOrderResponse
import com.fabwalley.food.webservices.responsebean.PlaceOrderResponseAutomatic
import com.google.gson.Gson
import io.paperdb.Paper
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
class FoodApplication : Application() {
    var runnable:Runnable ?=null
    var manufacturer = Build.MANUFACTURER
    var model = Build.MODEL
    var version = Build.VERSION.SDK_INT
    var versionRelease = Build.VERSION.RELEASE
    var deviceId = "$manufacturer $model $version $versionRelease"
    override fun onCreate() {
        super.onCreate()
        Paper.init(this)

//        AppPreferences.init(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
//        if(AppPreferences.orderTime.isNullOrEmpty())
//        {
//          //  "TimeStamp":'2020-09-15 22:34:42'
//
//            val c: Calendar = Calendar.getInstance()
//            val date: Date = c.getTime() //current date and time in UTC
//
//            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            df.setTimeZone(TimeZone.getTimeZone("GMT-4")) //format in given timezone
//
//            val strDate: String = df.format(date)
//            AppPreferences.orderTime=strDate
//            Log.e("fnn","s"+strDate)
//        }
//
//        var handler= Handler()
//        runnable= Runnable {
////            Toast.makeText(this, "After every 20 secs call-- "+AppPreferences.orderTime, Toast.LENGTH_LONG).show();
//            orderNowApiCall()
//            handler.postDelayed(runnable, 30000);
//        }
//        handler.postDelayed(runnable, 30000)
    }
    private fun init() {
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this)
    }

    private fun orderNowApiCall() {

        var obj=JSONObject()
        obj.put("appVersion","v.59" + AppPreferences.appVersion)
        obj.put("CartToken","")
        obj.put("DeviceId",""+ deviceId)
        obj.put("LatLong",""+ AppPreferences.latlng)
        obj.put("RequestSource","kiosk")
        obj.put("SecurityToken","")
        var obj1=JSONObject()
        obj1.put("TimeStamp",AppPreferences.orderTime)

        var mainObj=JSONObject()
        mainObj.put("requestContainer",obj)
        mainObj.put("requestData",obj1)


        Log.e("print att to cart: ", "" + mainObj.toString())
        try {
            val queue = Volley.newRequestQueue(this)
            val url = ApiClient.BASE_URL+"APISubmitOrder.asmx/OrderPrintDetail"

            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, mainObj,
                { response ->
                    // textView.text = "Response: %s".format(response.toString())
                    try {
                        if (response != null) {
                            val gson = Gson()
                            val obj2: PlaceOrderResponseAutomatic = gson.fromJson(
                                response.toString(),
                                PlaceOrderResponseAutomatic::class.java
                            )
                            if(obj2.d.responseData!=null)
                            {
                                if(obj2.d.responseData.get(0).OrderList.size>0)
                                {
                                    kitchenPrinter(obj2.d)
                                    resetDateandTime()
                                    Log.e("found Printing now", response.toString())
                                }
                                Log.e("Api response found", response.toString())
                            }
                            Log.e("Api response", response.toString())
                        }
                    } catch (e: Exception) {

                    }
                },
                { error ->
                    // TODO: Handle error
                    Log.e("Api error", error.toString())
                }
            )
           // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun resetDateandTime()
    {
        val c: Calendar = Calendar.getInstance()
        val date: Date = c.time //current date and time in UTC

        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df.timeZone = TimeZone.getTimeZone("GMT-4") //format in given timezone

        val strDate: String = df.format(date)
        AppPreferences.orderTime=strDate
    }

    fun kitchenPrinter(data:PlaceOrderResponseAutomatic.D)
    {
        var ordrList=data.responseData.get(0).OrderList

        Thread {
            try {
                val printer = EscPosPrinter(TcpConnection(AppPreferences.compDetail.get(0).PrinterIP, AppPreferences.compDetail.get(0).PrinterPort.toInt()), 203, 72f, 40)
                var tt=""
                tt+="[C]Kitchen's Copy\n"


                if(AppPreferences.compDetail.size>0)
                {
                    if(AppPreferences.compDetail.get(0).CompanyName.isNullOrEmpty())
                    {

                    }
                    else
                    {
                        tt+="[C]<b>"+AppPreferences.compDetail.get(0).CompanyName+"</b>\n"
                    }


                }
                tt+="[C]<font size='big'>"+ordrList.get(0).OrderType+"</font>\n"
                tt+="[C]-----------------------------------------------\n"

//                ordrList.forEachIndexed { index, orderList ->

//                }
                try {
                    tt+="[C]<font size='medium'>Order Date:"+ordrList.get(0).OrderDate+"</font>\n"
                } catch (e: Exception) {
                }
                tt+="[C]<font size='medium'>Order No:"+ordrList.get(0).OrderId+"</font>\n"


                try {
                    tt+="[C]<font size='medium'>Customer Name:"+ordrList.get(0).CustomerName+"</font>\n"
                } catch (e: Exception) {
                }

                if(!ordrList.get(0).MobileNo.isNullOrEmpty())
                {
                    tt+="[C]Phone:"+ordrList.get(0).MobileNo+"\n"
                }
                if(!ordrList.get(0).Email.isNullOrEmpty())
                {
                    tt+="[C]Email:"+ordrList.get(0).Email+"\n"
                }
                tt+="[C]-----------------------------------------------\n"
                tt+="[L]Qty[L][L]Item Name[L][L][L][L][L][L][L]Total\n"
                tt+="[C]-----------------------------------------------\n"

                val list = ordrList.get(0).ItemDetail
                list.forEach {
                    Log.e("check ", "" + Gson().toJson(it))

                    tt+="[L]<b>"+it.Qty.toString()+"[L]"+it.ProductName.toString()+"[L][L][L][L][L][L][L]"+it.ProductTotal.getTwoDigitVlaue()+"</b>\n"
                    tt+="[L][L]- "+it.UnitName.toString()+"[L][L][L][L][L][L][L]"+""+"\n"

                    it.AddOnCategoryDetail.forEach {
                        tt+="[L][L]"+it.OptionCategory.toString()+"[L][L][L][L][L][L][L]"+""+"\n"
                        it.AddOnDetail.forEach {
                            var msgg=""
                            if (it.Price > 0)
                            {
                                msgg="+$" + it.Price.getTwoDigitVlaue()
                            }
                            else
                            {
                                msgg=""
                            }
                            tt+="[L][L]- "+it.AddonName.toString()+ msgg+"[L][L][L][L][L][L][L]\n"
                        }
                    }
                    if(!it.Note.isNullOrEmpty())
                    {
                        tt+="[L]"+"Special instructions"+"\n"
                        tt+="[L]- "+it.Note+"\n"
                    }
                }
                var totalPrice = 0.0
                var totaltax = 0.0
//                list!!.d!!.responseData!!.forEach {
                    totalPrice += ordrList.get(0).SubTotal.toDouble()
                    totaltax+= ordrList.get(0).TaxAmount.toDouble()
//                }

                tt+="[C]-----------------------------------------------\n"
                tt+="[L][L][L]SubTotal:[L]"+ "$" + totalPrice.getTwoDigitVlaue()+"\n"
                tt+="[L][L][L]Tax:     [L]"+ "$" + totaltax.getTwoDigitVlaue()+"\n"
                tt+="[L][L][L][L]--------------\n"

                tt+="[L]<b>[L][L]Grand Total:[L]"+ "$" + (totaltax + totalPrice).getTwoDigitVlaue()+"</b>\n"
                tt+="[C]-----------------------------------------------\n"
//                if(!AppPreferences.compDetail.get(0).FooterName.isNullOrEmpty())
//                {
//                    tt+="[C]<b>"+AppPreferences.compDetail.get(0).FooterName+"</b>\n"
//                }
//                if(!AppPreferences.compDetail.get(0).FooterNote.isNullOrEmpty())
//                {
//                    tt+="[C]"+AppPreferences.compDetail.get(0).FooterNote+"\n"
//                }
//                if(!AppPreferences.compDetail.get(0).StorePromotion.isNullOrEmpty())
//                {
//                    tt+="[C]"+AppPreferences.compDetail.get(0).StorePromotion+"\n"
//                }
                tt+="[C]"+""+"\n"
                tt+="[C]"+""+"\n"
                printer.printFormattedTextAndCut(tt)
                markPrinted(ordrList.get(0).OrderId)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

        }.start()
    }

    private fun markPrinted(ordNu:String) {

        var obj=JSONObject()
        obj.put("appVersion","v.59" + AppPreferences.appVersion)
        obj.put("CartToken","")
        obj.put("DeviceId",""+ deviceId)
        obj.put("LatLong",""+ AppPreferences.latlng)
        obj.put("RequestSource","kiosk")
        obj.put("SecurityToken","")

        var obj1=JSONObject()
        obj1.put("OrderId",ordNu)

        var mainObj=JSONObject()
        mainObj.put("requestContainer",obj)
        mainObj.put("requestData",obj1)


        Log.e("print att to cart: ", "" + mainObj.toString())
        try {
            val queue = Volley.newRequestQueue(this)
            val url = ApiClient.BASE_URL+"APISubmitOrder.asmx/ChangePrintStatus"

            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, mainObj,
                { response ->
                    // textView.text = "Response: %s".format(response.toString())
                    try {
                        if (response != null) {
                            val gson = Gson()
                            val obj2: PlaceOrderResponseAutomatic = gson.fromJson(
                                response.toString(),
                                PlaceOrderResponseAutomatic::class.java
                            )

                            Log.e("Api update", response.toString())
                        }
                    } catch (e: Exception) {

                    }
                },
                { error ->
                    // TODO: Handle error
                    Log.e("Api error", error.toString())
                }
            )
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
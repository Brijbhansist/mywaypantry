package com.fabwalley.food.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import com.fabwalley.food.R
import com.fabwalley.food.adapter.CateAdapter
import com.fabwalley.food.adapter.ProAdapter
import com.fabwalley.food.base.NavigationActivity
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.AppPreferences
import com.fabwalley.food.webservices.ApiClient
import com.fabwalley.food.webservices.requestbean.CateRequest
import com.fabwalley.food.webservices.requestbean.DeleteRequest
import com.fabwalley.food.webservices.requestbean.SearchRequest
import com.fabwalley.food.webservices.responsebean.CateResonse
import com.fabwalley.food.webservices.responsebean.SearchResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_product_detail.*


class MainActivity : NavigationActivity() {
    var manufacturer = Build.MANUFACTURER
    var model = Build.MODEL
    var version = Build.VERSION.SDK_INT
    var versionRelease = Build.VERSION.RELEASE
    lateinit var cateadapter: CateAdapter
    lateinit var proAdapter: ProAdapter
    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    override fun onResume() {
        super.onResume()
        if (ProductDetailActivity.isChange) {
            setCart()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE

                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_main)
        setCart()
        cateadapter = CateAdapter(this, object : OnItemClickListner {
            override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                val data = obj as CateResonse.ResponseData
//                apicallPro(data.autoId)
                apicallPro(data.AutoId)
            }
        })
        proAdapter = ProAdapter(this, object : OnItemClickListner {
            override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                val data = obj as SearchResponse.D.ResponseData
                ProductDetailActivity.id = data.ProductAutoId
                startActivity(Intent(this@MainActivity, ProductDetailActivity::class.java))
            }
        })
//        homePageApiCall()
        cateRv.adapter = cateadapter
        prodRv.adapter = proAdapter
        cart.setOnClickListener {
            if (!AppPreferences.cartAutoId.equals("0"))
                startActivity(Intent(this, CartActivity::class.java))
        }
        apicall()
        apicallPro(0)

        startOver.setOnClickListener {
            startActivity(Intent(this,SplashActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))

//            try {
//                if(AppPreferences.cartCount!!>0)
//                {
//                    val builder = AlertDialog.Builder(this@MainActivity,R.style.MyDialog)
//
//                    // Set the alert dialog title
//                    builder.setTitle("Confirmation?")
//
//                    // Display a message on alert dialog
//                    builder.setMessage(HtmlCompat.fromHtml("<big><big>Are you sure to start over? This will remove all items from your cart.</big></big>", 0))
//
//                    // Set a positive button and its click listener on alert dialog
//                    builder.setPositiveButton("YES"){dialog, which ->
//                        // Do something when user press the positive button
//                        // Change the app background color
//                        AppPreferences.cartAutoId = "0"
//                        AppPreferences.cartItemAutoId = "0"
//                        AppPreferences.cartCount = 0
//                        setCart()
//                        dialog.dismiss()
//                    }
//
//
//                    // Display a negative button on alert dialog
//                    builder.setNegativeButton("No"){dialog,which ->
//                        dialog.dismiss()
//                    }
//
//                    // Finally, make the alert dialog using builder
//                    val dialog: AlertDialog = builder.create()
//
//
//                    // Display the alert dialog on app interface
//                    dialog.show()
//                }
//            } catch (e: Exception) {
//            }

        }

        if(AppPreferences.compDetail.size>0)
            titlePmain.text = HtmlCompat.fromHtml(AppPreferences.compDetail[0].CompanyName+" "+AppPreferences.compDetail.get(0).Address.get(0).City,0)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun setCart() {
        cartCount.text = AppPreferences.cartCount.toString()
        ProductDetailActivity.isChange = false
//        if(AppPreferences.cartCount!!>0)
//        {
            startOver.visibility=View.VISIBLE
//        }
//        else
//        {
//            startOver.visibility=View.INVISIBLE
//        }
    }


    private fun apicall() {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val datarequest = CateRequest.RequestContainer("", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""), "")
        val request = CateRequest(datarequest)
        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).getCate(request)
        }, handleSuccess = {
            var dataList = arrayListOf<CateResonse.ResponseData>()
            var data = CateResonse.ResponseData(
                0,
                "View Full Menu",
                "https://mywaypantry.com/images/food.png"
            )
            dataList.add(data)
            dataList.addAll(it.d.responseData)

            cateadapter.addData(dataList)
        }, handleGenric = {
            makeToast(it)
        }
        )
    }

    private fun homePageApiCall() {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val datarequest = CateRequest.RequestContainer("", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""), "")
        val request = CateRequest(datarequest)
        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).getHomePage(request)
        }, handleSuccess = {
//            cateadapter.addData(it.d.responseData[0].CategoryList)
//            proAdapter.addData(it.d.responseData[0].PopularProduct)
            proHome.visibility = View.GONE
        }, showDialg = true
        )
    }

    private fun apicallPro(id: Int) {
        proAdapter.clear()
        proHome.visibility = View.VISIBLE
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val datarequest = SearchRequest.RequestContainer("", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""), "")
        val dataCa = SearchRequest.RequestData(id, "")
        val request = SearchRequest(datarequest, dataCa)
        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).searchPro(request)
        }, handleSuccess = {
            proAdapter.addData(it.d.responseData)
            proHome.visibility = View.GONE
        }, handleGenric = {
            proHome.visibility = View.GONE
            makeToast(it)
        }, showDialg = false
        )

    }

    override fun onDestroy() {
        CateAdapter.selected = 0
        super.onDestroy()
    }
}
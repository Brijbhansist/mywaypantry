package com.fabwalley.food.activity

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fabwalley.food.R
import com.fabwalley.food.base.BaseActivity
import com.fabwalley.food.utils.AppPreferences
import com.fabwalley.food.utils.escapeMetaCharacters
import com.fabwalley.food.webservices.ApiClient
import com.fabwalley.food.webservices.requestbean.CateRequest
import com.fabwalley.food.webservices.requestbean.CompanyDetail
import com.fabwalley.food.webservices.responsebean.CateResonse
import com.fabwalley.food.webservices.responsebean.CompanyResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_splash.*
import android.content.pm.PackageInfo
import com.google.android.gms.location.LocationSettingsStatusCodes

import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences

import com.google.android.gms.location.LocationSettingsStates

import com.google.android.gms.location.LocationSettingsResult

import com.google.android.gms.location.LocationSettingsRequest

import com.google.android.gms.location.LocationRequest

import com.google.android.gms.common.api.GoogleApiClient
import android.location.LocationManager
import com.google.android.gms.tasks.OnSuccessListener


class SplashActivity : BaseActivity() {
    var manufacturer = Build.MANUFACTURER
    var model = Build.MODEL
    var version = Build.VERSION.SDK_INT
    var versionRelease = Build.VERSION.RELEASE
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var locationManager: LocationManager? = null
    var GpsStatus = false
    var intent1: Intent? = null
    private var client: FusedLocationProviderClient? = null
     var lat: String? =null
     var long: String? =null



    private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                 View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        setContentView(com.fabwalley.food.R.layout.activity_splash)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setFinishOnTouchOutside(true)
        requestPermission()
        client = LocationServices.getFusedLocationProviderClient(this)
        checkGpsStatus()
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val first = "<font color='#FFFFFFFF'>My Way Pantry 1 </font>"
        val next = "<font color='#E80B0B'>Deli &amp; Grill</font>"

        val textView = findViewById<TextView>(R.id.txtAppVersion)
        textView.setText("App Version : V"+AppPreferences.appVersion).toString()
        val textViewValue = textView.text
//        mainTxt.setText(HtmlCompat.fromHtml(first + next,0))
//        val handler = Handler();
//        handler.postDelayed(Runnable {
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
//        },1000)
        placeOrder.setOnClickListener {
            AppPreferences.cartAutoId = "0"
            AppPreferences.cartItemAutoId = "0"
            AppPreferences.cartCount = 0
            startActivity(Intent(this,MainActivity::class.java))
        }
        setData()
        Handler().postDelayed(Runnable {
            apicall()
        },1000)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }
    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun apicall() {

        val datarequest = CompanyDetail.RequestContainer("", ""+ AppPreferences.appVersion,
            "", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),""+AppPreferences.latlng,"")

        val request = CompanyDetail(datarequest)

        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).getCompanyDetail(request)
        }, handleSuccess = {

            var dataList = arrayListOf<CompanyResponse.ResponseData>()
            dataList= it.d.responseData
            if(dataList.size>0)
        AppPreferences.compDetail=dataList
            setData()
        }, handleGenric = {
            makeToast(it)
        }
        )
    }


    fun setData()
    {
        if(AppPreferences.compDetail.size>0)
        {
            try {
                mainTxt.text = HtmlCompat.fromHtml(AppPreferences.compDetail[0].CompanyName,0)
            } catch (e: Exception) {
            }
            try {
                txtLocation.text = HtmlCompat.fromHtml(
                    AppPreferences.compDetail.get(0).Address.get(
                        0
                    ).City,0)
            } catch (e: Exception) {
            }
            try {
                orderTv.text = HtmlCompat.fromHtml(AppPreferences.compDetail[0].CompanyTagLine,0)
            } catch (e: Exception) {
            }


            try {
                Glide.with(this@SplashActivity).load(AppPreferences.compDetail[0].Logo).listener(object :
                    RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).placeholder(R.drawable.logo)
                    .into(logo)
            } catch (e: Exception) {
            }


            try {
                Glide.with(this@SplashActivity).load(AppPreferences.compDetail[0].Banner).listener(object :
                    RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).placeholder(R.drawable.banner_bg)
                    .into(background)
            } catch (e: Exception) {
            }
        }
    }

    private fun checkGpsStatus() {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        GpsStatus  = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (GpsStatus ) {
            if (ActivityCompat.checkSelfPermission(
                    this@SplashActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            client!!.lastLocation.addOnSuccessListener(
                this@SplashActivity, object : OnSuccessListener<Location?> {
                    override fun onSuccess(location: Location?) {
                        val sharedPreference =  getSharedPreferences("Location",Context.MODE_PRIVATE)
                        var editor = sharedPreference.edit()
                        if (location != null) {
                            editor.putString("Latitude",location.latitude.toString())
                            editor.putString("Longitude",location.longitude.toString())
                        }else{
                            editor.putString("Latitude","")
                            editor.putString("Longitude","")
                        }
                        editor.commit()
                    }
                })

        } else {
            intent1 =  Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent1)
        }
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

}
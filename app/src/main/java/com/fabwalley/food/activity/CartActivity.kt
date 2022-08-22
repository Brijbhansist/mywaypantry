package com.fabwalley.food.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabwalley.food.R
import com.fabwalley.food.adapter.CartAdapter
import com.fabwalley.food.base.BaseActivity
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.AppPreferences
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.ApiClient
import com.fabwalley.food.webservices.requestbean.DeleteRequest
import com.fabwalley.food.webservices.requestbean.GetCartDetails
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cart.*
import kotlin.collections.ArrayList

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */

class CartActivity : BaseActivity() {
    var list: ArrayList<CartDetailResponse.ResponseData> = arrayListOf()
    var adapter: CartAdapter? = null
    var cartDetailResponse: CartDetailResponse? = null
    var manufacturer = Build.MANUFACTURER
    var model = Build.MODEL
    var version = Build.VERSION.SDK_INT
    var versionRelease = Build.VERSION.RELEASE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        setContentView(R.layout.activity_cart)

        adapter = CartAdapter(true, object : OnItemClickListner {
            override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                if (task == 1) {
                    val data = obj as CartDetailResponse.ResponseData
                    val view: View = LayoutInflater.from(this@CartActivity)
                        .inflate(R.layout.report_problem_dialog, null)
                    val alertDialog =
                        android.app.AlertDialog.Builder(this@CartActivity).create()
//                    alertDialog.setTitle("Please login")
                    alertDialog.setCancelable(false)
                    // alertDialog.setMessage("Please Enter your Mobile Number");

                    val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
                    msg.text = "Are you sure to Delete this Item?"
                    val login =
                        view.findViewById<View>(R.id.ios_logout) as AppCompatTextView
                    login.setOnClickListener {
                        alertDialog.dismiss()
                        deleteApicall(data, position)
                    }
                    val cancel =
                        view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
                    cancel.setOnClickListener { alertDialog.dismiss() }
                    alertDialog.setView(view)
                    alertDialog.show()
                } else if (task == 2) {
                    val data = obj as CartDetailResponse.ResponseData
                    startActivity(
                        Intent(this@CartActivity, EditCartActivity::class.java)
                            .putExtra("cartedit", data)
                    )
                }
            }
        })
        rvCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCart.adapter = adapter

        addToCart.setOnClickListener {
            AppPreferences.saveCart = cartDetailResponse
            if(AppPreferences.cartCount!! > 0){
                startActivity(
                    Intent(this, ConfirmDetailsActivity::class.java)
                        .putExtra("cartdetails", cartDetailResponse)
                )
            }
            else
            {
                val view: View = LayoutInflater.from(this@CartActivity)
                    .inflate(R.layout.report_problem_dialog_single_btn, null)
                val alertDialog =
                    android.app.AlertDialog.Builder(this@CartActivity).create()
                alertDialog.setCancelable(false)
                val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
                msg.text = "You don't have any item in cart"
                val cancel =
                    view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
                cancel.setOnClickListener { alertDialog.dismiss() }
                alertDialog.setView(view)
                alertDialog.show()
            }
        }
        addmore.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // start your next activity
            startActivity(intent)
            // onBackPressed()
        }
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
    private fun deleteApicall(
        data: CartDetailResponse.ResponseData,
        position: Any
    ) {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val requestContainer = DeleteRequest.RequestContainer("",  Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""), "Kiosk","",""+AppPreferences.appVersion)
        val requestData = DeleteRequest.OrderItem(AppPreferences.cartAutoId!!,
            data.cartItemAutoId.toString()
        )
        val orderItem = DeleteRequest.RequestData(requestData)
        val request = DeleteRequest(requestContainer, orderItem)
        Log.e("DELETE","delete request  "+Gson().toJson(request))
        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).deleteCart(request)
        }, handleSuccess = {
            makeToast(it.d!!.responseMessage)
            list.removeAt(position as Int)
            ProductDetailActivity.isChange = true
            adapter!!.addData(list)
            AppPreferences.cartCount = list.size
            setTotal()
            showPopUp()
        }, handleGenric = {
            makeToast(it)
        })
    }
    fun showPopUp()
    {
        val view: View = LayoutInflater.from(this@CartActivity)
            .inflate(R.layout.report_problem_dialog_single_btn, null)
        val alertDialog =
            android.app.AlertDialog.Builder(this@CartActivity).create()
        alertDialog.setCancelable(false)
        val alert_img=view.findViewById<View>(R.id.alert_img) as ImageView
        alert_img.setImageResource(R.drawable.ic_check_circle_outline_black_48dp)
        val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
        msg.text = "1 Item deleted from the cart"
        val cancel =
            view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
        cancel.setOnClickListener { alertDialog.dismiss() }
        alertDialog.setView(view)
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        getCartDetailsApiCall()
    }
    fun setTotal() {
        var totalPrice = 0.0
        var totaltax = 0.0
        list.forEach {
            totalPrice += it.itemTotal!!.toDouble()
            totaltax+=((it.itemTotal*it.TaxPer!!)/100)
        }
        tvSubTotalAmttax.text = "$" + totaltax.getTwoDigitVlaue()
        tvSubTotalAmt.text = "$${(totaltax+totalPrice).getTwoDigitVlaue()}"
    }
    fun getCartDetailsApiCall() {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        AppPreferences.saveCartFromApi= ArrayList<CartDetailResponse.ResponseData>()
        val requestContainer = GetCartDetails.RequestContainer("", "",  Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""))
        val requestData = GetCartDetails.RequestData(AppPreferences.cartAutoId!!.toInt())
        val request = GetCartDetails(requestContainer, requestData)

        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).cartDetails(request)
        }, handleSuccess = {

            val gson = Gson()
            gson.toJson(it)
            Log.e("Cart Details Response",""+gson)
            cartDetailResponse = it
            try {
                adapter!!.addData(it.d!!.responseData!!)
                list = it.d.responseData!!
                AppPreferences.saveCartFromApi= it.d.responseData!!
            } catch (e: Exception) {
            }
            setTotal()
        }, handleGenric = {
            makeToast(it)
        })
    }
}
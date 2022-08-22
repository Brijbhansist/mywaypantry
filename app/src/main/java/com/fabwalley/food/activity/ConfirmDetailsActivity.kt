package com.fabwalley.food.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import com.developer.kalert.KAlertDialog
import com.fabwalley.food.R
import com.fabwalley.food.base.BaseActivity
import com.fabwalley.food.utils.AppPreferences
import com.fabwalley.food.webservices.ApiClient
import com.fabwalley.food.webservices.requestbean.DeleteRequest
import com.fabwalley.food.webservices.requestbean.PlaceOrderRequest
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import com.fabwalley.food.webservices.responsebean.OrderItem
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_confirm_details.*
import kotlinx.android.synthetic.main.activity_confirm_details.close
import kotlinx.android.synthetic.main.activity_confirm_details.titleP
import kotlinx.android.synthetic.main.activity_product_detail.*


/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
class ConfirmDetailsActivity : BaseActivity() {
    private var selectedProductList = ArrayList<OrderItem>()
    private var cartDetailsRespone :CartDetailResponse?=null

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
        setContentView(R.layout.activity_confirm_details)
        close.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        lbl.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        selectedProductList = AppPreferences.prefCart!!.orderItem
//        selectedProductList = intent.getSerializableExtra("OrderDetails") as ArrayList<OrderItem>

        cartDetailsRespone = intent.getSerializableExtra("cartdetails") as CartDetailResponse?
        edtOrderNow.setOnClickListener {
            if (edtFirstName.text.toString().trim().isEmpty()){
                showPopUp("Please Enter First Name")
//                Snackbar.make(edtFirstName,"Please Enter First Name",Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            else if (edtLastName.text.toString().trim().isEmpty()){
//                Snackbar.make(edtLastName,"Please Enter Last Name",Snackbar.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            else if (edtEmailId.text.toString().trim().isEmpty()){
//                showPopUp("Please Enter Email Id")
//                Snackbar.make(edtEmailId,"Please Enter Email Id",Snackbar.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }else if (!isValidEmail(edtEmailId.text.toString().trim())){
//                showPopUp("Please Enter valid Email Id")
//                Snackbar.make(edtEmailId,"Please Enter valid Email Id",Snackbar.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            else if (edtPhone.text.toString().trim().isEmpty()){
//                showPopUp("Please Enter Phone Number")
//                Snackbar.make(edtFirstName,"Please Enter Phone Number",Snackbar.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }else if (edtPhone.text.toString().trim().length!=10){
//                showPopUp("Please Enter valid Phone Number")
//                Snackbar.make(edtFirstName,"Please Enter valid Phone Number",Snackbar.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
            else{
               var checkEM = if (cbNoEm.isChecked){
                    "1"
                }else{
                    "0"
                }
                var checkSMS = if (cbNotiMo.isChecked){
                    "1"
                }else{
                    "0"
                }
                var orderItem =ArrayList<OrderItem>()
                var addOnAmount =0.0
                var orderAddOnItems: ArrayList<PlaceOrderRequest.OrderAddOnItem> = arrayListOf()
                cartDetailsRespone!!.d!!.responseData!!.forEach {

                    it.categoryOptionList!!.forEach {
                        it.optionList!!.forEach {its->
                            addOnAmount+= its.optionPrice!!
                            orderAddOnItems.add(PlaceOrderRequest.OrderAddOnItem(
                                OptionAutoId = its.categoryOptionAutoId.toString(),
                                Price = its.optionPrice.toString(),
                                addOnName = its.categoryOptionName!!,
                                addOnParentName = it.optionCategory!!
                            ))
                        }
                    }
                    orderItem.add(OrderItem(
                        AddOnAmount = addOnAmount.toString(),
                        Discount=it.discount.toString(),
                        ItemTotal = it.itemTotal.toString(),
                        OrderAddOnItems = orderAddOnItems,
                        Price = it.unitPrice.toString(),
                        ProductAutoId = it.productAutoId.toString(),
                        Qty = it.qty.toString(),
                        SpecialInstruction = "",
                        UnitAutoId = it.unitAutoId.toString(),
                        productName = it.productName!!,
                        productDes = it.productDetail!!,
                        productImg = it.productImage100px!!
                    ))
                }
                var autoID=""
                if(cartDetailsRespone!!.d!!.responseData!=null)
                {
                    if(cartDetailsRespone!!.d!!.responseData!!.size>0)
                    {
                        autoID = cartDetailsRespone!!.d!!.responseData?.get(0)?.cartItemAutoId.toString()
                    }
                }
                val orderInfo= PlaceOrderRequest.OrderInfo(
                    CoupanCode = "",
                    DeliveryType = "PickUp",
                    Email = edtEmailId.text.toString().trim(),
                    FirstName = edtFirstName.text.toString().trim(),
                    LastName = edtLastName.text.toString().trim(),
                    NotifyEmail = checkEM,
                    NotifySMS = checkSMS,
                    OrderItem = orderItem,
                    PaymentType = "PayAtStore",
                    Phoneno = edtPhone.text.toString().trim(),
                    ReferenceNumber = "",
                    CartAutoId = AppPreferences.cartAutoId.toString()
                )

                orderNowApiCall(orderInfo)
            }
        }
        if(AppPreferences.compDetail.size>0)
            titleP.text = HtmlCompat.fromHtml(AppPreferences.compDetail[0].CompanyName+" "+AppPreferences.compDetail.get(0).Address.get(0).City,0)
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
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


    fun showPopUp(msggg:String)
    {
//       var dialog= KAlertDialog(this@ConfirmDetailsActivity, KAlertDialog.WARNING_TYPE)
//            var title= dialog.findViewById(com.cazaea.sweetalert.R.id.title_text) as TextView
//              title.visibility=View.GONE

//            dialog
//            .setContentText("<big><big>${msg}</big></big>")
//            .setConfirmText("OK") // Do not set the property, do not show the button
//            .show();

        val view: View = LayoutInflater.from(this@ConfirmDetailsActivity)
            .inflate(R.layout.report_problem_dialog_single_btn, null)
        val alertDialog =
            android.app.AlertDialog.Builder(this@ConfirmDetailsActivity).create()
        alertDialog.setCancelable(false)
        val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
        msg.text = msggg
        val cancel =
            view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
        cancel.setOnClickListener { alertDialog.dismiss() }
        alertDialog.setView(view)
        alertDialog.show()

//        dialog.setOnShowListener {
//            var title= dialog.findViewById(R.id.title_text) as TextView
//            title.setSingleLine(false);
//            title.maxLines=3
//            title.setLines(3)
//        }
    }

    private fun orderNowApiCall(orderInfo: PlaceOrderRequest.OrderInfo) {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val datarequest = PlaceOrderRequest.RequestContainer(AppPreferences.appVersion, "", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""),"kiosk","")
        val requestData = PlaceOrderRequest.RequestData(orderInfo)
        val request = PlaceOrderRequest(datarequest, requestData)
        val gson = Gson()
        val tt= gson.toJson(request)
        Log.e("print att to cart: ",""+tt)

        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).placeOrder(request)
        }, handleSuccess = {
            try {
                startActivity(Intent(this,ThankYouActivityChanged::class.java).putExtra("orderid",it.d.responseData[0].OrderId).putExtra("print",it.d.responseData[0].PrintOrder).putExtra("custname",edtFirstName.text.toString().trim()+" "+edtLastName.text.toString().trim())
                    .putExtra("custphone",edtPhone.text.toString().trim()).putExtra("custemail",edtEmailId.text.toString().trim()).putExtra("ordrdate",it.d.responseData[0].OrderDate).
                    putExtra("ordrnote",it.d.responseData[0].OrderNote).putExtra("OrderTotal",(it.d.responseData[0].OrderTotal).toString()))
                finish()
                this.finish()
            } catch (e: Exception) {
            }
        }, handleGenric = {
            makeToast(it)
        }
        )
    }
}
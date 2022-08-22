package com.fabwalley.food.activity

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.developer.kalert.KAlertDialog
import com.fabwalley.food.R
import com.fabwalley.food.adapter.*
import com.fabwalley.food.base.BaseActivity
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.AppPreferences
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.ApiClient
import com.fabwalley.food.webservices.requestbean.AddToCartRequest
import com.fabwalley.food.webservices.requestbean.DeleteRequest
import com.fabwalley.food.webservices.requestbean.ProdDetailRequest
import com.fabwalley.food.webservices.requestbean.UpdateCartRequest
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import com.fabwalley.food.webservices.responsebean.PriceModel
import com.fabwalley.food.webservices.responsebean.ProDetailResponse
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.pro_img.*

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
class EditCartActivity : BaseActivity() {
    var data: CartDetailResponse.ResponseData? = null
    var sendPrice = 0.0
    private var productResponse: ProDetailResponse? = null
    lateinit var cateadapter: EditProductAdapter
    lateinit var mainCate: EditMainAdapter
    var sendData: ProDetailResponse.ProductUnit? = null

    var mainPrice = 0.0
    var includePrice = 0.0
    var qun = 1
     var pricemodel: ArrayList<PriceModel> ? = null
    var pricemodel1: ArrayList<PriceModel> ? = null
    val hashMap:HashMap<Int,PriceModel> = HashMap<Int,PriceModel>()

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
        setContentView(R.layout.activity_product_detail)
        pricemodel = ArrayList()
        pricemodel1 = ArrayList()
        data = intent.getSerializableExtra("cartedit") as CartDetailResponse.ResponseData?
        qun = data!!.qty!!
        mainPrice = data!!.unitPrice!!
        etInst.setText(data!!.specialInstruction.toString())
        decrement.setOnClickListener {
            if (qun > 1) {
                qun -= 1
                setPrice()
            }
        }
        close.setOnClickListener {
            onBackPressed()
        }
        increment.setOnClickListener {
            qun += 1
            setPrice()
        }
        etInst.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) mainscroll.smoothScrollTo(view.left, view.bottom)
        }

        cateadapter = EditProductAdapter(this,data!!.categoryOptionList, object : OnItemClickListner {
            override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                Log.e("Rk", "Get onItemClick Activity  task $task")
                var pos=0
                if (task == 4) {
//                    pricemodel1!!.clear()
                    cateadapter.getData().forEachIndexed { index, category ->
                        if(category.OptionCategory==obj2 as String)
                        {
                            pos=index
                        }
                    }
                    val data = obj as ProDetailResponse.CategoryOption
                    Log.e("Array", "Obj  " + obj2 as String)
//                    var test = pricemodel!!.any { it.parentName == obj2 as String }
//                    if (test) {
//                        pricemodel!!.forEachIndexed { index, priceModel ->
//                            if (priceModel.parentName.equals(obj2 as String)) {
////                                pricemodel!!.removeAt(index)
//                            }
//                            else
//                            {
//                                pricemodel1!!.add(priceModel)
//                            }
//                        }
//                        val d = PriceModel()
//                        d.parentName = obj2 as String
//                        d.price = data.Price
////                        pricemodel!!.add(d)
//                        pricemodel1!!.add(d)
//                    } else {
//                        val d = PriceModel()
//                        d.parentName = obj2 as String
//                        d.price = data.Price
////                        pricemodel!!.add(d)
//                        pricemodel1!!.add(d)
//                    }
//                    pricemodel!!.clear()
//                    pricemodel!!.addAll(pricemodel1!!)
                    val d = PriceModel()
                    d.parentName = obj2
                    d.price = data.Price

                    hashMap.put(pos,d)
                    addPrice()
                    setPrice()
                }
                if (task == 3) {
                    Log.e("Listner","task 3")
                    Log.e("Listner","obje   "+   obj2 as Boolean)
                    val data = obj as ProDetailResponse.CategoryOption
                    Log.e("Listner","data   "+   data.Price)
                    if (obj2 == true) {
                        includePrice += data.Price
                        setPrice()
                    } else {
                        includePrice -= data.Price
                        setPrice()
                    }
                }
            }
        })
        rvPro.adapter = cateadapter

        mainCate = EditMainAdapter(data!!,object : OnItemClickListner {
            override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                val data = obj as ProDetailResponse.ProductUnit
                // start by jsw
                pricemodel!!.clear()
                hashMap.clear()
                includePrice = 0.0
                sendData = data
                mainPrice = data.Price
                // end by jsw
                cateadapter.addToData(data.Category)
//                sendData = data
//                mainPrice = data.Price
                setPrice()
                Handler().postDelayed(
                    Runnable {
                        EditProductAdapter.firstTimeOnly=1
                        setPrice()
                    },1000
                )


//                val data = obj as ProDetailResponse.ProductUnit
//                pricemodel.clear()
//                includePrice = 0.0
//                sendData = data
//                mainPrice = data.Price
//                cateadapter.addToData(data.Category)
//                setPrice()
            }
        })
        rvMain.adapter = mainCate
        apicall()

        addToCart.setOnClickListener {
            var requiredSize = cateadapter.dataList.filter {
                if(it.OptionIsRequired.toString().equals("1") && it.OptionIsMultiple.toString().equals("1")){
                    if(cateadapter.hashSet.size>=1){
                        it.OptionIsRequired.toString().equals("0")
                    }
                    else{
                        it.OptionIsRequired.toString().equals("1")
                    }
                }else{
                    it.OptionIsRequired.toString().equals("1")
                }
            }
            if(pricemodel ==null)
                pricemodel =ArrayList()

            if (pricemodel!!.size >= requiredSize.size) {
                val orderItem = UpdateCartRequest.OrderItem(
                    cartAutoId = AppPreferences.cartAutoId,
                    cartItemAutoId = AppPreferences.cartItemAutoId,
                    unitAutoId = sendData!!.UnitAutoId.toString(),
                    qty = qun.toString() ,
                    orderAddOnItems = cateadapter.selectedItemList,
                    specialInstruction = ""
                )
//                unitAutoId = productResponse!!.d.responseData[0].ProductUnit[0].UnitAutoId.toString(),

                updateCart(orderItem)
            } else {
//                Snackbar.make(
//                    addToCart,
//                    "Please Select all required categories",
//                    Snackbar.LENGTH_SHORT
//                ).show()
                var errorMsg=""
                var contain=0
                for(index in 0 until requiredSize.size)
                {
                    contain=0
                    for(index1 in 0 until pricemodel!!.size)
                    {
                        if(requiredSize.get(index).OptionCategory.equals(pricemodel!!.get(index1).parentName,true))
                        {
                            contain=1
                            break
                        }
                        else
                        {
                            contain=0
                        }
                    }
                    if(contain==0)
                    {
                        errorMsg=requiredSize.get(index).OptionCategory
                        break
                    }
                }

//                requiredSize.forEach {
//                        it ->
//                    contain=0
//                    pricemodel!!.forEach {
//                            it1->
//                        if(it.OptionCategory.equals(it1.parentName,true))
//                        {
//                            contain=1
//                        }
//                        else
//                        {
//                            contain=0
//                        }
//                    }
//                    if(contain==0)
//                    {
//                        errorMsg=it.OptionCategory
//                        return@forEach
//                    }
//                }

                val view: View = LayoutInflater.from(this@EditCartActivity)
                    .inflate(R.layout.report_problem_dialog_single_btn, null)
                val alertDialog =
                    android.app.AlertDialog.Builder(this@EditCartActivity).create()
                alertDialog.setCancelable(false)
                val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
                msg.text = "Please select min 1 modifier from ${errorMsg.toUpperCase()} Group."
                val cancel =
                    view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
                cancel.setOnClickListener { alertDialog.dismiss() }
                alertDialog.setView(view)
                alertDialog.show()

//                KAlertDialog(this@EditCartActivity, KAlertDialog.WARNING_TYPE)
//                    .setTitleText("")
//                    .setContentText("<big><big>Please select min 1 modifier from ${errorMsg.toUpperCase()} Group.</big></big>")
//                    .setConfirmText("OK") // Do not set the property, do not show the button
//                    .show();

//                val builder = AlertDialog.Builder(this@EditCartActivity,R.style.MyDialog)
//
//                // Set the alert dialog title
//                builder.setTitle("Alert.")
//
//                // Display a message on alert dialog
//                builder.setMessage(HtmlCompat.fromHtml("<big><big>Please select min 1 modifier from ${errorMsg.toUpperCase()} Group.</big></big>", 0))
//
//                // Set a positive button and its click listener on alert dialog
//                // Display a negative button on alert dialog
//                builder.setNegativeButton("Ok"){dialog,which ->
//                    dialog.dismiss()
//                }
//
//                // Finally, make the alert dialog using builder
//                val dialog: AlertDialog = builder.create()
//
//
//                // Display the alert dialog on app interface
//                dialog.show()
            }
        }
        EditProductAdapter.firstTimeOnly=0

        if(AppPreferences.compDetail.size>0)
            titleP.text = HtmlCompat.fromHtml(AppPreferences.compDetail[0].CompanyName+" "+AppPreferences.compDetail.get(0).Address.get(0).City,0)
//        titleP.setText(resources.getString(R.string.app_name))

        Handler().postDelayed(Runnable {
            etInst.clearFocus()
        },50)
        etInst.isFocusableInTouchMode = false
        etInst.isFocusable = false
    }

    fun addPrice()
    {
        pricemodel!!.clear()
        for(key in hashMap.keys){
            pricemodel!!.add(hashMap[key]!!)
            println("Element at key $key = ${hashMap[key]}")
        }
    }

    private fun updateCart(orderItem: UpdateCartRequest.OrderItem) {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val datarequest = UpdateCartRequest.RequestContainer("", "",Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""), ""+AppPreferences.appVersion,"kiosk")
        val requestData = UpdateCartRequest.RequestData(orderItem)
        val request = UpdateCartRequest(datarequest, requestData)
        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).updateCart(request)
        },handleSuccess = {
            AppPreferences.cartAutoId =it.d!!.responseData!![0].cartAutoId.toString()
            AppPreferences.cartItemAutoId = it.d.responseData!![0].cartItemAutoId.toString()
            onBackPressed()
        },handleGenric = {
            makeToast(it)
        })

    }

    private fun setPrice() {
        var modelPrice = 0.0
        pricemodel!!.forEach {
            Log.e("PRICE", it.price.toString())
            modelPrice = modelPrice + it.price
        }
        var totalPrice = mainPrice + includePrice + modelPrice
        display.text = qun.toString()
        var finalPrice = totalPrice * qun
        sendPrice = finalPrice
        Log.e("Rk", "Model Price " + finalPrice)

        addToCart.text = "Add to cart   $" + finalPrice.getTwoDigitVlaue()
        if (productResponse!!.d.responseData[0].Discount.toString() == "0.0") {
//            tvTotalPrice.text = "$" + it.itemTotal  !!.getTwoDigitVlaue()
//            tvUnitPrice.text = "$" + it.unitPrice!!.getTwoDigitVlaue()
            addToCart.text = "Add to cart $" + finalPrice.getTwoDigitVlaue()

        } else {
            var dd=productResponse!!.d.responseData[0].Discount
//                    val price =
//                        it.itemTotal!! - (it.itemTotal!! * it.discount!! / 100)
            val priceUni = finalPrice - (finalPrice * dd / 100)
            val text = "Add to cart &nbsp;&nbsp;&nbsp;&nbsp; <strike>$${finalPrice.getTwoDigitVlaue()}</strike>&nbsp;&nbsp;$${priceUni.getTwoDigitVlaue()} "

            addToCart.text = HtmlCompat.fromHtml(text,0)
        }

    }
    fun fromHtml(source: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(source)
        }
    }

    fun loadImage(imageUrl: String?, imageView: ImageView?, progressBar: ProgressBar?) {
        if (imageView == null) {
            return
        }
        if (imageUrl == null) {
            imageView.setImageResource(R.drawable.no_image)
            if (progressBar != null) progressBar.visibility = View.GONE
            return
        }
        Glide.with(this).load(imageUrl).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable?>,
                isFirstResource: Boolean
            ): Boolean {
                if (progressBar != null) {
                    progressBar.visibility = View.GONE
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any,
                target: Target<Drawable?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                if (progressBar != null) progressBar.visibility = View.GONE
                return false
            }
        }).placeholder(R.drawable.no_image)
            .into(imageView)
    }

    private fun apicall() {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val datarequest = ProdDetailRequest.RequestContainer("", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""), "")
        val requestData = ProdDetailRequest.RequestData(data!!.productAutoId!!)
        val request = ProdDetailRequest(datarequest, requestData)
        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).proDetail(request)
        }, handleSuccess = {
            productResponse = it
            it.d.responseData[0].ProductUnit[0].Category.forEach {
            }

//            titleP.setText(it.d.responseData[0].ProductName)

            proTitle.text = it.d.responseData[0].ProductName
            desc.text = it.d.responseData[0].Product_Detail
            if (it.d.responseData[0].Discount != 0.0) {
                tvDiscount.text =
                    "(" + it.d.responseData[0].Discount.toString().replace(".0", "") + "% off)"
            } else {
                tvDiscount.visibility = View.GONE
            }
            loadImage(it.d.responseData[0].ProductImage400px, pImg, pro)
            Log.e("Img", it.d.responseData[0].ProductImage400px)
            mainCate.addData(it.d.responseData[0].ProductUnit)
            it.d.responseData[0].ProductUnit.forEachIndexed { index, productUnit ->
                if (productUnit.IsDefault == 1) {
                    cateadapter.addData(it.d.responseData[0].ProductUnit[index].Category)
                } else {
                    cateadapter.addData(it.d.responseData[0].ProductUnit[index].Category)
                }
            }
            it.d.responseData[0].ProductUnit.forEachIndexed { index, productUnit ->
                if (productUnit.IsDefault == 1) {
                    mainPrice = productUnit.Price
                }
            }
            setPrice()
            layHide.visibility = View.GONE
        }, handleGenric = {
            makeToast(it)
        }
        )
    }


    fun getDiscountedValue(discount: Double, price: Double): String {
        var total = price - ((discount / 100) * price)
        return total.toString()
    }

    override fun onDestroy() {
        mainPrice = 0.0
        includePrice = 0.0
        mainPrice = 0.0
        includePrice = 0.0
        qun = 1
//        EditCatOptionAdapter.selected = -1
        EditMainAdapter.selected = -1
        super.onDestroy()
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

}
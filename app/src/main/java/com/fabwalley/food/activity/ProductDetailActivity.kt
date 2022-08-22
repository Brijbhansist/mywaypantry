package com.fabwalley.food.activity

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.developer.kalert.KAlertDialog
import com.fabwalley.food.R
import com.fabwalley.food.adapter.GridAdapter
import com.fabwalley.food.adapter.MainCateAdapter
import com.fabwalley.food.adapter.ProCateAdapter
import com.fabwalley.food.base.NavigationActivity
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.AppPreferences
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.ApiClient
import com.fabwalley.food.webservices.requestbean.AddToCartRequest
import com.fabwalley.food.webservices.requestbean.ProdDetailRequest
import com.fabwalley.food.webservices.responsebean.PriceModel
import com.fabwalley.food.webservices.responsebean.ProDetailResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.pro_img.*


class ProductDetailActivity : NavigationActivity() {
    var manufacturer = Build.MANUFACTURER
    var model = Build.MODEL
    var version = Build.VERSION.SDK_INT
    var versionRelease = Build.VERSION.RELEASE

    lateinit var cateadapter: ProCateAdapter
    lateinit var mainCate: MainCateAdapter
    private var productResponse: ProDetailResponse? = null

    companion object {
        var isChange = false
        var mainPrice = 0.0
        var includePrice = 0.0
        var qun = 1
        var id = 0
        lateinit var pricemodel: ArrayList<PriceModel>
        lateinit var pricemodel1: ArrayList<PriceModel>
    }
    val hashMap:HashMap<Int,PriceModel> = HashMap<Int,PriceModel>()

    var sendData: ProDetailResponse.ProductUnit? = null
    var sendPrice = 0.0

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
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        pricemodel = ArrayList()
        pricemodel1 = ArrayList()
        decrement.setOnClickListener {
            if (qun > 1) {
                qun -= 1
                setPrice()
            }
        }
        close.setOnClickListener {
//            onBackPressed()
//            KAlertDialog(this@ProductDetailActivity, KAlertDialog.WARNING_TYPE)
//                .setTitleText("")
//                .setContentText("<big><big>Are you sure you want to exit?</big></big>"
//                    )
//                .setConfirmText("YES")
//                .setCancelText("Cancel")
//                .showConfirmButton(true)
//                .showCancelButton(true)
//                .setCancelClickListener{
//                        it-> it.dismissWithAnimation()
//                }
//                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
//                  onBackPressed()
//                }
//                .show()

            val view: View = LayoutInflater.from(this@ProductDetailActivity)
                .inflate(R.layout.report_problem_dialog, null)
            val alertDialog =
                android.app.AlertDialog.Builder(this@ProductDetailActivity).create()
            alertDialog.setCancelable(false)
            val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
            msg.text = "Are you sure you want to exit?"
            val login =
                view.findViewById<View>(R.id.ios_logout) as AppCompatTextView
            login.setOnClickListener {
                alertDialog.dismiss()
                onBackPressed()
            }
            val cancel =
                view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
            cancel.setOnClickListener { alertDialog.dismiss() }
            alertDialog.setView(view)
            alertDialog.show()

            // Finally d
        }
        increment.setOnClickListener {
            qun += 1
            setPrice()
        }

//        etInst.setOnFocusChangeListener(object : View.OnFocusChangeListener {
//          override fun onFocusChange(
//                view: View?,
//                hasFocus: Boolean
//            ) {
//                if (hasFocus) {
////                    Handler().postDelayed(Runnable {
//                        mainscroll.post(Runnable { mainscroll.fullScroll(View.FOCUS_DOWN) })
////                        etInst.requestFocus()
////                    },200)
//                } else {
//
//                }
//            }
//        })

//        etInst.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
//            if (hasFocus)
//                mainscroll.smoothScrollTo(0,mainscroll.bottom)
//        }

        etInst.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    mainscroll.smoothScrollTo(0, mainscroll.bottom)
                    mainscroll.post {
                        Runnable {
                            etInst.requestFocus()
                        }
                    }
//                    scrollView.post(Runnable() {
//                        @Override
//                        public void run() {
//                            et_email.requestFocus();
//                        }
//                    })

                    return false
                }
            })

//        et_email.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ...
//                scrollView.smoothScrollTo(0, et_email.getTop());
//                scrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        et_email.requestFocus();
//                    }
//                }
//                );
//                return false;
//            }
//        }
//        );

        cateadapter = ProCateAdapter(this, object : OnItemClickListner {
            override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                Log.e("Rk", "Get onItemClick Activity  task $task")
                var pos=0
                if (task == 4) {
                    cateadapter.getData().forEachIndexed { index, category ->
                        if(category.OptionCategory==obj2 as String)
                        {
                          pos=index
                        }
                    }
//                    pricemodel1.clear()
                    val data = obj as ProDetailResponse.CategoryOption
//                    Log.e("Array", "Obj  " + obj2 as String)
//                    var test = pricemodel.any { it.parentName == obj2 as String }
//                    if (test) {
//                        pricemodel.forEachIndexed { index, priceModel ->
//                            if (priceModel.parentName.equals(obj2 as String)) {
////                                pricemodel.removeAt(index)
//                            }
//                            else
//                            {
//                                pricemodel1.add(priceModel)
//                            }
//                        }
//                        val d = PriceModel()
//                        d.parentName = obj2 as String
//                        d.price = data.Price
////                        pricemodel.add(d)
//                        pricemodel1.add(d)
//                    } else {
//                        val d = PriceModel()
//                        d.parentName = obj2 as String
//                        d.price = data.Price
////                        pricemodel.add(d)
//                        pricemodel1.add(d)
//                    }
//                    pricemodel.forEachIndexed { index, priceModel ->
//                        if (priceModel.CategoryOptionAutoId == data.CategoryOptionAutoId) {
//                            Log.e("Array", priceModel.CategoryOptionAutoId.toString())
//                            priceModel.Price = data.Price
//                            Log.e("Array","Price Under "+ pricemodel[index].Price)
//                        }
//                    }
//                    pricemodel.clear()
//                    pricemodel.addAll(pricemodel1)
                    val d = PriceModel()
                    d.parentName = obj2 as String
                    d.price = data.Price

                    hashMap.put(pos,d)
                    addPrice()
                    setPrice()
                }
                if (task == 3) {
                    val data = obj as ProDetailResponse.CategoryOption
                    if (obj2 as Boolean == true) {
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

        mainCate = MainCateAdapter(this, object : OnItemClickListner {
            override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                val data = obj as ProDetailResponse.ProductUnit
                pricemodel.clear()
                hashMap.clear()
                includePrice = 0.0
                sendData = data
                mainPrice = data.Price
//                cateadapter.dataList.clear()
                cateadapter.addToData(data.Category)

                setPrice()
            }
        })
        rvPro.adapter = cateadapter
        rvMain.adapter = mainCate

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
            Log.e("Count" ,"" +cateadapter.hashSet.size)
            Log.e("Count" ,"" +cateadapter.count)
            Log.e("Count" ,"" +requiredSize.size)
            if(pricemodel==null)
                pricemodel=ArrayList()

            if (pricemodel.size >= requiredSize.size) {
                val orderItem = AddToCartRequest.OrderItem(
                    cartAutoId = AppPreferences.cartAutoId,
                    cartItemAutoId = "0",
                    referenceNumber = Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    ) + System.currentTimeMillis().toString(),
                    productAutoId = productResponse!!.d.responseData[0].ProductAutoId.toString(),
                    unitAutoId =sendData!!.UnitAutoId.toString(),
                    qty = qun.toString(),
                    orderAddOnItems = cateadapter.selectedItemList,
                    specialInstruction = etInst.text.toString()
                )
                addTocartApiCall(orderItem)
            } else {
                var errorMsg=""
                var contain=0

                for(index in 0 until requiredSize.size)
                {
                    contain=0
                    for(index1 in 0 until pricemodel.size)
                    {
                        if(requiredSize.get(index).OptionCategory.equals(pricemodel.get(index1).parentName,true))
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
                val view: View = LayoutInflater.from(this@ProductDetailActivity)
                    .inflate(R.layout.report_problem_dialog_single_btn, null)
                val alertDialog =
                    android.app.AlertDialog.Builder(this@ProductDetailActivity).create()
                alertDialog.setCancelable(false)
                val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
                msg.text = "Please select min 1 modifier from ${errorMsg.toUpperCase()} Group."
                val cancel =
                    view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
                cancel.setOnClickListener { alertDialog.dismiss() }
                alertDialog.setView(view)
                alertDialog.show()

//                var dialog= KAlertDialog(this@ProductDetailActivity, KAlertDialog.WARNING_TYPE)

//                    dialog
//                     .setContentText("<big><big>Please select min 1 modifier from ${errorMsg.toUpperCase()} Group.</big></big>")
//                    .setConfirmText("OK") // Do not set the property, do not show the button
//                    .show();

//                dialog.setOnShowListener {
//                    var title= dialog.findViewById(com.cazaea.sweetalert.R.id.title_text) as TextView
//                    title.setSingleLine(false);
//                    title.setLines(3)
//                    title.maxLines=3
//                }

//                val builder = AlertDialog.Builder(this@ProductDetailActivity,R.style.MyDialog)
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
//                Snackbar.make(
//                    addToCart,
//                    "Please Select all required categories",
//                    Snackbar.LENGTH_SHORT
//                ).show()
            }
        }
        apicall()
//        titleP.setText(resources.getString(R.string.app_name))

        if(AppPreferences.compDetail.size>0)
            titleP.text = HtmlCompat.fromHtml(AppPreferences.compDetail[0].CompanyName+" "+AppPreferences.compDetail.get(0).Address.get(0).City,0)

        etInst.clearFocus()

    }

    fun addPrice()
    {
        pricemodel.clear()
        for(key in hashMap.keys){
            pricemodel.add(hashMap[key]!!)
            println("Element at key $key = ${hashMap[key]}")
        }
    }

    private fun addTocartApiCall(orderItem: AddToCartRequest.OrderItem) {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val dataRequest = AddToCartRequest.RequestContainer("", "", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""), ""+AppPreferences.appVersion, "kiosk")
        val requestData = AddToCartRequest.RequestData(orderItem)
        val request = AddToCartRequest(dataRequest, requestData)
        val gson = Gson()
       val tt= gson.toJson(request)
        Log.e("print att to cart: ",""+tt)

        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).addToCart(request)
        }, handleSuccess = {
            AppPreferences.cartAutoId = it.d!!.responseData!![0].cartAutoId!!.toString()
            AppPreferences.cartItemAutoId = it.d.responseData!![0].cartItemAutoId!!.toString()
            AppPreferences.cartCount = AppPreferences.cartCount!! + 1
            isChange = true
            showPopup()
        }, handleGenric = {
            makeToast(it)
        })
    }

  fun showPopup()
  {
//      KAlertDialog(this@ProductDetailActivity, KAlertDialog.SUCCESS_TYPE)
//           .setTitleText("")
//          .setContentText("<big><big>1 Item added to the cart.</big></big>")
//          .setConfirmText("Ok")
//          .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation()
//          onBackPressed()
//          }
//          .show()

//      val view: View = LayoutInflater.from(this@ProductDetailActivity)
//          .inflate(R.layout.report_problem_dialog_single_btn, null)
//      val alertDialog =
//          android.app.AlertDialog.Builder(this@ProductDetailActivity).create()
//      alertDialog.setCancelable(false)
//      val alert_img=view.findViewById<View>(R.id.alert_img) as ImageView
//      alert_img.setImageResource(R.drawable.ic_check_circle_outline_black_48dp)
//
//      val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
//      msg.setText("1 Item added to the cart.")
//      val cancel =
//          view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
//      cancel.setOnClickListener { alertDialog.dismiss()
//          onBackPressed()
//      }
//      alertDialog.setView(view)
//      alertDialog.show()

      val view: View = LayoutInflater.from(this@ProductDetailActivity)
          .inflate(R.layout.report_problem_dialog, null)
      val alertDialog =
          android.app.AlertDialog.Builder(this@ProductDetailActivity).create()
//                    alertDialog.setTitle("Please login")
      alertDialog.setCancelable(false)
      //        alertDialog.setMessage("Please Enter your Mobile Number");
      val alert_img=view.findViewById<View>(R.id.alert_img) as ImageView
      alert_img.setImageResource(R.drawable.ic_check_circle_outline_black_48dp)
      val msg=view.findViewById<View>(R.id.msg) as AppCompatTextView
      msg.text = "1 Item added to the cart."
      val login = view.findViewById<View>(R.id.ios_logout) as AppCompatTextView
      login.text = "PROCEED TO CHECKOUT"
      login.setOnClickListener {
          alertDialog.dismiss()
          startActivity(Intent(this,CartActivity::class.java))
          finish()
      }
      val cancel =
          view.findViewById<View>(R.id.ios_cancel) as AppCompatTextView
      cancel.text = "ADD MORE ITEMS"
      cancel.setOnClickListener {
          alertDialog.dismiss()
          onBackPressed()
      }
      alertDialog.setView(view)
      alertDialog.show()


//      val builder = AlertDialog.Builder(this@ProductDetailActivity,R.style.MyDialog)
//
//      // Set the alert dialog title
//      builder.setTitle("Notification.")
//
//      // Display a message on alert dialog
//      builder.setMessage(HtmlCompat.fromHtml("<big><big>1 Item added to the cart.</big></big>", 0))
//
//      // Set a positive button and its click listener on alert dialog
//      // Display a negative button on alert dialog
//      builder.setNegativeButton("Ok"){dialog,which ->
//          dialog.dismiss()
//          onBackPressed()
//      }
//
//      // Finally, make the alert dialog using builder
//      val dialog: AlertDialog = builder.create()
//
//
//      // Display the alert dialog on app interface
//      dialog.show()
  }

    private fun apicall() {
        val sharedPreference =  getSharedPreferences("Location", Context.MODE_PRIVATE)
        val datarequest = ProdDetailRequest.RequestContainer("", Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID),
            ""+sharedPreference.getString("Latitude","")+","+sharedPreference.getString("Longitude",""), "")
        val requestData = ProdDetailRequest.RequestData(id)
        val request = ProdDetailRequest(datarequest, requestData)
        val gson = Gson()
        val tt= gson.toJson(request)
        Log.e("product details from: ",""+tt)
        callApiAndShowDialog(call = {
            ApiClient.getClient(applicationContext as Application).proDetail(request)
        }, handleSuccess = {
            productResponse = it

            val gson = Gson()
            val tt= gson.toJson(it)
            Log.e("product detail cart: ",""+tt)
            it.d.responseData[0].ProductUnit[0].Category.forEach {
//                if (it.OptionIsRequired == 1) {
//                    it.CategoryOption.forEach {
//                        Log.e("optionid", it.CategoryOptionAutoId.toString())
//                        val dd = it
//                        dd.Price = 0.0
//                        pricemodel.add(dd)
//                    }
//                }
            }

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

    private fun setPrice() {
        var modelPrice = 0.0
        pricemodel.forEach {
            modelPrice = modelPrice + it.price
        }
        var totalPrice = mainPrice + includePrice + modelPrice
        display.text = qun.toString()
        var finalPrice = totalPrice * qun
        sendPrice = finalPrice
        Log.e("Rk", "Model Price " + finalPrice)


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
        pricemodel.clear()
        id = 0
        GridAdapter.selected = 999999999
        MainCateAdapter.selected = 999999999
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
package com.fabwalley.food.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabwalley.food.R
import com.fabwalley.food.adapter.CartAdapter
import com.fabwalley.food.base.BaseActivity
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.*
import com.fabwalley.food.webservices.responsebean.PrefCart
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_details.*
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
class ThankYouActivity :BaseActivity() {
    var adapter : CartAdapter?=null
    var handler: Handler = Handler()
    private var mWebView: WebView? = null
    private val record = 0
    private val isBold = false
    private var isUnderLine = false
    private val mStrings = arrayOf(
        "CP437",
        "CP850",
        "CP860",
        "CP863",
        "CP865",
        "CP857",
        "CP737",
        "CP928",
        "Windows-1252",
        "CP866",
        "CP852",
        "CP858",
        "CP874",
        "Windows-775",
        "CP855",
        "CP862",
        "CP864",
        "GB18030",
        "BIG5",
        "KSC5601",
        "utf-8"
    )
    var printerArray = ArrayList<TableItem>()

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
        setContentView(R.layout.activity_order_details)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        tvOrderNumber.text ="Order number:- "+intent.getStringExtra("orderid")

        val list = AppPreferences.saveCart
        adapter= CartAdapter(false,object : OnItemClickListner{
            override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
            }

        })
        rvOrder.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rvOrder.adapter = adapter
        adapter!!.addData(list!!.d!!.responseData!!)
        var totalPrice =0.0
        var totalTax =0.0
        list.d!!.responseData!!.forEach {
            totalPrice+=it.itemTotal!!.toDouble()
            totalTax+=((it.itemTotal *it.TaxPer!!)/100).toDouble()
        }
        subtotal.text="$"+getTwoDigitVlaue(totalPrice)
        var tt=totalPrice+totalTax
        tvAmount.text ="$"+getTwoDigitVlaue(tt)
        taxttotal.text="$${getTwoDigitVlaue(totalTax)}"
        close.setOnClickListener {
            onBackPressed()
        }
        handler.postDelayed(Runnable {
//            onBackPressed()
        },60000)

        //doWebViewPrint(intent.getStringExtra("print"))
//        printPage(intent.getStringExtra("print"))
        setPrinterHeader()
    }

  fun printPage(msg:String)
  {
     var msggg=HtmlCompat.fromHtml(msg,0).toString()
//      var content: String = mEditText.getText().toString()

//      var size: Float = mTextView2.getText().toString().toInt().toFloat()
      if (!BluetoothUtil.isBlueToothPrinter)
      {
          SunmiPrintHelper.getInstance().printText(msggg, 24F, false, false)
          SunmiPrintHelper.getInstance().feedPaper()
          SunmiPrintHelper.getInstance().cutpaper()
      } else
      {
          printByBluTooth(msg)
      }
  }

    private fun printByBluTooth(content: String) {
        try {
            if (isBold) {
                BluetoothUtil.sendData(ESCUtil.boldOn())
            } else {
                BluetoothUtil.sendData(ESCUtil.boldOff())
            }
            if (isUnderLine) {
                BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn())
            } else {
                BluetoothUtil.sendData(ESCUtil.underlineOff())
            }
            if (record < 17) {
                BluetoothUtil.sendData(ESCUtil.singleByte())
                BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)))
            } else {
                BluetoothUtil.sendData(ESCUtil.singleByteOff())
                BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)))
            }
            val charset = Charsets.UTF_8
            BluetoothUtil.sendData(content.toByteArray(charset))
            BluetoothUtil.sendData(ESCUtil.cutter())


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun codeParse(value: Int): Byte {
        var res: Byte = 0x00
        when (value) {
            0 -> res = 0x00
            1, 2, 3, 4 -> res = (value + 1).toByte()
            5, 6, 7, 8, 9, 10, 11 -> res = (value + 8).toByte()
            12 -> res = 21
            13 -> res = 33
            14 -> res = 34
            15 -> res = 36
            16 -> res = 37
            17, 18, 19 -> res = (value - 17).toByte()
            20 -> res = 0xff.toByte()
            else -> {
            }
        }
        return res
    }


    fun getTwoDigitVlaue(price: Double): String? {
        val a: Double =
            DecimalFormat("##.##").format(price).toDouble()
        return String.format(Locale.US, "%.2f", a)
    }

    override fun onBackPressed() {
        AppPreferences.prefCart = PrefCart()
        startActivity(Intent(this,SplashActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
        AppPreferences.cartAutoId = "0"
        AppPreferences.cartItemAutoId = "0"
        AppPreferences.cartCount = 0
        ProductDetailActivity.isChange = true
        finish()
        this.finishAffinity()
//        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if(handler!=null)
            {
                handler.removeCallbacksAndMessages(null)
            }
        } catch (e: Exception) {
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

    private fun doWebViewPrint(html:String) {
        // Create a WebView object specifically for printing
        val webView = WebView(this@ThankYouActivity)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

            override fun onPageFinished(view: WebView, url: String) {
                Log.i("", "page finished loading $url")
                createWebPrintJob(view)
                mWebView = null
            }
        }

        // Generate an HTML document on the fly:
//        val htmlDocument =
//            "<html><body><h1>Test Content</h1><p>Testing, testing, testing...</p></body></html>"
        webView.loadDataWithBaseURL(null, html, "text/HTML", "UTF-8", null)

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView
    }

    private fun createWebPrintJob(webView: WebView) {

        // Get a PrintManager instance
        (this@ThankYouActivity.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${getString(R.string.app_name)} Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().build()
            )
        }
    }

    fun setPrinterHeader()
    {
        printerArray.clear()
      // preparing for header
        if(AppPreferences.compDetail.size>0)
        {
            if(AppPreferences.compDetail.get(0).CompanyName.isNullOrEmpty())
            {
                printerArray.add(TableItem(arrayOf("Pantry 1 Manalapan"), intArrayOf(1), intArrayOf(1)))
            }
            else
            {
                printerArray.add(TableItem(arrayOf(AppPreferences.compDetail.get(0).CompanyName), intArrayOf(1), intArrayOf(1)))
            }

            if(AppPreferences.compDetail.get(0).Address.isNullOrEmpty())
            {
                printerArray.add(TableItem(arrayOf("366 US HWY 9 NORTH"), intArrayOf(1), intArrayOf(1)))
            }
            else
            {
                if(AppPreferences.compDetail.get(0).Address.size>0)
                printerArray.add(TableItem(arrayOf(AppPreferences.compDetail.get(0).Address.get(0).Address), intArrayOf(1), intArrayOf(1)))
            }
            if(AppPreferences.compDetail.get(0).Address.isNullOrEmpty())
            {
                printerArray.add(TableItem(arrayOf("366 US HWY 9 NORTH"), intArrayOf(1), intArrayOf(1)))
            }
            else
            {
                if(AppPreferences.compDetail.get(0).Address.size>0)
                {
                    var msg=""
                    if(!AppPreferences.compDetail.get(0).Address.get(0).City.isNullOrEmpty())
                    {
                        msg=msg+AppPreferences.compDetail.get(0).Address.get(0).City
                    }
                    if(!AppPreferences.compDetail.get(0).Address.get(0).State.isNullOrEmpty())
                    {
                        msg=msg+", "+AppPreferences.compDetail.get(0).Address.get(0).State
                    }
                    if(!AppPreferences.compDetail.get(0).Address.get(0).ZipCode.isNullOrEmpty())
                    {
                        msg=msg+", "+AppPreferences.compDetail.get(0).Address.get(0).ZipCode
                    }
                    printerArray.add(TableItem(arrayOf(msg), intArrayOf(1), intArrayOf(1)))
                }
            }

            if(!AppPreferences.compDetail.get(0).PhoneNo1.isNullOrEmpty())
            {
                printerArray.add(TableItem(arrayOf("Phone: "+AppPreferences.compDetail.get(0).PhoneNo1), intArrayOf(1), intArrayOf(1)))
            }
        }

//        printerArray.add(TableItem(arrayOf("366 US HWY 9 NORTH"), intArrayOf(1), intArrayOf(1)))
//        printerArray.add(TableItem(arrayOf("New Jersey"), intArrayOf(1), intArrayOf(1)))
//        printerArray.add(TableItem(arrayOf("Phone: (732) 385-8730"), intArrayOf(1), intArrayOf(1)))
     // preparing for first item below header
//        printerArray.add(TableItem(arrayOf("Receipt Date:","19-Aug-202 10:06:27 AM"), intArrayOf(1,1), intArrayOf(0,0)))
        printerArray.add(TableItem(arrayOf("---------------------------------------------------------"), intArrayOf(1), intArrayOf(0)))
        val format = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")

        try {
            printerArray.add(TableItem(arrayOf("Order Date:",""+format.format(Date(System.currentTimeMillis()))), intArrayOf(1,1), intArrayOf(0,0)))
        } catch (e: Exception) {
        }

        printerArray.add(TableItem(arrayOf("Order No:",""+intent.getStringExtra("orderid")), intArrayOf(1,1), intArrayOf(0,0)))

        printerArray.add(TableItem(arrayOf("Customer Name:",""+intent.getStringExtra("custname")), intArrayOf(1,1), intArrayOf(0,0)))
        printerArray.add(TableItem(arrayOf("Customer Phone:",""+intent.getStringExtra("custphone")), intArrayOf(1,1), intArrayOf(0,0)))
        printerArray.add(TableItem(arrayOf("Customer Email:",""+intent.getStringExtra("custemail")), intArrayOf(1,1), intArrayOf(0,0)))

//        printerArray.add(TableItem(arrayOf("Bill No:","48"), intArrayOf(1,1), intArrayOf(0,0)))
       //line
        // printing items header
        printerArray.add(TableItem(arrayOf("---------------------------------------------------------"), intArrayOf(1), intArrayOf(0)))
        printerArray.add(TableItem(arrayOf("Qty","Item Name","Total Amt"), intArrayOf(1,3,1), intArrayOf(0,0,0)))
        printerArray.add(TableItem(arrayOf("---------------------------------------------------------"), intArrayOf(1), intArrayOf(0)))

        // printing items of Cart
        val list = AppPreferences.saveCart
        list!!.d!!.responseData!!.forEach {
             Log.e("check ",""+ Gson().toJson(it))
            printerArray.add(TableItem(arrayOf(it.qty.toString(),it.productName.toString(),it.itemTotal.toString()), intArrayOf(1,3,1), intArrayOf(0,0,0)))
            it.categoryOptionList!!.forEach {
                it.optionList!!.forEach {
                    printerArray.add(TableItem(arrayOf("",it.categoryOptionName.toString()+"+ $"+if(it.optionPrice!! > 0) it.optionPrice.getTwoDigitVlaue() else "",""), intArrayOf(1,3,1), intArrayOf(0,0,0)))
                }
            }
        }
        var totalPrice = 0.0
        var totaltax = 0.0
        list.d!!.responseData!!.forEach {
            totalPrice += it.itemTotal!!.toDouble()
            totaltax+=((it.itemTotal*it.TaxPer!!)/100)
        }
        printerArray.add(TableItem(arrayOf("---------------------------------------------------------"), intArrayOf(1), intArrayOf(0)))
        printerArray.add(TableItem(arrayOf("","Sub Total","$ "+totalPrice.getTwoDigitVlaue()), intArrayOf(1,1,1), intArrayOf(0,2,2)))
        printerArray.add(TableItem(arrayOf("","Tax","$ "+totaltax.getTwoDigitVlaue()), intArrayOf(1,1,1), intArrayOf(0,2,2)))
        printerArray.add(TableItem(arrayOf("","","-------------------"), intArrayOf(1,1,1), intArrayOf(0,2,2)))

        printerArray.add(TableItem(arrayOf("","Grand Total:","$ "+(totaltax+totalPrice).getTwoDigitVlaue()), intArrayOf(1,1,1), intArrayOf(0,2,2)))
        printerArray.add(TableItem(arrayOf("---------------------------------------------------------"), intArrayOf(1), intArrayOf(0)))

        if(!AppPreferences.compDetail.get(0).FooterNote.isNullOrEmpty())
        {
            printerArray.add(TableItem(arrayOf(AppPreferences.compDetail.get(0).FooterNote), intArrayOf(1), intArrayOf(1)))
        }
        if(!AppPreferences.compDetail.get(0).Website.isNullOrEmpty())
        {
            printerArray.add(TableItem(arrayOf(AppPreferences.compDetail.get(0).Website), intArrayOf(1), intArrayOf(1)))
        }
        if(!AppPreferences.compDetail.get(0).StorePromotion.isNullOrEmpty())
        {
            printerArray.add(TableItem(arrayOf(AppPreferences.compDetail.get(0).StorePromotion), intArrayOf(1), intArrayOf(1)))
        }

        SunmiPrintHelper.getInstance().sendRawData(ESCUtil.underlineOff())
        if (!BluetoothUtil.isBlueToothPrinter) {
            var count=0
            var lengt=printerArray.size
            SunmiPrintHelper.getInstance().isbold(false)

            for (TableItem in printerArray) {
                if(count<1)
                {
                    SunmiPrintHelper.getInstance().isbold(true)
                    SunmiPrintHelper.getInstance().printLarge(30)
                }
                else
                {
                    SunmiPrintHelper.getInstance().isbold(false)
                }
                if(count>1 && count <4)
                {
                    SunmiPrintHelper.getInstance().printLarge(30)
                }
                if(count > 4)
                {
                    SunmiPrintHelper.getInstance().printLarge(20)
                }
                if(count==(lengt-2))
                {
                    SunmiPrintHelper.getInstance().isbold(true)
                    SunmiPrintHelper.getInstance().printLarge(30)
                }

                SunmiPrintHelper.getInstance().printTable(
                    TableItem.text,
                    TableItem.width, TableItem.align
                )
                count++
            }
            SunmiPrintHelper.getInstance().feedPaper()
            SunmiPrintHelper.getInstance().cutpaper()

        } else {
            BluetoothUtil.sendData(ESCUtil.printBitmap(BytesUtil.initTable(6, 12)))
        }

    }

    class TableItem(txt:Array<String>,wdt:IntArray,algn:IntArray) {
        var text: Array<String>
        var width: IntArray
        var align: IntArray

        init {
            text = txt
            width = wdt
            align = algn
        }
    }

}
package com.fabwalley.food.activity

import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import com.fabwalley.food.R
import com.fabwalley.food.base.BaseActivity
import com.fabwalley.food.utils.BluetoothUtil
import com.fabwalley.food.utils.ESCUtil
import com.fabwalley.food.utils.SunmiPrintHelper
import kotlinx.android.synthetic.main.cuttertest.*
import java.io.IOException

class CutterTestActivity : BaseActivity() {

    private val record = 0
    private val isBold = false
    private var isUnderLine = false
    var cc=HtmlCompat.fromHtml("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head runat=\"server\">\n" +
            "    <title>Print Kitchen Order</title>\n" +
            "    <style>\n" +
            "        .Row {\n" +
            "            display: table;\n" +
            "            width: 100%; /*Optional*/\n" +
            "            table-layout: fixed; /*Optional*/\n" +
            "        }\n" +
            "        td {\n" +
            "            border-right: 1px solid black;\n" +
            "            border-bottom: 1px solid black;\n" +
            "            padding: 0px;\n" +
            "            margin: 0px;\n" +
            "        }\n" +
            "\n" +
            "        table {\n" +
            "            border-collapse: collapse;\n" +
            "        }\n" +
            "\n" +
            "        .Column {\n" +
            "            display: table-cell;\n" +
            "        }\n" +
            "    </style>\n" +
            "    <script src=\"../plugins/jQuery/jquery-2.2.3.min.js\"></script>\n" +
            "    <script src=\"../pages/JS/jquery.min.js\"></script>\n" +
            "    <script src=\"../bootstrap/js/bootstrap.min.js\"></script>\n" +
            "</head>\n" +
            "<body>\n" +
            "        <div style=\"margin-left: 10px; margin-right: 10px; border: 1px solid black; width: 100mm; font-family: Calibri\">\n" +
            "            <div style=\"width: 100%; text-align: center; font-size: 20px; border-bottom: 1px solid black; border-top: 1px solid black;\">My Way Pantry 1 Deli & Grill Belleville</div>\n" +
            "            <div style=\"margin-left: 5px; margin-top: 10px; margin-bottom:5px;\">\n" +
            "                <div>\n" +
            "                    <b>Order No : </b>ORD000076</div>\n" +
            "                    <div>\n" +
            "                    <b>Order Date : </b>08/23/2020  10:06AM</div>\n" +
            "                    <div>\n" +
            "                    <b>Customer Name : </b>sjjs ajja</div>\n" +
            "                    <div>\n" +
            "                    <b>Mobile No : </b>3666944441</div>\n" +
            "                    <div>\n" +
            "                    <b>Delivery Type : </b>PickUp</div>\n" +
            "                     <div>\n" +
            "                    <b>Delivery Date & Time : </b>Now</div>\n" +
            "\n" +
            "            </div>\n" +
            "            <div style=\"width: 100%; text-align: center; font-size: 20px; border-bottom: 1px solid black; border-top: 1px solid black;\">Product Detail</div>\n" +
            "            <div>\n" +
            "                <table style=\"width: 100%; text-align: center;\" id=\"tblList\">\n" +
            "                    <thead>\n" +
            "                        <tr>\n" +
            "                            <td class=\"ProductName\" style=\"width:40%;text-transform: capitalize;\">Product Name</td>\n" +
            "                            <td class=\"AddOn\" style=\"width:50%; text-align:left;padding:4px;font-size: 12px;\">Details</td>\n" +
            "                            <td class=\"Qty\" style=\"width:10%;border-right: none;\">Qty</td>\n" +
            "                        </tr>\n" +
            "                    </thead>\n" +
            "\t\t\t\t\t<tbody>\n" +
            "                    <tr>\n" +
            "\t<td style=\"width:40%;text-transform: capitalize;\">BBQ Grilled chicken <br> <b>Rye bread</b><br> \$7.24<br/><br/><b>Special Instruction</b><br/> </td>\n" +
            "\t<td style=\"width:50%; text-align:left;padding:4px;font-size: 12px;\"><b style=\"color: red;display: block;text-transform: capitalize;\">Choice of cheese</b><span style=\"text-transform: capitalize;\"> Sliced Mozzarella Cheese</span>, <b style=\"color: red;display: block;text-transform: capitalize;\">Choice of sauce</b><span style=\"text-transform: capitalize;\">Spicy mustard</span>, <b style=\"color: red;display: block;text-transform: capitalize;\">Extra topping</b><span style=\"text-transform: capitalize;\">mushrooms</span> + \$0.75, <b style=\"color: red;display: block;text-transform: capitalize;\">Salt and peppers</b><span style=\"text-transform: capitalize;\">EXtra meat</span> + \$1.50, <b style=\"color: red;display: block;text-transform: capitalize;\">Topping</b><span style=\"text-transform: capitalize;\">Tomatoes</span></td>\n" +
            "\t<td class=\"Qty\" style=\"width:10%;border-right: none;\">1</td>\n" +
            "\t</tr>\n" +
            "\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t</table>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "</body>\n" +
            "</html>",0)

    var rawHtml="<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head runat=\"server\">\n" +
            "    <title>Print Kitchen Order</title>\n" +
            "    <style>\n" +
            "        .Row {\n" +
            "            display: table;\n" +
            "            width: 100%; /*Optional*/\n" +
            "            table-layout: fixed; /*Optional*/\n" +
            "        }\n" +
            "        td {\n" +
            "            border-right: 1px solid black;\n" +
            "            border-bottom: 1px solid black;\n" +
            "            padding: 0px;\n" +
            "            margin: 0px;\n" +
            "        }\n" +
            "\n" +
            "        table {\n" +
            "            border-collapse: collapse;\n" +
            "        }\n" +
            "\n" +
            "        .Column {\n" +
            "            display: table-cell;\n" +
            "        }\n" +
            "    </style>\n" +
            "    <script src=\"../plugins/jQuery/jquery-2.2.3.min.js\"></script>\n" +
            "    <script src=\"../pages/JS/jquery.min.js\"></script>\n" +
            "    <script src=\"../bootstrap/js/bootstrap.min.js\"></script>\n" +
            "</head>\n" +
            "<body>\n" +
            "        <div style=\"margin-left: 10px; margin-right: 10px; border: 1px solid black; width: 100mm; font-family: Calibri\">\n" +
            "            <div style=\"width: 100%; text-align: center; font-size: 20px; border-bottom: 1px solid black; border-top: 1px solid black;\">My Way Pantry 1 Deli & Grill</div>\n" +
            "            <div style=\"margin-left: 5px; margin-top: 10px; margin-bottom:5px;\">\n" +
            "                <div>\n" +
            "                    <b>Order No : </b>ORD000076</div>\n" +
            "                    <div>\n" +
            "                    <b>Order Date : </b>08/23/2020  10:06AM</div>\n" +
            "                    <div>\n" +
            "                    <b>Customer Name : </b>sjjs ajja</div>\n" +
            "                    <div>\n" +
            "                    <b>Mobile No : </b>3666944441</div>\n" +
            "                    <div>\n" +
            "                    <b>Delivery Type : </b>PickUp</div>\n" +
            "                     <div>\n" +
            "                    <b>Delivery Date & Time : </b>Now</div>\n" +
            "\n" +
            "            </div>\n" +
            "            <div style=\"width: 100%; text-align: center; font-size: 20px; border-bottom: 1px solid black; border-top: 1px solid black;\">Product Detail</div>\n" +
            "            <div>\n" +
            "                <table style=\"width: 100%; text-align: center;\" id=\"tblList\">\n" +
            "                    <thead>\n" +
            "                        <tr>\n" +
            "                            <td class=\"ProductName\" style=\"width:40%;text-transform: capitalize;\">Product Name</td>\n" +
            "                            <td class=\"AddOn\" style=\"width:50%; text-align:left;padding:4px;font-size: 12px;\">Details</td>\n" +
            "                            <td class=\"Qty\" style=\"width:10%;border-right: none;\">Qty</td>\n" +
            "                        </tr>\n" +
            "                    </thead>\n" +
            "\t\t\t\t\t<tbody>\n" +
            "                    <tr>\n" +
            "\t<td style=\"width:40%;text-transform: capitalize;\">BBQ Grilled chicken <br> <b>Rye bread</b><br> \$7.24<br/><br/><b>Special Instruction</b><br/> </td>\n" +
            "\t<td style=\"width:50%; text-align:left;padding:4px;font-size: 12px;\"><b style=\"color: red;display: block;text-transform: capitalize;\">Choice of cheese</b><span style=\"text-transform: capitalize;\"> Sliced Mozzarella Cheese</span>, <b style=\"color: red;display: block;text-transform: capitalize;\">Choice of sauce</b><span style=\"text-transform: capitalize;\">Spicy mustard</span>, <b style=\"color: red;display: block;text-transform: capitalize;\">Extra topping</b><span style=\"text-transform: capitalize;\">mushrooms</span> + \$0.75, <b style=\"color: red;display: block;text-transform: capitalize;\">Salt and peppers</b><span style=\"text-transform: capitalize;\">EXtra meat</span> + \$1.50, <b style=\"color: red;display: block;text-transform: capitalize;\">Topping</b><span style=\"text-transform: capitalize;\">Tomatoes</span></td>\n" +
            "\t<td class=\"Qty\" style=\"width:10%;border-right: none;\">1</td>\n" +
            "\t</tr>\n" +
            "\t\t\t\t\t</tbody>\n" +
            "\t\t\t\t\t</table>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "</body>\n" +
            "</html>"
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
        setContentView(R.layout.cuttertest)

        test1.setOnClickListener {
            if (!BluetoothUtil.isBlueToothPrinter)
            {

                SunmiPrintHelper.getInstance().printText(cc.toString(), 24F, false, false)
                SunmiPrintHelper.getInstance().feedPaper()
                SunmiPrintHelper.getInstance().cutpaper()
            } else
            {
                printByBluTooth("Test 1 cutter")
                BluetoothUtil.sendData(ESCUtil.cutter())
            }
        }
        test2.setOnClickListener {
            if (!BluetoothUtil.isBlueToothPrinter)
            {
                val charset = Charsets.UTF_8
                SunmiPrintHelper.getInstance().sendRawData(rawHtml.toByteArray(charset))
                SunmiPrintHelper.getInstance().feedPaper()
                Handler().postDelayed(Runnable {
                    SunmiPrintHelper.getInstance().cutpaper()
                },500)
            } else
            {
                printByBluTooth("Test 2 cutter")
                Handler().postDelayed(Runnable {
                    BluetoothUtil.sendData(ESCUtil.cutter())
                },500)
            }
        }
        test3.setOnClickListener {
            if (!BluetoothUtil.isBlueToothPrinter)
            {
                SunmiPrintHelper.getInstance().printText("Test 3", 24F, false, false)
                SunmiPrintHelper.getInstance().feedPaper()
                Handler().postDelayed(Runnable {
                    SunmiPrintHelper.getInstance().cutpaper()
                },500)
            } else
            {
                printByBluTooth("Test 3 cutter")
                Handler().postDelayed(Runnable {
                    BluetoothUtil.sendData(ESCUtil.cutter2())
                },500)
            }
        }

        test4.setOnClickListener {
            if (!BluetoothUtil.isBlueToothPrinter)
            {
                SunmiPrintHelper.getInstance().printText("Test 4", 24F, false, false)
                SunmiPrintHelper.getInstance().feedPaper()
                Handler().postDelayed(Runnable {
                    SunmiPrintHelper.getInstance().cutpaper()
                },500)
            } else
            {
                printByBluTooth("Test 4 cutter")
                Handler().postDelayed(Runnable {
                    BluetoothUtil.sendData(ESCUtil.cutter3())
                },500)
            }
        }

        test5.setOnClickListener {
            if (!BluetoothUtil.isBlueToothPrinter)
            {
                SunmiPrintHelper.getInstance().printText("Test 5", 24F, false, false)
                SunmiPrintHelper.getInstance().feedPaper()
                Handler().postDelayed(Runnable {
                    SunmiPrintHelper.getInstance().cutpaper()
                },500)
            } else
            {
                printByBluTooth("Test 5 cutter")
                Handler().postDelayed(Runnable {
                    BluetoothUtil.sendData(ESCUtil.gogogo())
                },500)
            }
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
            BluetoothUtil.sendData(ESCUtil.nextLine(3))



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

}
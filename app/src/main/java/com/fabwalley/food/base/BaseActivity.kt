package com.fabwalley.food.base

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import com.fabwalley.food.R
import com.fabwalley.food.utils.AppPreferences
import com.google.android.material.snackbar.Snackbar
import com.fabwalley.food.webservices.ResultWrapper
import com.fabwalley.food.webservices.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity(), CoroutineScope {
    private var snackbar: Snackbar? = null
    var mContext: Context = this
    private var toolbar: Toolbar? = null
    private var toolbarTitle: TextView? = null
    private val strUserEndPoints = ArrayList<String>()
    private var alertDialog: AlertDialog? = null
    private var initialLocale: String? = null
    var popUpDialog: AppCompatDialog? = null
    var token: String = ""
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main



    fun showSnackBar(view: View?, str: String?) {
        if (view == null) return
        if (str.isNullOrEmpty()) return
        if (snackbar != null && snackbar!!.isShownOrQueued) {
            snackbar?.dismiss()
        }
        snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    fun showSnackBar(str: String?) {
        val view: View = this.window.decorView
        if (str.isNullOrEmpty()) return
        if (snackbar != null) snackbar?.dismiss()
        snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    fun toast(str: String?) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialog = AppCompatDialog(this)
        dialog!!.setCancelable(false)
        Objects.requireNonNull<Window>(dialog!!.window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setContentView(R.layout.progress_loading)
        val imageView: ImageView = dialog!!.findViewById(R.id.proImg)!!
        val rotate = RotateAnimation(
            0F, 360F,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.duration = 900
        rotate.repeatCount = Animation.INFINITE
        imageView.startAnimation(rotate)
        AppPreferences.init(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                token = task.result?.token!!
//                Log.e("Rk", "fcm  " + token)
//            })


    }

    fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private var dialog: AppCompatDialog? = null


    fun hidedialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    fun setSecureActivity() {
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
    }

    fun keepScreenOn() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


    protected fun updateTitle(@StringRes title: Int) {
        toolbarTitle?.setText(title)
    }

/*
    open fun logout(callBack: (isSuccess: Boolean) -> Unit) {
        try {
            showdialog()
            AppPreferences.isLogin = false
            */
/*  AppHelper.setLogin(false)
              PreferenceHelper.getInstance().removeKey(SharedPrefConstant.USER_DETAILS)*//*

            */
/* WorkerManager.cancelAll()
             DbManager.getInstance().clearDataBase()
             ConnectionService.instance?.restartConnection()  // don't change this line sequence
 *//*

            hidedialog()
            callBack.invoke(true)
        } catch (e: Exception) {
            e.printStackTrace()
            callBack.invoke(false)
        }
    }
*/

    open fun showAlertMessage(str: String) {
        showAlertMessage(str, null)
    }

    open fun showAlertMessage(str: String, onClickListener: DialogInterface.OnClickListener?) {
        showAlertMessage(null, str, true, "", onClickListener)
    }

    open fun showAlertMessage(
        title: String?, str: String, isCancelable: Boolean, positiveText: String,
        onClickListener: DialogInterface.OnClickListener?
    ): AlertDialog? {
        try {
            if (alertDialog != null && alertDialog!!.isShowing) {
                alertDialog!!.dismiss()
            }
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
                .setMessage(str).setCancelable(isCancelable)
                .setPositiveButton(positiveText.takeIf { positiveText.isBlank() }
                    ?: getString(R.string.ok), onClickListener)

            if (!title.isNullOrBlank()) builder.setTitle(title)

            alertDialog = builder.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return alertDialog
    }

    open fun showAlertMessage(str: String, isCancelable: Boolean, positiveText: String, nagetiveText: String, callback: (isPositive: Boolean) -> Unit): AlertDialog? {
        try {
            if (alertDialog != null && alertDialog!!.isShowing) {
                alertDialog!!.dismiss()
            }
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
                .setMessage(str)
                .setCancelable(isCancelable)
                .setPositiveButton(positiveText.takeIf { positiveText.isNotBlank() }
                    ?: getString(R.string.ok)) { _, _ -> callback.invoke(true) }
                .setNegativeButton(nagetiveText.takeIf { nagetiveText.isNotBlank() }
                    ?: getString(R.string.cancel)) { _, _ -> callback.invoke(false) }

            if (!title.isNullOrBlank()) builder.setTitle(title)

            alertDialog = builder.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return alertDialog
    }

//    fun pickDateTime(onPickedDateTime: (calender: Calendar) -> Unit) {
//        val currentDateTime = Calendar.getInstance()
//        val startYear = currentDateTime.get(Calendar.YEAR)
//        val startMonth = currentDateTime.get(Calendar.MONTH)
//        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
//        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
//        val startMinute = currentDateTime.get(Calendar.MINUTE)
//
//        DatePickerDialog(mContext, R.style.DatePickerStyle, DatePickerDialog.OnDateSetListener { _, year, month, day ->
//            TimePickerDialog(mContext, R.style.TimePickerStyle, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
//                val pickedDateTime = Calendar.getInstance()
//                pickedDateTime.set(year, month, day, hour, minute)
//                onPickedDateTime.invoke(pickedDateTime)
//            }, startHour, startMinute, false).show()
//        }, startYear, startMonth, startDay).show()
//    }

    fun showDialog() {
        dialog!!.show()
    }



    fun <T> callApiAndShowDialog(
        call: suspend () -> T,
        handleSuccess: (T) -> Unit,
        handleGenric: (String) -> Unit = {},
        handleNetwork: (String) -> Unit = {},
        showDialg: Boolean = true,
        handledGenerinc: Boolean = false
    ) {

        launch {
            if (showDialg) showDialog()
            val result = safeApiCall(Dispatchers.IO) {
                call.invoke()
            }
            if (showDialg) hideDialog()
            when (result) {
                is ResultWrapper.NetworkError -> {
                    androidx.appcompat.app.AlertDialog.Builder(this@BaseActivity).apply {
                        setView(R.layout.dialog_internet)
                        setPositiveButton("Retry"){ _, _ ->
                            callApiAndShowDialog(
                                call,
                                handleSuccess,
                                handleGenric,
                                handleNetwork,
                                showDialg,
                                handledGenerinc
                            )
                        }
                        setNegativeButton("Cancel"){dialog, _ ->
                            onBackPressed()
                            dialog.dismiss()
                        }
                        setOnCancelListener {
                            dialog!!.dismiss()
                            onBackPressed()
                        }
                        show()
                    }
                    handleNetwork(result.error ?: "Something went wrong")
                }
                is ResultWrapper.GenericError -> {
                    if (handledGenerinc) {
                        handleGenric(result.error ?: "Something went wrong")
                    } else {
                        showPopUp(result.error)
                        handleGenric(result.error ?: "Something went wrong")
                    }
                }
                is ResultWrapper.Success -> {
                    handleSuccess(result.value)
                }
                is ResultWrapper.LoggedError -> {
                    handleNetwork(result.error ?: "Something went wrong")
                }
            }
        }
    }

    inline fun showPopUp(message: String?, crossinline action: () -> Unit = {}) {
        popUpDialog = androidx.appcompat.app.AlertDialog.Builder(this@BaseActivity).run {
            setMessage(message)
            setPositiveButton(android.R.string.ok) { dialogInterface, i ->
                action()
                popUpDialog?.dismiss()
            }
            setOnCancelListener {
                action.invoke()
            }
            create()
        }
        popUpDialog?.show()
    }

    fun makeToast(message: String?) {
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
    }

    fun hideDialog() {
        if (dialog!!.isShowing) dialog!!.dismiss()
    }
}
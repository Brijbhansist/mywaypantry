package com.fabwalley.food.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import com.fabwalley.food.R
import com.fabwalley.food.webservices.ResultWrapper
import com.fabwalley.food.webservices.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(),CoroutineScope {
    var mContext: Context? = null
    private var dialog: AppCompatDialog? = null
    var popUpDialog: AppCompatDialog? = null

    var base: BaseActivity? = null
    var navigation: NavigationActivity? = null
    var rootView: View? = null
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is BaseActivity) {
            base = context
        }
        if (context is NavigationActivity) {
            navigation = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        dialog = AppCompatDialog(activity)
        dialog!!.setCancelable(false)
        Objects.requireNonNull<Window>(dialog!!.window)
            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
    }

    fun showSnackBar(message: String?) {
        if (base == null) return
        base?.showSnackBar(rootView, message)
    }

    fun showSnackBar(view: View?, message: String?) {
        if (base == null) return
        base?.showSnackBar(view, message)
    }

    fun getDummyList(length: Int): List<String> {
        val list: MutableList<String> =
            ArrayList()
        for (i in 0 until length) {
            list.add("test $i")
        }
        return list
    }

    protected fun toMultipartBody(param: String, file: File?): MultipartBody.Part? {
        var attachment: MultipartBody.Part? = null
        val requestFile: RequestBody
        if (file != null) {
            requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val filename = file.name
            attachment = MultipartBody.Part.createFormData(param, filename, requestFile)
        }
        return attachment
    }

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
                    AlertDialog.Builder(activity!!).apply {
                        setView(R.layout.dialog_internet)
                        setPositiveButton("Retry") { _, _ ->
                            callApiAndShowDialog(
                                call,
                                handleSuccess,
                                handleGenric,
                                handleNetwork,
                                showDialg,
                                handledGenerinc
                            )
                        }
                        setNegativeButton("Cancel") { dialog, _ ->
                            activity!!.onBackPressed()
                            dialog.dismiss()
                        }
                        setOnCancelListener {
                            dialog!!.dismiss()
                            activity!!.onBackPressed()
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
        popUpDialog = activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it).run {
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
        }
        popUpDialog?.show()
    }
    fun makeToast(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun hideDialog() {
        if (dialog!!.isShowing) dialog!!.dismiss()
    }
}
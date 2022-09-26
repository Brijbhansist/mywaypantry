package com.fabwalley.food.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fabwalley.food.R
import com.fabwalley.food.activity.CategoryOptionsAdapter
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.AppPreferences
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import kotlinx.android.synthetic.main.item_cate.view.*
import kotlinx.android.synthetic.main.row_cart.view.*


/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
class CartAdapter(var isCart: Boolean, listeners: OnItemClickListner) :
    RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    var listener = listeners
    var dataList = ArrayList<CartDetailResponse.ResponseData>()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var context: Context? = null

        init {
            context = itemView.context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_cart, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    fun loadImage(holder: MyViewHolder,imageUrl: String?, imageView: ImageView?, progressBar: ProgressBar?) {
        if (imageView == null) {
            return
        }
        if (imageUrl == null) {
            imageView.setImageResource(R.drawable.fullmenu)
            if (progressBar != null) progressBar.visibility = View.GONE
            return
        }
        Glide.with(holder.context!!).load(imageUrl).listener(object : RequestListener<Drawable?> {
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
        }).placeholder(R.drawable.fullmenu)
            .into(imageView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        dataList[position].let {
            holder.itemView.apply {
                tvUnitPriceDis.paintFlags = tvUnitPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvTotalPriceDis.paintFlags = tvTotalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                loadImage(holder,it.productImage400px,imgPro,pro)

                tvItemName.text = it.productName!!.trim()
                dish.text = "  " + it.unitName!!.toUpperCase() + "  "
                if (it.productDetail!!.trim().isEmpty()) {
                    tvDesc.visibility = View.GONE
                } else {
                    tvDesc.text = it.productDetail.trim()
                    tvDesc.visibility = View.VISIBLE
                }
                tvQuantity.text = it.qty.toString()
                if(it.specialInstruction.isNullOrEmpty())
                {
                    instr.text="N/A"
                    instrlbl.visibility=View.GONE
                    instr.visibility=View.GONE
                }
                else
                {
                    instrlbl.visibility=View.VISIBLE
                    instr.visibility=View.VISIBLE
                    instr.text=it.specialInstruction.toString()
                }


                if (it.discount.toString() == "0.0") {
                    tvTotalPrice.text = "$" + it.itemTotal  !!.getTwoDigitVlaue()
                    tvUnitPrice.text = "$" + it.unitPrice!!.getTwoDigitVlaue()
                } else {
//                    val price =
//                        it.itemTotal!! - (it.itemTotal!! * it.discount!! / 100)

                    try {
                        val priceUni =
                            it.unitPrice!! - (it.unitPrice * it.discount!! / 100)

                        val price=priceUni*it.qty!!

                        tvUnitPriceDis.text = "$" + it.unitPrice.getTwoDigitVlaue()
//                    tvTotalPriceDis.text = "$" + it.itemTotal.getTwoDigitVlaue()
                        tvTotalPriceDis.text = "$" +(it.unitPrice* it.qty).getTwoDigitVlaue()


                        tvUnitPrice.text = "$" + priceUni.getTwoDigitVlaue()
                        tvTotalPrice.text = "$" + price.getTwoDigitVlaue()+"\n Tax:-$"+((price*it.TaxPer!!)/100).getTwoDigitVlaue()
                    } catch (e: Exception) {
                    }

                    tvQuantityDis.visibility =View.VISIBLE
                    tvUnitPriceDis.visibility =View.VISIBLE
                    tvTotalPriceDis.visibility =View.VISIBLE
                }
                if (isCart) {
                    ivDelete.visibility = View.VISIBLE
                    ivEdit.visibility = View.VISIBLE

                } else {
                    ivDelete.visibility = View.GONE
                    ivEdit.visibility = View.GONE
                }
                rvCartProducts.layoutManager =
                    LinearLayoutManager(holder.context, LinearLayoutManager.VERTICAL, false)
                val cartOptionAdapter = CategoryOptionsAdapter(it.categoryOptionList)
                rvCartProducts.adapter = cartOptionAdapter
//

//                val sb = StringBuilder()
//
//                sb.append(it.categoryOptionList!![position].optionCategory).append("\n")
//                it.categoryOptionList[position].optionList!!.forEach {
//                    if (it.optionPrice.toString().equals("0.0")||it.equals("0")){
//                            sb.append(it.categoryOptionName)
//                        }else{
//                            sb.append(it.categoryOptionName).append(" + $"+it.optionPrice)
//                        }
//                }

//                it.categoryOptionList.forEachIndexed { index, orderAddOnItem ->
//                    if (it.OrderAddOnItems.size-1 == index){
//                        if (orderAddOnItem.Price.equals("0.0")||it.equals("0")){
//                            sb.append(orderAddOnItem.addOnName)
//                        }else{
//                            sb.append(orderAddOnItem.addOnName).append(" + $"+it.Price)
//                        }
//                    }else{
//                        if (orderAddOnItem.Price.equals("0.0")||it.equals("0")){
//                            sb.append(orderAddOnItem.addOnName).append(", ")
//                        }else{
//                            sb.append(orderAddOnItem.addOnName).append(" + $"+it.Price).append(", ")
//                        }
//                    }
//
//                }
//                rvCartProducts.setText(sb)

//                if (isCart){
//                    ivDelete.visibility = View.VISIBLE
//                }else{
//                    ivDelete.visibility= View.GONE
//                }
                ivDelete.setOnClickListener {
                    listener.onItemClick(dataList[position], it, position, 1)
                }
                ivEdit.setOnClickListener {
                    listener.onItemClick(dataList[position], it, position, 2)
                    AppPreferences.cartItemAutoId = dataList[position].cartItemAutoId.toString()
                  //  AppPreferences.set
                   // it.shar
                    //AppPreferences.cartItemAutoId = dataList[position].cartItemAutoId

                }
            }
        }
    }

    fun addData(carList: ArrayList<CartDetailResponse.ResponseData>) {
//        dataList.clear()
        dataList = carList
        notifyDataSetChanged()
    }
}
package com.fabwalley.food.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabwalley.food.R
import com.fabwalley.food.webservices.requestbean.PlaceOrderRequest
import kotlinx.android.synthetic.main.row_text.view.*

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
class CartOptionsAdapter(orderAddOnItems: ArrayList<PlaceOrderRequest.OrderAddOnItem>) : RecyclerView.Adapter<CartOptionsAdapter.MyViewHolder>() {
   var dataList = orderAddOnItems
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_text,parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        dataList[position].let {
            holder.itemView.apply {
                tvCartText.text = it.addOnName+" + $"+it.Price
            }
        }
    }
}
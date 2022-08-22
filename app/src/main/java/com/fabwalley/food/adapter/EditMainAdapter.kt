package com.fabwalley.food.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabwalley.food.R
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import com.fabwalley.food.webservices.responsebean.ProDetailResponse
import kotlinx.android.synthetic.main.item_radio.view.*

class EditMainAdapter(
    private val listData: CartDetailResponse.ResponseData,
    private val itemClick: OnItemClickListner
) :
    RecyclerView.Adapter<EditMainAdapter.BannerViewHolder>() {
    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        var selected = -1
    }

    var dataList: ArrayList<ProDetailResponse.ProductUnit> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_radio_main, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if (selected == -1) {
            if (listData.unitAutoId == dataList[position].UnitAutoId) {
                selected = position
                itemClick.onItemClick(dataList[position], dataList[position], position, 1)
            }
        }
        holder.itemView.radio.isChecked = position == selected
        holder.itemView.radio.text = dataList[position].UnitName +" +$"+dataList[position].Price.getTwoDigitVlaue()
        holder.itemView.radio.setOnClickListener {
            selected = position
            itemClick.onItemClick(dataList[position], dataList[position], position, 1)
            notifyDataSetChanged()
        }

    }

    fun addData(popular: ArrayList<ProDetailResponse.ProductUnit>) {
        dataList = popular
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }
}
package com.fabwalley.food.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fabwalley.food.R
import com.fabwalley.food.activity.ProductDetailActivity
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.requestbean.AddToCartRequest
import com.fabwalley.food.webservices.responsebean.ProDetailResponse
import kotlinx.android.synthetic.main.item_radio.view.*
import kotlin.collections.ArrayList

/**
 * Created By Rahul Kansara (Rk) \n Email : rahul.kansara10@gmail.com on 05-Aug-20.
 */


class GridAdapter(
    val activity: Context?,
    val dataList: ArrayList<ProDetailResponse.CategoryOption>,
    val isMul: Int,
    val isReq:Int,
    val itemClick: OnItemClickListner
) :
    RecyclerView.Adapter<GridAdapter.BannerViewHolder>() {

    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        var selected = -1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_radio, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val data = dataList[position]
        Log.e("Rvs", dataList[position].CategoryOptionName)
        holder.itemView.radio.isChecked = position == selected
        if (dataList[position].Price <= 0.0) {
            holder.itemView.radio.text = dataList[position].CategoryOptionName
        } else {
            holder.itemView.radio.text = dataList[position].CategoryOptionName + " +$" + dataList[position].Price.getTwoDigitVlaue()
        }
        holder.itemView.radio.setOnClickListener {
            if (isMul == 0 && isReq==1) {
                selected = position
                itemClick.onItemClick(dataList[position], dataList[position], position, 4)
                notifyDataSetChanged()
            }
            else {
                if (holder.itemView.radio.isChecked) {
                    itemClick.onItemClick(dataList[position], true, position, 3)
                } else {
                    itemClick.onItemClick(dataList[position], false, position, 3)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }
}
package com.fabwalley.food.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fabwalley.food.R
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.responsebean.ProDetailResponse
import kotlinx.android.synthetic.main.item_pro_cat.view.*
import kotlinx.android.synthetic.main.item_radio.view.*

class MainCateAdapter(val activity: Context?, val itemClick: OnItemClickListner) :
    RecyclerView.Adapter<MainCateAdapter.BannerViewHolder>() {
    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        var selected = 999999999
    }

    var dataList: ArrayList<ProDetailResponse.ProductUnit> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_radio_main, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        if (selected == 999999999) {
            if (dataList[position].IsDefault == 1) {
                holder.itemView.radio.isChecked = true
                selected = position
                itemClick.onItemClick(dataList[position], dataList[position], position, 1)
            } else {
                holder.itemView.radio.isChecked = false
            }
        }else{
            holder.itemView.radio.isChecked = selected ==position
        }
        if (dataList[position].Price==0.0){
            holder.itemView.radio.text = dataList[position].UnitName
        }else{
            holder.itemView.radio.text = dataList[position].UnitName +" +$"+dataList[position].Price.getTwoDigitVlaue()
        }
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


    fun loadImage(imageUrl: String?, imageView: ImageView?, progressBar: ProgressBar?) {
        if (imageView == null) {
            return
        }
        if (imageUrl == null) {
            imageView.setImageResource(R.drawable.fullmenu)
            if (progressBar != null) progressBar.visibility = View.GONE
            return
        }
        Glide.with(activity!!).load(imageUrl).listener(object : RequestListener<Drawable?> {
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


    override fun getItemCount(): Int {
        return dataList.size
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }
}
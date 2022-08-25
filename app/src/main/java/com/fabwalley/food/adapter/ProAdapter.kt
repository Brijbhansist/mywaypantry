package com.fabwalley.food.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
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
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.responsebean.SearchResponse
import kotlinx.android.synthetic.main.item_pro.view.*
import kotlinx.android.synthetic.main.pro_img.view.*
import java.util.*

class ProAdapter(val activity: Context?, val itemClick: OnItemClickListner) :
    RecyclerView.Adapter<ProAdapter.BannerViewHolder>() {
    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        var width = 1
    }

    var dataList: ArrayList<SearchResponse.D.ResponseData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_pro, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.itemView.title.text = dataList[position].ProductName
        if (dataList[position].Price == 0.0) {
            holder.itemView.price.text = "$" + dataList[0].Price.getTwoDigitVlaue()
        } else {
            holder.itemView.price.text = "$" + dataList[position].Price.getTwoDigitVlaue()
        }

        if (dataList[position].Discount > 0.0) {
            val price =
                dataList[position].Price - (dataList[position].Price * (dataList[position].Discount / 100))
//            holder.itemView.tvDiscount.text = dataList[position].Price.toString()+"% off"
            holder.itemView.tvDiscount.text = "$" + dataList[position].Price.getTwoDigitVlaue()
            holder.itemView.price.text = "$" + price.getTwoDigitVlaue()
            holder.itemView.price.setTextColor(activity!!.resources.getColor(R.color.black))
        } else {
            holder.itemView.tvDiscount.text = ""
        }

        holder.itemView.tvDiscount.paintFlags = holder.itemView.tvDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        loadImage(dataList[position].ProductImage400px, holder.itemView.imgPro, holder.itemView.pro)
        holder.itemView.setOnClickListener {
            itemClick.onItemClick(dataList[position], dataList[position], position, position)
        }
        holder.itemView.btn.setOnClickListener {
            itemClick.onItemClick(dataList[position], dataList[position], position, position)
        }
    }

    fun addData(popular: ArrayList<SearchResponse.D.ResponseData>) {
        dataList = popular
        notifyDataSetChanged()
    }

    fun setWidth(i: Int) {
        width = i
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
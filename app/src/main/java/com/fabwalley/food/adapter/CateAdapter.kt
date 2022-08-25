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
import com.fabwalley.food.webservices.responsebean.CateResonse
import com.fabwalley.food.webservices.responsebean.HomePageResponse
import kotlinx.android.synthetic.main.item_cate.view.*
import kotlinx.android.synthetic.main.pro_img.view.*
import java.util.ArrayList

class CateAdapter(val activity: Context?, val itemClick: OnItemClickListner) :
    RecyclerView.Adapter<CateAdapter.BannerViewHolder>() {
    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        var selected = 0
    }

    var dataList: ArrayList<CateResonse.ResponseData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cate, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        loadImage(dataList[position].Thumbimg, holder.itemView.img, holder.itemView.pro)
        holder.itemView.tv.text = dataList[position].CategoryName
        if (position == selected) {
            holder.itemView.mainCate.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context,R.drawable.gredient_back))
            holder.itemView.tv.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
        }else{
            holder.itemView.mainCate.setBackgroundColor(activity!!.resources.getColor(R.color.transperent))
            holder.itemView.tv.setTextColor(activity.resources.getColor(R.color.black))
        }
//        holder.itemView.title.setText(dataList[position].title)
//        val a1 = dataList[position].price.replace("&#8377;","₹").replace("&ndash;"," - ")
//



        holder.itemView.setOnClickListener {
            selected = position
            itemClick.onItemClick(dataList[position],dataList[position], position, position)
            notifyDataSetChanged()
        }
    }

    fun addData(popular: ArrayList<CateResonse.ResponseData>) {
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
}
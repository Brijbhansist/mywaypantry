package com.fabwalley.food.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
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
import com.fabwalley.food.webservices.requestbean.AddToCartRequest
import com.fabwalley.food.webservices.requestbean.PlaceOrderRequest
import com.fabwalley.food.webservices.responsebean.ProDetailResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_pro_cat.view.*
import kotlin.collections.ArrayList

class ProCateAdapter(val activity: Context?, val itemClick: OnItemClickListner) :

    RecyclerView.Adapter<ProCateAdapter.BannerViewHolder>() {
    var count = 0
    var selectedItemList = ArrayList<AddToCartRequest.OrderAddOnItem>()
    val hashMap:HashMap<Int,AddToCartRequest.OrderAddOnItem> = HashMap<Int,AddToCartRequest.OrderAddOnItem>()
    val hashSet :HashSet<AddToCartRequest.OrderAddOnItem> = HashSet<AddToCartRequest.OrderAddOnItem>()
    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        var width = 1
    }

    var dataList: ArrayList<ProDetailResponse.Category> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_pro_cat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        var pos = position
        if (dataList[position].OptionIsRequired == 1 ) {
            holder.itemView.requiredText.text = "Required - Choose 1."
            holder.itemView.main.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorLineGray
                )
            )
        } else {
            holder.itemView.requiredText.text = "Optional - Choose as many as you like."
            holder.itemView.main.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.transperent
                )
            )
        }
        holder.itemView.mainTxt.text = dataList[position].OptionCategory
        val gadapter = GridAdapter(
            activity,
            dataList[position].CategoryOption,

            dataList[position].OptionIsMultiple,
            dataList[position].OptionIsRequired,
            object : OnItemClickListner {
                override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                    try {
//                        selectedItemList1.clear();
                        val data = obj as ProDetailResponse.CategoryOption
                        if (dataList[pos].OptionIsRequired == 1 &&dataList[pos].OptionIsMultiple==0) {
//                            selectedItemList.forEachIndexed { index, categoryOption ->
//                                dataList[pos].CategoryOption.forEachIndexed { indexs, orderAddOnItem ->
//                                    if (categoryOption.optionCategoryAutoId == orderAddOnItem.CategoryOptionAutoId.toString()) {
////                                        selectedItemList.removeAt(index)
//                                        count--
//                                    }
//                                    else
//                                    {
//                                        selectedItemList1.add(categoryOption);
//                                    }
//                                }
//                            }
//                            if(selectedItemList.isNotEmpty()) {
//                                val hasId =
//                                    selectedItemList.any { it.optionAutoId == data.CategoryOptionAutoId.toString() }
////                        var sd= selectedItemList.removeIf { it.OptionAutoId==data.CategoryOptionAutoId.toString() }
//                                if (hasId) {
//                                    val iterator = selectedItemList.iterator()
//                                    while (iterator.hasNext()) {
//                                        val it = iterator.next()
//                                        if (it.optionAutoId == data.CategoryOptionAutoId.toString()) {
//                                            iterator.remove()
//                                        }
//                                    }
//                                }
//                            }
//                            val primeCheck = { num: AddToCartRequest.OrderAddOnItem -> dataList[pos].CategoryOption.any {
//                                it.CategoryOptionAutoId.toString() == num.optionCategoryAutoId
//                            } }
//
//                            selectedItemList.filterTo(selectedItemList,primeCheck)

//                         var filter=   selectedItemList.filter {
//                                it.optionAutoId != data.CategoryOptionAutoId.toString()
//                            }
//                            selectedItemList.clear()
//                            selectedItemList.addAll(selectedItemList1)

//                            selectedItemList.add(
//                                AddToCartRequest.OrderAddOnItem(
//                                    data.CategoryOptionAutoId.toString(),
//                                    dataList[pos].CategoryAutoId.toString(),
//                                    data.Price.toString()
//                                )
//                            )
//                            selectedItemList.add(
//                                AddToCartRequest.OrderAddOnItem(
//                                    data.CategoryOptionAutoId.toString(),
//                                    dataList[pos].CategoryAutoId.toString(),
//                                    data.Price.toString()
//                                )
//                            )
                            hashMap.put(pos,AddToCartRequest.OrderAddOnItem(
                                data.CategoryOptionAutoId.toString(),
                                dataList[pos].CategoryAutoId.toString(),
                                data.Price.toString()
                            ))
//                            selectedItemList.clear()
//                            selectedItemList.addAll(selectedItemList1)
                            count++
                            Log.e("multiple single", " " + hashMap.size)

                        }
//                        else if (selectedItemList.isNotEmpty()) {
//                                val hasId =
//                                    selectedItemList.any { it.optionCategoryAutoId == data.CategoryOptionAutoId.toString() }
////                        var sd= selectedItemList.removeIf { it.OptionAutoId==data.CategoryOptionAutoId.toString() }
//                                if (hasId) {
//                                    val iterator = selectedItemList.iterator()
//                                    while (iterator.hasNext()) {
//                                        val it = iterator.next()
//                                        if (it.optionCategoryAutoId == data.CategoryOptionAutoId.toString()) {
//                                            iterator.remove()
//                                        }
//                                    }
////                            selectedItemList.forEachIndexed { index, orderAddOnItem ->
////                                if (orderAddOnItem.OptionAutoId == data.CategoryOptionAutoId.toString()) {
////                                   if (dataList[pos].OptionIsRequired==0) {
////                                       selectedItemList.removeAt(index)
////                                       return@forEachIndexed
////                                   }
////                                }
////                            }
//                                } else {
//                                    selectedItemList.add(
//                                        AddToCartRequest.OrderAddOnItem(
//                                            data.CategoryOptionAutoId.toString(),
//                                            dataList[pos].CategoryAutoId.toString(),
//                                            data.Price.toString()
//                                        )
//                                    )
//                                }
//                            }
                        else {
//                            val primeCheck = { num: AddToCartRequest.OrderAddOnItem -> dataList[pos].CategoryOption.any {
//                                it.CategoryOptionAutoId.toString() == num.optionCategoryAutoId
//                            } }
//
//                            selectedItemList.filterTo(selectedItemList,primeCheck)
//                                selectedItemList.add(
//                                    AddToCartRequest.OrderAddOnItem(
//                                        data.CategoryOptionAutoId.toString(),
//                                        dataList[pos].CategoryAutoId.toString(),
//                                        data.Price.toString()
//                                    )
//                                )
                            if(hashSet.contains(AddToCartRequest.OrderAddOnItem(
                                    data.CategoryOptionAutoId.toString(),
                                    dataList[pos].CategoryAutoId.toString(),
                                    data.Price.toString()
                                )))
                            {
                                hashSet.remove(AddToCartRequest.OrderAddOnItem(
                                    data.CategoryOptionAutoId.toString(),
                                    dataList[pos].CategoryAutoId.toString(),
                                    data.Price.toString()
                                ))
                            }
                            else
                            {
                                hashSet.add( AddToCartRequest.OrderAddOnItem(
                                    data.CategoryOptionAutoId.toString(),
                                    dataList[pos].CategoryAutoId.toString(),
                                    data.Price.toString()
                                ))
                            }

                            Log.e("multiple", " " + hashSet.size)

                        }
                        Log.e("Rk", "GEt onItemClick ProCate  task " + task)
                        if (task ==4){
                            itemClick.onItemClick(obj, dataList[pos].OptionCategory, position, task)
                        }else{
                            itemClick.onItemClick(obj, obj2, position, task)
                        }
                        updateSelectedItems()
                    } catch (e: Exception) {
                        Log.e("PRODUCT EXCEPTION", e.message!!)
                        Snackbar.make(
                            holder.itemView.gRv,
                            e.localizedMessage!!,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            })
        holder.itemView.gRv.adapter = gadapter

    }

    fun addData(popular: ArrayList<ProDetailResponse.Category>) {
        dataList = popular
        notifyDataSetChanged()
    }

    fun getData():ArrayList<ProDetailResponse.Category>
    {
        return dataList
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

    fun addToData(category: java.util.ArrayList<ProDetailResponse.Category>) {
        hashSet.clear()
        hashMap.clear()
        dataList = category
        GridAdapter.selected = -1
        notifyDataSetChanged()
    }

    fun updateSelectedItems()
    {
        selectedItemList.clear()
        hashSet.toCollection(selectedItemList)
        for(key in hashMap.keys){
            selectedItemList.add(hashMap[key]!!)
            println("Element at key $key = ${hashMap[key]}")
        }

    }
}
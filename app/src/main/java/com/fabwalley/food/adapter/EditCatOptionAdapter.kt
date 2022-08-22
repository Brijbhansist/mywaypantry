package com.fabwalley.food.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabwalley.food.R
import com.fabwalley.food.activity.EditCartActivity
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.requestbean.UpdateCartRequest
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import com.fabwalley.food.webservices.responsebean.ProDetailResponse
import kotlinx.android.synthetic.main.item_radio.view.*

/**
 * Created By Tejas Soni
 */


class EditCatOptionAdapter(var con:EditProductAdapter,
    val dataList: ArrayList<ProDetailResponse.CategoryOption>,
    val isMul: Int,
    val isReq:Int,
    val itemClick: OnItemClickListner
) :
    RecyclerView.Adapter<EditCatOptionAdapter.BannerViewHolder>() {
    var editList: List<CartDetailResponse.Option>? = arrayListOf()

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
        holder.itemView.radio.tag = position
        if (selected == -1) {
            editList!!.forEachIndexed { index, option ->
                if (dataList[position].CategoryOptionAutoId == option.categoryOptionAutoId) {
                    holder.itemView.radio.isChecked = true
                    if (isMul == 0 && isReq==1) {
                        selected = position
                        itemClick.onItemClick(dataList[position], dataList[position], position, 4)
                    } else {
                        itemClick.onItemClick(dataList[position], true, position, 3)
                    }
                }
            }
        }
//        if (position == selected) {
//            holder.itemView.radio.isChecked = true
//        } else {
//            holder.itemView.radio.isChecked = false
//        }
        if (isMul == 0 && isReq==1) {
            if (position == selected) {
                holder.itemView.radio.isChecked = true
                itemClick.onItemClick(dataList[position], dataList[position], position, 4)

            } else {
                holder.itemView.radio.isChecked = false
            }
        }
        else
        {
            if (selected == -1)
            {

            }
            else
            {
                if (con is EditProductAdapter) {
                    try {
                        if (con.hashSet.contains(UpdateCartRequest.OrderAddOnItem(data.CategoryOptionAutoId.toString(),
                                con.dataList[position].CategoryAutoId.toString())))
                        {
                            holder.itemView.radio.isChecked=true
                            itemClick.onItemClick(dataList[position], true, position, 3)
                        }
                    } catch (e: Exception) {
                    }
                }
                else
                {
                    holder.itemView.radio.isChecked=false
                }
            }

//         if( EditProductAdapter.hashSet.contains(
//              UpdateCartRequest.OrderAddOnItem(
//              data.CategoryOptionAutoId.toString(),
//                  EditProductAdapter.dataList[position].CategoryAutoId.toString()
//              )))
//         {
//             holder.itemView.radio.isChecked = true
//             itemClick.onItemClick(dataList[position], true, position, 3)
//
//         }
//            else
//         {
//             holder.itemView.radio.isChecked = false
//
//         }

        }
        if (dataList[position].Price <= 0.0) {
            holder.itemView.radio.text = dataList[position].CategoryOptionName
        } else {
            holder.itemView.radio.text = dataList[position].CategoryOptionName + " +$" + dataList[position].Price.getTwoDigitVlaue()
        }
        holder.itemView.radio.setOnClickListener {
            var pos:Int=Integer.parseInt(""+it.tag)

            if (isMul == 0 && isReq==1) {
                selected = pos
                itemClick.onItemClick(dataList[pos], dataList[pos], pos, 4)
                notifyDataSetChanged()
            } else {
                if (holder.itemView.radio.isChecked) {
                    itemClick.onItemClick(dataList[pos], true, pos, 3)
                } else {
                    itemClick.onItemClick(dataList[pos], false, pos, 3)
                }
            }
        }


    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    fun upDateData(optionList: List<CartDetailResponse.Option>?) {
        editList = optionList
        notifyDataSetChanged()
    }
}
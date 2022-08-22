package com.fabwalley.food.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.fabwalley.food.R
import com.fabwalley.food.utils.getTwoDigitVlaue
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import kotlinx.android.synthetic.main.row_categoryoptions.view.*
import java.lang.StringBuilder

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
class CategoryOptionsAdapter(var categoryOptionList: List<CartDetailResponse.CategoryOption>?) : RecyclerView.Adapter<CategoryOptionsAdapter.MyViewHolder>() {

    class MyViewHolder (view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_categoryoptions,null,false))
    }

    override fun getItemCount(): Int = categoryOptionList!!.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        categoryOptionList!![position].let {
            holder.itemView.apply {
                if (it.optionList!!.isNotEmpty()) {
                    var sb = StringBuilder()
                    sb.append("<font color='#000'><b>"+it.optionCategory!!.trim()+" : </b></font>")
                    tvTitle.text = it.optionCategory.trim()+" : "

                    it.optionList.forEachIndexed { index, option ->
                        if (option.optionPrice!!<=0.0){
                            if (index==it.optionList.size-1){
                                sb.append(option.categoryOptionName!!.trim())
                            }else {
//                                sb.append(option.categoryOptionName!!.trim() + "\n")
                                sb.append(option.categoryOptionName!!.trim() + ", ")

                            }
//                            tvCategory.text = it.categoryOptionName!!.trim()
                        }else{
                            if (index==it.optionList.size-1){
                                sb.append(option.categoryOptionName!!.trim()+" + $"+option.optionPrice.getTwoDigitVlaue())
                            }else {
//                                sb.append(option.categoryOptionName!!.trim() + " + $" + option.optionPrice.getTwoDigitVlaue() + "\n")
                                sb.append(option.categoryOptionName!!.trim() + " + $" + option.optionPrice.getTwoDigitVlaue() + ", ")

                            }
//                            tvCategory.text = it.categoryOptionName!!.trim()+" + $"+it.optionPrice
                        }
                    }
                    if (sb.isNotEmpty()){
//                        tvCategory.text =sb
                        tvTitle.text=HtmlCompat.fromHtml(sb.toString(),0)
                    }
                }
            }
        }

    }
}
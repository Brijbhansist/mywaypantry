package com.fabwalley.food.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fabwalley.food.R
import com.fabwalley.food.base.OnItemClickListner
import com.fabwalley.food.webservices.requestbean.AddToCartRequest
import com.fabwalley.food.webservices.requestbean.UpdateCartRequest
import com.fabwalley.food.webservices.responsebean.CartDetailResponse
import com.fabwalley.food.webservices.responsebean.ProDetailResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_pro_cat.view.*

/**
 * Created By Tejas Soni
 * tejashsoni51331@gmail.com
 */
class EditProductAdapter(var con: Context,
                         var categoryOptinList: List<CartDetailResponse.CategoryOption>?,
                         var listener: OnItemClickListner
) :RecyclerView.Adapter<EditProductAdapter.MyViewHolder>(){
    var count=0
    var dataList: ArrayList<ProDetailResponse.Category> = ArrayList()
    var selectedItemList = ArrayList<UpdateCartRequest.OrderAddOnItem>()
    val hashMap:HashMap<Int,UpdateCartRequest.OrderAddOnItem> = HashMap<Int,UpdateCartRequest.OrderAddOnItem>()
    val hashSet :HashSet<UpdateCartRequest.OrderAddOnItem> = HashSet<UpdateCartRequest.OrderAddOnItem>()

    companion object {
        var firstTimeOnly=0
    }
    class MyViewHolder(view:View) :RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pro_cat, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        var pos = position
        if (dataList[position].OptionIsRequired == 1) {
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
        val gadapter = EditCatOptionAdapter(this,
            dataList[position].CategoryOption,
            dataList[position].OptionIsMultiple,
            dataList[position].OptionIsRequired,
            object : OnItemClickListner {
                override fun onItemClick(obj: Any, obj2: Any, position: Int, task: Int) {
                    try {
                        var selectedItemListdup = ArrayList<UpdateCartRequest.OrderAddOnItem>()
//                        selectedItemListdup.addAll(selectedItemList)
                        val data = obj as ProDetailResponse.CategoryOption
                        if (dataList[pos].OptionIsRequired==1 &&dataList[pos].OptionIsMultiple==0){
//                            selectedItemList.forEachIndexed { index, categoryOption ->
//
//                                dataList[pos].CategoryOption.forEachIndexed { indexs, orderAddOnItem ->
//                                    Log.e("NEW","categoryOption  "+categoryOption.optionCategoryAutoId)
//                                    Log.e("NEW","orderAddOnItem  "+orderAddOnItem.CategoryOptionAutoId)
//                                    if (categoryOption.optionAutoId == orderAddOnItem.CategoryOptionAutoId.toString()){
////                                        selectedItemList.removeAt(index)
//                                        count--
//                                        Log.e("Count",count.toString());
//                                    }
//                                    else
//                                    {
//                                        selectedItemListdup.add(categoryOption)
//                                    }
//                                }
//                            }
//                            selectedItemListdup.add(
//                                UpdateCartRequest.OrderAddOnItem(
//                                    data.CategoryOptionAutoId.toString(),
//                                    dataList[pos].CategoryAutoId.toString()
//                                    )
//                            )
//                            selectedItemList.clear()
//                            selectedItemList.addAll(selectedItemListdup)
                            hashMap.put(pos,UpdateCartRequest.OrderAddOnItem(
                                data.CategoryOptionAutoId.toString(),
                                dataList[pos].CategoryAutoId.toString()
                            ))
                            count++
                            Log.e("Count",count.toString())
                        }
//                        else if (selectedItemList.isNotEmpty()) {
//                                val hasId=  selectedItemList.any { it.optionCategoryAutoId==data.CategoryOptionAutoId.toString() }
//                                if (hasId){
//                                    val iterator = selectedItemList.iterator()
//                                    while (iterator.hasNext()){
//                                        val it = iterator.next()
//                                        if ( it.optionCategoryAutoId==data.CategoryOptionAutoId.toString()){
//                                            iterator.remove()
//                                        }
//                                    }
//                                }else{
//                                    selectedItemList.add(
//                                        UpdateCartRequest.OrderAddOnItem(
//                                            data.CategoryOptionAutoId.toString(),
//                                            dataList[pos].CategoryAutoId.toString()
//                                        )
//                                    )
//                                }
//                            }
                            else{
//                                selectedItemList.add(
//                                    UpdateCartRequest.OrderAddOnItem(
//                                        data.CategoryOptionAutoId.toString(),
//                                        dataList[pos].CategoryAutoId.toString()
//                                    )
//                                )
                            if(hashSet.contains(UpdateCartRequest.OrderAddOnItem(
                                    data.CategoryOptionAutoId.toString(),
                                    dataList[pos].CategoryAutoId.toString()
                                )))
                            {
                                hashSet.remove(UpdateCartRequest.OrderAddOnItem(
                                    data.CategoryOptionAutoId.toString(),
                                    dataList[pos].CategoryAutoId.toString()
                                ))
                            }
                            else
                            {
                                hashSet.add( UpdateCartRequest.OrderAddOnItem(
                                    data.CategoryOptionAutoId.toString(),
                                    dataList[pos].CategoryAutoId.toString()

                                ))
                            }
                        }
                        Log.e("Rk", "GEt onItemClick ProCate  task " + task)
                        if (task ==4){
                            listener.onItemClick(obj, dataList[pos].OptionCategory, position, task)
                        }else{
                            listener.onItemClick(obj, obj2, position, task)
                        }
                        updateSelectedItems()
                    }catch (e:Exception){
                        Snackbar.make(holder.itemView.gRv,e.localizedMessage!!, Snackbar.LENGTH_LONG).show()
                    }
                }
            })
        holder.itemView.gRv.adapter = gadapter

        if(firstTimeOnly==0)
        {
            categoryOptinList!!.forEachIndexed { index, categoryOption ->
                if (dataList[position].OptionCategory==categoryOption.optionCategory){
                    gadapter.upDateData(categoryOption.optionList)
                }
                gadapter.notifyDataSetChanged()
                EditCatOptionAdapter.selected=-1
            }

//            firstTimeOnly=1
        }
        gadapter.notifyDataSetChanged()

    }

    fun getData():ArrayList<ProDetailResponse.Category>
    {
        return dataList
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

    fun addData(category: java.util.ArrayList<ProDetailResponse.Category>) {
        dataList =category
        notifyDataSetChanged()
    }

    fun addToData(category: java.util.ArrayList<ProDetailResponse.Category>) {
            dataList = category
//        EditCatOptionAdapter.selected = -1
        hashMap.clear()
        hashSet.clear()
        selectedItemList.clear()
        EditCatOptionAdapter.selected=-2
        notifyDataSetChanged()
    }
}
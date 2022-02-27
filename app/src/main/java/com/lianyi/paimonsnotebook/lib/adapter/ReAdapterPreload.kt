package com.lianyi.paimonsnotebook.lib.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import kotlin.math.max

//recyclerview通用适配器 带有预加载
class ReAdapterPreload<T>(val data:List<T>, val item:Int, val block:(Adapter<ViewHolder>.(view: View, T, position:Int)->Unit)):
    RecyclerView.Adapter<ViewHolder>(){

    private var scrollState = SCROLL_STATE_IDLE
    var onPreload:(()->Unit)? =null
    var preloadCount = 0
    var isPreloading = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return object :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(item,parent,false)){}
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        block(holder.itemView,data[position],position)
        checkPreload(position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                scrollState = newState
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun checkPreload(position: Int){
        if(onPreload!=null
            && position == max((itemCount - 1 - preloadCount),0)
            &&scrollState!= SCROLL_STATE_IDLE
            &&!isPreloading
        ){
            isPreloading = true
            onPreload?.invoke()
        }
    }
}
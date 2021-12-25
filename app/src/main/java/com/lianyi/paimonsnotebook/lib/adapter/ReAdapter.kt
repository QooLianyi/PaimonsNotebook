package com.lianyi.paimonsnotebook.lib.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//recyclerview通用适配器
class ReAdapter<T>(val data:List<T>,val item:Int,val block:(RecyclerView.Adapter<RecyclerView.ViewHolder>.(view: View, T, position:Int)->Unit)):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(item,parent,false)){}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        block(holder.itemView,data[position],position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
package com.bhumika.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhumika.myapplication.databinding.RvItemsBinding


class RvAdapter(val context: Context, var data:ArrayList<Modal>):
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {
        fun changeData(filteredData : ArrayList<Modal>){
        data = filteredData
        notifyDataSetChanged()
        }
    inner class ViewHolder(val binding: RvItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=RvItemsBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text= data[position].name
        holder.binding.symbol.text= data[position].symbol
        holder.binding.price.text= data[position].price.toString()
    }
}
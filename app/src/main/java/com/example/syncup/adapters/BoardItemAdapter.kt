package com.example.syncup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.syncup.R
import com.example.syncup.databinding.ItemBoardBinding
import com.example.syncup.models.Board

open class BoardItemAdapter(private val context: Context,
                        private val list:ArrayList<Board>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
//        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_board,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            Glide.with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(holder.binding.ivBoardImage)

            holder.binding.tvName.text = model.name
            holder.binding.tvCreatedBy.text = "Created By: ${model.createdBy}"

            holder.binding.fullItem.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }

    interface OnClickListener{
        fun onClick(position: Int, model: Board)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

private class MyViewHolder(val binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root){

}
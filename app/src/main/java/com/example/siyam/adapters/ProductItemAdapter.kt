package com.example.siyam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.siyam.R
import com.example.siyam.databinding.ItemHomeBinding
import com.example.siyam.databinding.ItemProductBinding
import com.example.siyam.databinding.ItemRadiatorsBinding
import com.example.siyam.helpers.HelperUtils.toHTML
import com.example.siyam.model.GetContentList

class ProductItemAdapter (private val list: List<GetContentList>, private val context: Context, private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<ProductItemAdapter.Holder>()  {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRadiatorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.root.setOnClickListener {
            onClickListener.onMove(position)
        }


        holder.binding.text11.text = list[position].title.toHTML()

        Glide.with(context)
            .load(list[position].cover_Image)
            .centerCrop()
            .placeholder(R.drawable.siyamlogo)
            .error(R.drawable.user)
            .into(holder.binding.image1)


    }


    override fun getItemCount(): Int {
        return list.size
    }


    class Holder(val binding: ItemRadiatorsBinding) : RecyclerView.ViewHolder(binding.root)

}
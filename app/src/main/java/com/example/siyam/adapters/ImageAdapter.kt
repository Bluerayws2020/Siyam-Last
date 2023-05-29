package com.example.siyam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.siyam.InterFace.CatlogeClick
import com.example.siyam.R
import com.example.siyam.databinding.ItemHomeBinding
import com.example.siyam.databinding.ItemImageBinding
import com.example.siyam.model.GetCategoryList
import com.example.siyam.model.ImageLink

class ImageAdapter (private val list: List<ImageLink>,
                    private val displayCat: CatlogeClick,
                    private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.Holder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        displayCat.displayCatlogeimage(list[position].img_link,holder.binding.image1)
        holder.itemView.setOnClickListener {
            onItemClick?.let {
                it(list[position].img_link,holder.binding.image1,position)
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    class Holder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClick: ((link : String , image:ImageView, position : Int) ->Unit)? = null
    fun listener (listener:((link : String , image:ImageView, position : Int) ->Unit) ){
        onItemClick = listener
    }

}
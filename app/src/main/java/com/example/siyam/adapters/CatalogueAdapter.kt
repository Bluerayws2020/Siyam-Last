package com.example.siyam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.siyam.InterFace.CatlogeClick
import com.example.siyam.R
import com.example.siyam.databinding.ItemCatalogueBinding
import com.example.siyam.model.CatalogeList

class CatalogueAdapter(private val list: List<CatalogeList>,
                       private val context: Context,
                       private val catlogeClick: CatlogeClick
) : RecyclerView.Adapter<CatalogueAdapter.Holder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCatalogueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        Glide.with(context)
            .load(list[position].img)
            .placeholder(R.drawable.siyamlogo)
            .error(R.drawable.user)
            .into(holder.binding.cataImg)



        holder.binding.pdfTxts.text = list[position].pdf



        holder.binding.cataImg.setOnClickListener{

   catlogeClick.displayCatloge(list[position].pdf)

        }


    }


    override fun getItemCount(): Int {
        return list.size
    }


    class Holder(val binding: ItemCatalogueBinding) : RecyclerView.ViewHolder(binding.root)

}
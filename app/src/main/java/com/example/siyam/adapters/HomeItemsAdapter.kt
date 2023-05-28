package com.example.siyam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siyam.databinding.ItemHomeBinding
import com.example.siyam.model.GetCategoryList

class HomeItemsAdapter (private val list: ArrayList<GetCategoryList>,
                        private val context: Context,
                        private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<HomeItemsAdapter.Holder>()  {




        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(binding)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {

            holder.binding.itemName.text = list[position].name.toString()

            holder.binding.root.setOnClickListener {
                onClickListener.onMove(position)
            }

        }


        override fun getItemCount(): Int {
            return list.size
        }


        class Holder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root)


    }

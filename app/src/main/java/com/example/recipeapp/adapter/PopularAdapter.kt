package com.example.recipeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemPopularBinding
import com.example.recipeapp.models.recipe.ResponseRecipes
import com.example.recipeapp.utils.BaseDiffUtils
import javax.inject.Inject

class PopularAdapter @Inject constructor() : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {
    //Binding
    private lateinit var binding: ItemPopularBinding

    //List
    private var items = emptyList<ResponseRecipes.Result>()


    //Data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.ViewHolder {
        binding = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: PopularAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseRecipes.Result) {
            binding.apply {
                //Text
                popularNameTxt.text = item.title
                popularPriceTxt.text = "${item.pricePerServing} $"
            }
        }
    }

    fun setData(data: List<ResponseRecipes.Result>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}
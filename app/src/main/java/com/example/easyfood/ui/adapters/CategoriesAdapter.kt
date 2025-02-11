package com.example.easyfood.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.data.categories.Category
import com.example.easyfood.databinding.CategoryItemBinding

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder>() {
    private var categoriesList = ArrayList<Category>()
    lateinit var onItemClick : ((Category) -> Unit)

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoriesList(categoriesList : List<Category>){
        this.categoriesList = categoriesList as ArrayList<Category>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        return CategoriesAdapterViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoriesList[position].strCategoryThumb)
            .into(holder.binding.ivCategory)

        holder.binding.tvCategoryName.text = categoriesList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick.invoke(categoriesList[position])
        }
    }

    class CategoriesAdapterViewHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root)
}
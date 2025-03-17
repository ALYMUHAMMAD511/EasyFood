package com.example.easyfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.easyfood.data.meals.Meal
import com.example.easyfood.databinding.MealItemBinding

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavoriteMealsAdapterViewHolder>() {
    lateinit var onItemClick : ((Meal) -> Unit)

    class FavoriteMealsAdapterViewHolder (val binding: MealItemBinding) : ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMealsAdapterViewHolder {
        return FavoriteMealsAdapterViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoriteMealsAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(meal.strMealThumb).
            into(holder.binding.ivCategoryMeal)

        holder.binding.tvMeal.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }
    }
}
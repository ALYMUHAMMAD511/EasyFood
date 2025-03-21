package com.example.easyfood.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.data.category_meals.MealsByCategory
import com.example.easyfood.databinding.PopularItemsBinding

class PopularMealsAdapter : RecyclerView.Adapter<PopularMealsAdapter.PopularMealsViewHolder>() {
    private var mealsList = ArrayList<MealsByCategory>()
    lateinit var onItemClick : ((MealsByCategory) -> Unit)

    @SuppressLint("NotifyDataSetChanged")
    fun setMeals(mealsList : ArrayList<MealsByCategory>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealsViewHolder {
        return PopularMealsViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: PopularMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.ivPopularItems)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
    }

    class PopularMealsViewHolder(val binding: PopularItemsBinding) : RecyclerView.ViewHolder(binding.root)
}
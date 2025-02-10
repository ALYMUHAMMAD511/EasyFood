package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.data.meals.Meal
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.ui.activities.MealActivity
import com.example.easyfood.ui.view_models.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal

    companion object {
        const val MEAL_ID = "com.example.easyfood.ui.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.ui.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.ui.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()
    }

    private fun observeRandomMeal() {
        homeViewModel.observeRandomLiveData().observe(viewLifecycleOwner) { meal ->
            meal?.let {
                Glide.with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .into(binding.ivRandomMeal)

                this.randomMeal = meal
            }
        }
    }

    private fun onRandomMealClick(){
        binding.mainCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }
}


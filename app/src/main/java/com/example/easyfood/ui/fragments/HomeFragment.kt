package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.data.category_meals.MealsByCategory
import com.example.easyfood.data.meals.Meal
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.ui.activities.CategoryMealsActivity
import com.example.easyfood.ui.activities.MainActivity
import com.example.easyfood.ui.activities.MealActivity
import com.example.easyfood.ui.adapters.CategoriesAdapter
import com.example.easyfood.ui.adapters.PopularMealsAdapter
import com.example.easyfood.ui.view_models.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularMealsAdapter: PopularMealsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.easyfood.ui.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.ui.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.ui.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.easyfood.ui.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).homeViewModel
        popularMealsAdapter = PopularMealsAdapter()
        categoriesAdapter = CategoriesAdapter()
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

        preparePopularItemsRecyclerView()
        homeViewModel.getPopularItems()
        observePopularItems()
        onPopularItemClick()
        onSearchClicked()

        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryItemClick()
    }

    private fun onSearchClicked() {
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
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

    private fun onRandomMealClick() {
        binding.mainCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observePopularItems() {
        homeViewModel.observePopularItemsLiveData().observe(viewLifecycleOwner) { mealsList ->
            mealsList?.let {
                popularMealsAdapter.setMeals(mealsList = mealsList as ArrayList<MealsByCategory>)
            }
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.rvPopularMeals.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMealsAdapter
        }
    }

    private fun onPopularItemClick() {
        popularMealsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeCategoriesLiveData() {
        homeViewModel.observeCategoriesLiveData()
            .observe(viewLifecycleOwner) { categories ->
                categoriesAdapter.setCategoriesList(categories)
            }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3 ,GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onCategoryItemClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }
}

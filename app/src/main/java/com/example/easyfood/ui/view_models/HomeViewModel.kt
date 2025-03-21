package com.example.easyfood.ui.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.data.categories.Categories
import com.example.easyfood.data.categories.Category
import com.example.easyfood.data.category_meals.MealsByCategory
import com.example.easyfood.data.category_meals.MealsByCategoryList
import com.example.easyfood.data.meals.Meal
import com.example.easyfood.data.meals.Meals
import com.example.easyfood.pojo.retrofit.RetrofitInstance
import com.example.easyfood.pojo.room.MealDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()

    private var saveStateRandomMeal : Meal? = null


    fun getRandomMeal() {
        saveStateRandomMeal?.let {randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return
        }

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<Meals> {
            override fun onResponse(call: Call<Meals>, response: Response<Meals>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveStateRandomMeal = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<Meals>, t: Throwable) {
                Log.d("HOME FRAGMENT", t.message.toString())
            }
        })
    }

    fun observeRandomLiveData() : LiveData<Meal>{
        return randomMealLiveData
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                if (response.body() != null) {
                    popularItemsLiveData.value = response.body()?.meals
                } else {
                    return
                }
            }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HOME FRAGMENT", t.message.toString())
            }
        })
    }

    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<Categories> {
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                if (response.body() != null) {
                    response.body()?.let { categoryList ->
                        categoriesLiveData.postValue(categoryList.categories)
                    }
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<Categories>, t: Throwable) {
                Log.d("HOME FRAGMENT", t.message.toString())
            }
        })
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavoriteMealsLiveData() : LiveData<List<Meal>>{
        return favoriteMealsLiveData
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun searchMeals(searchQuery: String) {
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<Meals> {
            override fun onResponse(call: Call<Meals>, response: Response<Meals>) {
                if (response.body() != null) {
                    searchedMealsLiveData.value = response.body()?.meals
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<Meals>, t: Throwable) {
                Log.d("HOME FRAGMENT", t.message.toString())
            }
        })
    }

    fun observeSearchedMealsLiveData(): LiveData<List<Meal>> {
        return searchedMealsLiveData
    }
}
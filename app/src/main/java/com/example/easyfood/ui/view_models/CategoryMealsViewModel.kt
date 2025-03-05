package com.example.easyfood.ui.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.data.category_meals.MealsByCategory
import com.example.easyfood.data.category_meals.MealsByCategoryList
import com.example.easyfood.pojo.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {
    private var categoryMealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName : String) {
        RetrofitInstance.api.getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        categoryMealsLiveData.value = response.body()?.meals
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("HOME FRAGMENT", t.message.toString())
                }
            })
    }

    fun observeCategoryMealsLiveData(): LiveData<List<MealsByCategory>> {
        return categoryMealsLiveData
    }
}
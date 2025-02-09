package com.example.easyfood.pojo.retrofit

import com.example.easyfood.data.meals.Meals
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal() : Call<Meals>

    @GET("lookup.php?")
    fun getMealDetailsById(@Query("i") id : String) : Call<Meals>
}
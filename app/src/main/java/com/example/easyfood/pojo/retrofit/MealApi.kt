package com.example.easyfood.pojo.retrofit

import com.example.easyfood.data.Meals
import retrofit2.Call
import retrofit2.http.GET

interface MealApi {
    @GET("random.php")
    fun getRandomMeal() : Call<Meals>
}
package com.example.easyfood.pojo.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.easyfood.data.meals.Meal

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal : Meal)

    @Update
    suspend fun updateMeal(meal : Meal)

    @Delete
    suspend fun deleteMeal(meal : Meal)

    @Query("SELECT * FROM meal_info")
    fun getAllMeals() : LiveData<List<Meal>>
}
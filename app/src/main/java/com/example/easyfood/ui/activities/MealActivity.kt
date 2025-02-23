package com.example.easyfood.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.data.meals.Meal
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.pojo.room.MealDatabase
import com.example.easyfood.ui.fragments.HomeFragment
import com.example.easyfood.ui.view_models.MealViewModel
import com.example.easyfood.ui.view_models.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink : String
    private lateinit var mealViewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.meal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        onLoadingCase()
        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this, mealViewModelFactory)[MealViewModel::class.java]

        getMealInfoFromIntent()
        setInfoInViews()
        mealViewModel.getMealDetails(mealId)
        observeMealDetails()
        onYoutubeIconClicked()
        onFavoriteClick()
    }

    private fun getMealInfoFromIntent() {
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.ivMealDetails)

        binding.collapsingToolbar.title = mealName
    }

    private var mealToSave : Meal? = null
    @SuppressLint("SetTextI18n")
    private fun observeMealDetails() {
        mealViewModel.observeMealDetailsLiveData().observe(this) { meal ->
            meal?.let {
                onResponseCase()
                mealToSave = meal
                binding.tvMealCategory.text = "Category : ${meal.strCategory}"
                binding.tvMealArea.text = "Area : ${meal.strArea}"
                binding.ivMealInstructions.text = meal.strInstructions
                youtubeLink = meal.strYoutube!!
            }
        }
    }

    private fun onLoadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFavorites.visibility = View.INVISIBLE
        binding.tvMealCategory.visibility = View.INVISIBLE
        binding.tvMealArea.visibility = View.INVISIBLE
        binding.ivMealInstructions.visibility = View.INVISIBLE
        binding.youtubeIcon.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFavorites.visibility = View.VISIBLE
        binding.tvMealCategory.visibility = View.VISIBLE
        binding.tvMealArea.visibility = View.VISIBLE
        binding.ivMealInstructions.visibility = View.VISIBLE
        binding.youtubeIcon.visibility = View.VISIBLE
    }

    private fun onYoutubeIconClicked(){
        binding.youtubeIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun onFavoriteClick() {
        binding.btnAddToFavorites.setOnClickListener {
            mealToSave?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
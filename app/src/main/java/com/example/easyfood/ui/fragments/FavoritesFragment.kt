package com.example.easyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.databinding.FragmentFavoritesBinding
import com.example.easyfood.ui.activities.MainActivity
import com.example.easyfood.ui.activities.MealActivity
import com.example.easyfood.ui.adapters.MealsAdapter
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_NAME
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_THUMB
import com.example.easyfood.ui.view_models.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).homeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        prepareFavoritesRecyclerView()
        observeFavoritesLiveData()
        onFavoriteMealsClick()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = mealsAdapter.differ.currentList[position]
                homeViewModel.deleteMeal(deletedMeal)

                Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_SHORT).setAction(
                    "Undo"
                ) {
                    homeViewModel.insertMeal(deletedMeal)
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun prepareFavoritesRecyclerView() {
        mealsAdapter = MealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealsAdapter
        }
    }

    private fun observeFavoritesLiveData() {
        homeViewModel.observeFavoriteMealsLiveData().observe(requireActivity()) { meals ->
            meals?.let {
                mealsAdapter.differ.submitList(meals)
            }
        }
    }

    private fun onFavoriteMealsClick() {
        mealsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }
}
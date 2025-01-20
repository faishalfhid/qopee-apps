package com.dicoding.tugasakhircompose.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.tugasakhircompose.data.FoodRepository
import com.dicoding.tugasakhircompose.ui.screen.cart.CartViewModel
import com.dicoding.tugasakhircompose.ui.screen.detail.DetailViewModel
import com.dicoding.tugasakhircompose.ui.screen.home.FoodViewModel

class ViewModelFactory(private val repository: FoodRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FoodViewModel::class.java) -> {
                return FoodViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
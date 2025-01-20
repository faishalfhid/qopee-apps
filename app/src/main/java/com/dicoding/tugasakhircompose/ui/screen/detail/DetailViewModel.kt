package com.dicoding.tugasakhircompose.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugasakhircompose.data.FoodRepository
import com.dicoding.tugasakhircompose.model.Food
import com.dicoding.tugasakhircompose.model.OrderFood
import com.dicoding.tugasakhircompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: FoodRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<OrderFood>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<OrderFood>>
        get() = _uiState

    fun getRewardById(foodId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getOrderFoodById(foodId))
        }
    }

    fun addToCart(food: Food, count: Int) {
        viewModelScope.launch {
            repository.updateOrderFood(food.id, count)
        }
    }
}
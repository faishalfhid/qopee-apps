package com.dicoding.tugasakhircompose.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugasakhircompose.data.FoodRepository
import com.dicoding.tugasakhircompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: FoodRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CartState>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CartState>>
        get() = _uiState

    fun getAddedOrderRewards() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAddedOrderFood()
                .collect { orderFood ->
                    val totalPrice =
                        orderFood.sumOf { it.food.price * it.count }
                    _uiState.value = UiState.Success(CartState(orderFood, totalPrice))
                }
        }
    }

    fun updateOrderReward(foodId: Int, count: Int) {
        viewModelScope.launch {
            repository.updateOrderFood(foodId, count)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAddedOrderRewards()
                    }
                }
        }
    }
}
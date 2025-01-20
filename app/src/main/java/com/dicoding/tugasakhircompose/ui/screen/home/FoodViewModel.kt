package com.dicoding.tugasakhircompose.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.tugasakhircompose.data.FoodRepository
import com.dicoding.tugasakhircompose.model.Food
import com.dicoding.tugasakhircompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _groupedFoodState = MutableStateFlow<UiState<Map<Char, List<Food>>>>(UiState.Loading)
    val groupedFoodState: StateFlow<UiState<Map<Char, List<Food>>>> get() = _groupedFoodState

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query

    init {
        getFood()
    }

    fun search(newQuery: String) {
        _query.value = newQuery
        getFood()
    }

    private fun getFood() {
        viewModelScope.launch {
            _groupedFoodState.value = UiState.Loading
            try {
                val foodList = repository.searchFood(_query.value)
                    .sortedBy { it.name }
                    .groupBy { it.name[0] }
                _groupedFoodState.value = UiState.Success(foodList)
            } catch (e: Exception) {
                _groupedFoodState.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
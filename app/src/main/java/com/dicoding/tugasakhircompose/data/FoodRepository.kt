package com.dicoding.tugasakhircompose.data

import com.dicoding.tugasakhircompose.model.Food
import com.dicoding.tugasakhircompose.model.FoodData
import com.dicoding.tugasakhircompose.model.OrderFood
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FoodRepository {
    private val orderFood = mutableListOf<OrderFood>()

    init {
        if (orderFood.isEmpty()) {
            FoodData.food.forEach {
                orderFood.add(OrderFood(it, 0))
            }
        }
    }

    fun getFood(): List<Food> {
        return FoodData.food
    }
    fun getAllFood(): Flow<List<OrderFood>> {
        return flowOf(orderFood)
    }
    fun getOrderFoodById(foodId: Int): OrderFood {
        return orderFood.first {
            it.food.id == foodId
        }
    }
    fun searchFood(query: String): List<Food>{
        return FoodData.food.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }
    fun updateOrderFood(foodId: Int, newCountValue: Int): Flow<Boolean> {
        val index = orderFood.indexOfFirst { it.food.id == foodId }
        val result = if (index >= 0) {
            val orderFd = orderFood[index]
            orderFood[index] =
                orderFd.copy(food = orderFd.food, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }
    fun getAddedOrderFood(): Flow<List<OrderFood>> {
        return getAllFood()
            .map { orderRewards ->
                orderRewards.filter { orderReward ->
                    orderReward.count != 0
                }
            }

    }

    companion object {
        @Volatile
        private var instance: FoodRepository? = null

        fun getInstance(): FoodRepository =
            instance ?: synchronized(this) {
                FoodRepository().apply {
                    instance = this
                }
            }
    }
}
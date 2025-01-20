package com.dicoding.tugasakhircompose.ui.screen.cart

import com.dicoding.tugasakhircompose.model.OrderFood

data class CartState(
    val orderFood: List<OrderFood>,
    val totalRequiredPoint: Int
)
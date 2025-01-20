package com.dicoding.tugasakhircompose.ui.screen.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("home/{foodId}"){
        fun createRoute(foodId: Int) = "home/$foodId"
    }
    object Profile : Screen("profile")
    object Cart : Screen("cart")
}
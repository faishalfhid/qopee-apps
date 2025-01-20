package com.dicoding.tugasakhircompose.di

import com.dicoding.tugasakhircompose.data.FoodRepository

object Injection {
    fun provideRepository(): FoodRepository {
        return FoodRepository.getInstance()
    }
}
package com.example.ratingsapp.repositories

import com.example.ratingsapp.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getGames() = apiHelper.getGames()
}
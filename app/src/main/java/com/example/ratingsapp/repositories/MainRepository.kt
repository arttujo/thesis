package com.example.ratingsapp.repositories

import com.example.ratingsapp.api.ApiHelper
import com.example.ratingsapp.models.AuthorCreator

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getGames() = apiHelper.getGames()


    suspend fun postAuthors(creator: AuthorCreator) = apiHelper.postAuthors(creator)
}
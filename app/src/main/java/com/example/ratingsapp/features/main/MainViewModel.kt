package com.example.ratingsapp.features.main

import androidx.lifecycle.ViewModel
import com.example.ratingsapp.databases.AuthorDatabase
import com.example.ratingsapp.databases.GamesDatabase
import com.example.ratingsapp.repositories.MainRepository


class MainViewModel(private val mainRepository: MainRepository):ViewModel() {

    val authorDatabase by lazy { AuthorDatabase(mainRepository) }
    val gamesDatabase by lazy { GamesDatabase(mainRepository) }


}
package com.example.ratingsapp.databases

import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.example.ratingsapp.repositories.MainRepository
import com.example.ratingsapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class GamesDatabase (private val mainRepository: MainRepository){

    fun getGames() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getGames()))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
        }
    }


    val gamesList by lazy {
        Transformations.map(getGames()) {
            it.data ?: emptyList()
        }
    }

    val gamesListStatus by lazy {
        Transformations.map(getGames()) {
            it.status
        }
    }



}
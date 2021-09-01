package com.example.ratingsapp.features.main

import androidx.lifecycle.ViewModel
import com.example.ratingsapp.repositories.MainRepository

class MainViewModel(private val mainRepository: MainRepository):ViewModel() {

    val repository = mainRepository

    fun logout() {

    }


}
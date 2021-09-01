package com.example.ratingsapp.features.main

import com.example.ratingsapp.utils.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.ratingsapp.repositories.MainRepository

class MainViewModel(private val mainRepository: MainRepository):ViewModel() {

    val repository = mainRepository

    val logoutEvent = MutableLiveData<Event<Boolean>>()

    fun logout(navController: NavController,) {
        navController.navigate("login") {
            popUpTo(0)
        }
    }


}
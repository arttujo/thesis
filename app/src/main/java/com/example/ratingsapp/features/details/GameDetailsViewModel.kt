package com.example.ratingsapp.features.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.repositories.ApiError
import com.example.ratingsapp.utils.Event
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.launch

class GameDetailsViewModel : ViewModel() {


    lateinit var mainViewModel: MainViewModel
    var hasInit = false

    val gameId = MutableLiveData<Int>()

    fun init(mainViewModel: MainViewModel, gameId: Int) {
        this.mainViewModel = mainViewModel
        this.gameId.value = gameId
        loadGameDetails()
        hasInit = true
    }


    val game = MutableLiveData<Game>()
    val loading = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<ApiError>().apply { value = null }
    val errorEvent = MutableLiveData<Event<Boolean>>()

    fun loadGameDetails() {
        viewModelScope.launch {
            loading.value = true
            when (val _game = mainViewModel.repository.getGameDetails(gameId.value!!)) {
                is Result.Success -> {
                    game.value = _game.data!!
                }
                is Result.Error -> {
                    errorEvent.value = Event(true)
                    error.value = _game.exception
                }
            }
            loading.value = false
        }

    }


}
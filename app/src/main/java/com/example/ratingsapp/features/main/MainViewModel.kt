package com.example.ratingsapp.features.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ratingsapp.PreferenceKeys
import com.example.ratingsapp.models.Author
import com.example.ratingsapp.repositories.MainRepository
import com.example.ratingsapp.repositories.RawgRepository
import com.example.ratingsapp.utils.Event
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    mainRepository: MainRepository,
    dataStore: DataStore<Preferences>,
    rawgRepository: RawgRepository
) : ViewModel() {

    val repository = mainRepository
    val rawgRepo = rawgRepository

    val store = dataStore

    val logoutEvent = MutableLiveData<Event<Boolean>>()

    val loggedInAs = MutableLiveData<Author?>()


    init {
        val loginData: Flow<String> =
            store.data.map {
                it[PreferenceKeys.LOGGED_IN_AUTHOR] ?: ""
            }

        viewModelScope.launch {
            loginData.collect {
                if (it != "") {
                    loggedInAs.value = Gson().fromJson(it, Author::class.java)
                } else {
                    loggedInAs.value = null
                }
            }
        }
    }


    fun logout(navController: NavController) {
        viewModelScope.launch {
            removeLogin().apply {
                navController.navigate("login") {
                    popUpTo(0)
                }
            }
        }
    }

    suspend fun saveLogin(author: Author) {
        val objStr = Gson().toJson(author)
        store.edit {
            it[PreferenceKeys.LOGGED_IN_AUTHOR] = objStr
        }
    }

    suspend fun removeLogin() {
        store.edit {
            it[PreferenceKeys.LOGGED_IN_AUTHOR] = ""
        }
    }

    fun checkLikedGame(id: Int): Boolean {
        return loggedInAs.value?.likedGames?.contains(id) ?: false
    }

    fun checkLikedReview(id: Int): Boolean {
        return loggedInAs.value?.likedReviews?.contains(id) ?: false
    }

    fun checkLikedComment(id: Int): Boolean {
        return loggedInAs.value?.likedComments?.contains(id) ?: false
    }


}
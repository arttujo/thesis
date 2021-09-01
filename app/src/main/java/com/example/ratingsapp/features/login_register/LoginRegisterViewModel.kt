package com.example.ratingsapp.features.login_register

import android.content.Context
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.repositories.ApiError
import com.example.ratingsapp.utils.Event
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {


    var hasInit = false
    lateinit var mainViewModel: MainViewModel

    fun init(mainViewModel: MainViewModel) {
        hasInit = true
        this.mainViewModel = mainViewModel
    }

    val loading = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<ApiError>().apply { value = null }


    private val _errorEvent = MutableLiveData<Event<Boolean>>()
    val errorEvent = _errorEvent


    /**
     * Login here is simulated. We fetch the users from the api and then compare
     * if any usernames matches we take it and the login is successfull.
     */
    fun login(navController: NavController) {
        viewModelScope.launch {
            loading.value = true
            when (val result = mainViewModel.repository.getAuthors()) {
                is Result.Success -> {
                    val match = result.data.firstOrNull {
                        it.username == username.value
                    }
                    if (match != null) {
                        navController.navigate("main") {
                            popUpTo(0)
                        }
                    } else {
                        _errorEvent.value = Event(true)
                    }
                }
                is Result.Error -> {
                    _errorEvent.value = Event(true)
                    error.value = result.exception
                }
            }
            loading.value = false
        }

    }

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    fun onClearInputClick() {
        _username.value = ""
    }

    fun onInputChange(newInput: String) {
        _username.value = newInput
    }




}

class RegisterViewModel : ViewModel() {


    private lateinit var mvm: MainViewModel

    fun init(mainViewModel: MainViewModel) {
        this.mvm = mainViewModel
    }


    private val _username = MutableLiveData("")
    private val _firstname = MutableLiveData("")
    private val _lastname = MutableLiveData("")

    val username: LiveData<String> = _username
    val firstname: LiveData<String> = _firstname
    val lastname: LiveData<String> = _lastname


    fun clearFirstname() {
        _firstname.value = ""
    }

    fun clearLastname() {
        _lastname.value = ""
    }

    fun clearUsername() {
        _username.value = ""
    }


    fun onFNInputChange(newInput: String) {
        _firstname.value = newInput
    }

    fun onLNInputChange(newInput: String) {
        _lastname.value = newInput
    }

    fun onUNInputChange(newInput: String) {
        _username.value = newInput
    }


}
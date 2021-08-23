package com.example.ratingsapp.features.login_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel:ViewModel() {


    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    fun onClearInputClick(){
        _username.value = ""
    }

    fun onInputChange(newInput: String) {
        _username.value = newInput
    }

    fun onLoginClick(){

    }



}
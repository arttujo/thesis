package com.example.ratingsapp.features.login_register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import com.example.ratingsapp.features.main.MainViewModel

class LoginViewModel:ViewModel() {


    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    fun onClearInputClick(){
        _username.value = ""
    }

    fun onInputChange(newInput: String) {
        _username.value = newInput
    }

    fun onLoginClick(navController: NavController){
        navController.navigate("main") {
            popUpTo(0) // This will essentially clear the backstack preventing back navigate
        }
    }



}

class RegisterViewModel: ViewModel() {


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


    fun clearFirstname() { _firstname.value = "" }
    fun clearLastname() { _lastname.value = "" }
    fun clearUsername() { _username.value = "" }


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
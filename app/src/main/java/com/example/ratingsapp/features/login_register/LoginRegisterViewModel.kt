package com.example.ratingsapp.features.login_register

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.models.Author
import com.example.ratingsapp.models.AuthorCreator
import com.example.ratingsapp.repositories.AUTHOR_CACHE_KEY
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
        load()
    }

    val loading = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<ApiError>().apply { value = null }
    val authors = MutableLiveData<List<Author>>()

    private val _errorEvent = MutableLiveData<Event<Boolean>>()
    val errorEvent = _errorEvent

    /**
     * Load authors for simulated login
     */
    private fun load() {
        viewModelScope.launch {
            loading.value = true
            when (val result = mainViewModel.repository.getAuthors()) {
                is Result.Success -> {
                   authors.value = result.data ?: emptyList()
                }
                is Result.Error -> {
                    _errorEvent.value = Event(true)
                    error.value = result.exception
                }
            }
            loading.value = false
        }
    }

    /**
     * Login
     */
    fun login(navController: NavController) {
        val match = authors.value?.firstOrNull {
            it.username == username.value
        }
        if (match != null) {
            navController.navigate("main") {
                popUpTo(0) // clears backstack essentially
            }
        } else {
            _errorEvent.value = Event(true)
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
    var hasInit = false

    fun init(mainViewModel: MainViewModel) {
        hasInit = true
        this.mvm = mainViewModel
    }

    private val _errorEvent = MutableLiveData<Event<Boolean>>()
    val errorEvent = _errorEvent

    val loading = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<ApiError>().apply { value = null }

    val cachedAuthors by lazy {
        val cached = mvm.repository[AUTHOR_CACHE_KEY]
        // Cache must be null checked before cast
        if (cached != null) cached as List<Author> else null

    }

    fun onRegisterClick(navController: NavController) {
        viewModelScope.launch {
            loading.value = true
            // These can be asserted. Kotlin for some reason thinks they can be bull even when they
            // have a starting value of ""
            val creator = AuthorCreator(username.value!!, firstname.value!!, lastname.value!!)
            if (validUsername(creator)) {
                when (val result = mvm.repository.postAuthors(creator)) {
                    is Result.Success -> {
                        navController.navigate("main") {
                            popUpTo(0) // clears backstack essentially
                        }
                    }
                    is Result.Error -> {
                        _errorEvent.value = Event(true)
                        error.value = result.exception
                    }
                }
            } else {
                _errorEvent.value = Event(true)
            }
            loading.value = false
        }
    }

    /**
     * Another mock. This would normally be done in the backend but JSONServer cannot do validation
     * so we do it client side
     */
    private fun validUsername(author: AuthorCreator):Boolean {
       val exists = cachedAuthors?.firstOrNull {
           it.username == author.username
        }
        Log.d("DBGL", "$exists")
        return exists == null
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
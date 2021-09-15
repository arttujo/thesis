package com.example.ratingsapp.features.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.models.CommentCreator
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.repositories.ApiError
import com.example.ratingsapp.utils.Event
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.launch

class ReviewViewModel:ViewModel() {

    lateinit var mainViewModel: MainViewModel
    var hasInit = false
    private val reviewId = MutableLiveData<Int>()

    fun init (mainViewModel: MainViewModel, reviewId: Int) {
        this.mainViewModel = mainViewModel
        this.reviewId.value = reviewId
        loadReviewDetails()
        hasInit = true
    }

    val review = MutableLiveData<Review>()
    val loading = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<ApiError>().apply { value = null }
    val errorEvent = MutableLiveData<Event<Boolean>>()

    val commentInput = MutableLiveData("")

    fun onInputChanged(input: String) {
        commentInput.value = input
    }


    fun loadReviewDetails() {
        viewModelScope.launch {
            loading.value = true
            when(val _review = mainViewModel.repository.getReviewDetails(reviewId.value!!)) {
                is Result.Success -> {
                    review.value = _review.data!!
                }
                is Result.Error -> {
                    errorEvent.value = Event(true)
                    error.value = _review.exception
                }
            }
            loading.value = false
        }
    }

    fun newComment() {
        val author = mainViewModel.loggedInAs.value!!
        val newComment = CommentCreator(reviewId.value!!, author.username, author.id,commentInput.value!!)
        viewModelScope.launch {
            when (mainViewModel.repository.newComment(newComment)) {
                is Result.Success -> {
                    commentInput.value = ""
                    loadReviewDetails()
                }
                is Result.Error -> {
                    // TODO ERRRO
                }
            }
        }
    }



}
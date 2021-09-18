package com.example.ratingsapp.features.review

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratingsapp.R
import com.example.ratingsapp.components.*
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.models.ReviewCreator
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.Event
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.launch

@Composable
fun NewReviewScreen(mainViewModel: MainViewModel, navController: NavController, gameId: Int?) {

    val vm: NewReviewViewModel = viewModel()
    if (!vm.hasInit) {
        vm.init(mainViewModel, gameId!!)
    }

    Scaffold(topBar = { BackArrowTopBar(navController = navController) }) {
        NewReviewScreenContent(vm = vm)
    }

}

@Composable
fun NewReviewScreenContent(vm: NewReviewViewModel) {

    val titleInput by vm.titleInput.observeAsState()
    val reviewInput by vm.reviewInput.observeAsState()

        ColumnWithDefaultMargin {
            Text(text = stringResource(id = R.string.write_review), style = MaterialTheme.typography.h4)
            LimitedTextInput(
                charLimit = 25,
                hint = stringResource(id = R.string.title_hint),
                modifier = Modifier.padding(top = 32.dp),
                inputValue = titleInput!!,
                onChange = { vm.onTitleInputChange(it) })

            LimitedTextInput(
                charLimit = 250,
                hint = stringResource(id = R.string.new_review),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(200.dp),
                inputValue = reviewInput!!,
                onChange = { vm.onReviewInput(it) })
            
            Button(onClick = { vm.createReview() }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.submit_review))
            }

        }

}

@Preview(showBackground = true)
@Composable
fun NewReviewPreview() {
    RatingsAppTheme {
        NewReviewScreenContent(NewReviewViewModel())
    }
}

class NewReviewViewModel: ViewModel() {

    private lateinit var mainViewModel: MainViewModel
    var gameId = MutableLiveData<Int>()
    var hasInit = false
    fun init (mainViewModel: MainViewModel, gameId: Int) {
        this.mainViewModel = mainViewModel
        this.gameId.value = gameId
        hasInit = true
    }

    val successEvent = MutableLiveData<Event<Boolean>>()
    val errorEvent = MutableLiveData<Event<Boolean>>()

    val author = mainViewModel.loggedInAs.value

    val titleInput = MutableLiveData("")

    val reviewScore = MutableLiveData<Int>()

    fun onTitleInputChange(input: String) {
        titleInput.value = input
    }

    val reviewInput = MutableLiveData("")

    fun onReviewInput(input: String) {
        reviewInput.value = input
    }

    fun createReview() {
        val newReview = ReviewCreator(
            authorUsername = author?.username!!,
            authorId = author?.id!!,
            gameId = gameId.value!!,
            body = reviewInput.value!!,
            title = titleInput.value!!,
            reviewScore = reviewScore.value!!)
        viewModelScope.launch {
            when (mainViewModel.repository.newReview(newReview)) {
                is Result.Success -> {
                    successEvent.value = Event(true)
                }
                is Result.Error -> {
                    errorEvent.value = Event(true)
                }
            }
        }
    }


}
package com.example.ratingsapp.features.review

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratingsapp.R
import com.example.ratingsapp.components.BackArrowTopBar
import com.example.ratingsapp.components.ColumnWithDefaultMargin
import com.example.ratingsapp.components.LimitedTextInput
import com.example.ratingsapp.components.noRippleClickable
import com.example.ratingsapp.createDoneScreen
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.models.ReviewCreator
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.Event
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun NewReviewScreen(mainViewModel: MainViewModel, navController: NavController, gameId: Int?) {

    val vm: NewReviewViewModel = viewModel()
    if (!vm.hasInit) {
        vm.init(mainViewModel, gameId!!)
    }

    val success by vm.successEvent.observeAsState()

    Scaffold(topBar = { BackArrowTopBar(navController = navController) }) {
        NewReviewScreenContent(vm = vm)
    }
    if (success?.getContentIfNotHandled() == true) {
        navController.navigate(
            createDoneScreen(
                "gameDetails/${gameId}",
                stringResource(id = R.string.review_thanks)
            )
        )
    }

}

@Composable
fun NewReviewScreenContent(vm: NewReviewViewModel) {

    val titleInput by vm.titleInput.observeAsState()
    val reviewInput by vm.reviewInput.observeAsState()
    val rating by vm.reviewScore.observeAsState()

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
                .padding(top = 16.dp),
            inputValue = reviewInput!!,
            onChange = { vm.onReviewInput(it) })

        RatingBar(score = rating!!, onSelect = {
            vm.reviewScore.value = it
        })

        Button(
            onClick = { vm.createReview() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            enabled = !titleInput.isNullOrEmpty() && !reviewInput.isNullOrEmpty() && rating!! > 0
        ) {
            Text(text = stringResource(id = R.string.submit_review))
        }

    }

}


@Composable
fun RatingBar(score: Int, modifier: Modifier = Modifier, onSelect: (score: Int) -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {

            val iconMod = Modifier
                .width(40.dp)
                .height(40.dp)
                .noRippleClickable {
                    onSelect(i)
                }
            if (i <= score) {
                Icon(
                    modifier = iconMod,
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = stringResource(id = R.string.star),
                    tint = Color.Black
                )
            } else {
                Icon(
                    modifier = iconMod,
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = stringResource(id = R.string.star),
                    tint = Color.LightGray
                )
            }
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

class NewReviewViewModel : ViewModel() {

    private lateinit var mainViewModel: MainViewModel
    var gameId = MutableLiveData<Int>()
    var hasInit = false

    fun init(mainViewModel: MainViewModel, gameId: Int) {
        this.mainViewModel = mainViewModel
        this.gameId.value = gameId
        hasInit = true
    }

    val successEvent = MutableLiveData<Event<Boolean>>()
    val errorEvent = MutableLiveData<Event<Boolean>>()

    val author by lazy {
        mainViewModel.loggedInAs.value
    }

    val titleInput = MutableLiveData("")

    val reviewScore = MutableLiveData<Int>().apply { value = 0 }

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
            reviewScore = reviewScore.value!!
        )
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
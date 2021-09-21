package com.example.ratingsapp.features.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ratingsapp.R
import com.example.ratingsapp.components.ColumnWithDefaultMargin
import com.example.ratingsapp.components.ReviewList
import com.example.ratingsapp.models.Author
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.repositories.ApiError
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.Event
import com.example.ratingsapp.utils.Result
import com.example.ratingsapp.utils.ReviewListProvider
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(mainVm: MainViewModel, navController: NavController) {
    val author = mainVm.loggedInAs.value

    val vm: ProfileViewModel = viewModel()
    if (!vm.hasInit) {
        vm.init(mainVm)
        if (author != null) {
            vm.loadReviews(author.id)
        }
    }


    val reviews by vm.reviews.observeAsState()
    val deleteEvent by vm.deleteEvent.observeAsState()

    if (author != null) {
        ProfileScreenContent(author = author, reviews = reviews ?: emptyList(), vm, navController)
    } else {
        //TODO ERROR
    }

    deleteEvent?.getContentIfNotHandled().let {
        if (it == true) {
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.successful_delete),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}


@Composable
fun ProfileScreenContent(
    author: Author,
    reviews: List<Review>,
    vm: ProfileViewModel,
    navController: NavController
) {
    val loading by vm.loading.observeAsState()

    ColumnWithDefaultMargin {
        Text(
            text = author.username,
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = author.firstname,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = author.lastname,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SwipeRefresh(state = rememberSwipeRefreshState(loading == true),
            onRefresh = { vm.loadReviews(author.id) }) {
            ReviewList(
                reviews = reviews,
                shownInProfile = true,
                onDeleteClick = { id ->
                    Log.d("DBGL", "Clicked delete on: $id")
                    vm.deleteReview(id)
                },
                onRowClick = { id ->
                    navController.navigate("reviewDetails/${id}")
                }
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfilePreview(@PreviewParameter(ReviewListProvider::class) reviews: List<Review>) {
    RatingsAppTheme {
        ProfileScreenContent(
            Author(1, "test", "test", "tester", reviews = reviews),
            reviews,
            ProfileViewModel(),
            rememberNavController()
        )
    }
}

class ProfileViewModel : ViewModel() {

    private lateinit var mainVm: MainViewModel
    var hasInit = false
    fun init(mainVm: MainViewModel) {
        hasInit = true
        this.mainVm = mainVm
    }


    val loading = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<ApiError>().apply { value = null }
    val reviews = MutableLiveData<List<Review>>()


    fun loadReviews(id: Int) {
        viewModelScope.launch {
            when (val login = mainVm.repository.login(id)) {
                is Result.Success -> {
                    reviews.value = login.data.reviews
                }
                is Result.Error -> {

                }
            }
        }


    }

    val deleteEvent = MutableLiveData<Event<Boolean>>()


    fun deleteReview(id: Int) {
        viewModelScope.launch {
            when (mainVm.repository.deleteReview(id)) {
                is Result.Success -> {
                    // Deletion needs to be updated locally
                    val newList = reviews.value?.filter {
                        it.id != id
                    }
                    reviews.value = newList ?: emptyList()
                    deleteEvent.value = Event(true)

                }
                is Result.Error -> {
                    //TODO ERROR
                }
            }
        }
    }


}
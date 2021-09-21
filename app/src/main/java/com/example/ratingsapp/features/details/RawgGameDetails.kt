package com.example.ratingsapp.features.details

import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ratingsapp.R
import com.example.ratingsapp.components.BackArrowTopBar
import com.example.ratingsapp.components.LoadingOverlay
import com.example.ratingsapp.components.StaticRating
import com.example.ratingsapp.createDoneScreen
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.models.GameCreator
import com.example.ratingsapp.models.RawgGameDetails
import com.example.ratingsapp.repositories.ApiError
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.Event
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt


@Composable
fun RawgGameDetailsScreen(mainViewModel: MainViewModel, navController: NavController, gameId:Int?) {

    val vm: RawgGameDetailsViewModel = viewModel()
    if (!vm.hasInit) {
        vm.init(mainViewModel, gameId!!)
    }
    val matchEvent by vm.matchEvent.observeAsState()
    val successEvent by vm.successEvent.observeAsState()
    val loading by vm.loading.observeAsState()

    Scaffold(topBar = { BackArrowTopBar(navController, useX = true)}) {
        if (loading==true) {
            LoadingOverlay()
        } else {
            RawgGameDetailsContent(vm)
        }
    }

    when(matchEvent?.getContentIfNotHandled()) {
        true -> {
            Toast.makeText(LocalContext.current, stringResource(id = R.string.Cannot_add), Toast.LENGTH_SHORT).show()
        }
        false -> {
            vm.saveToBeReviewed()
        }
    }

    when(successEvent?.getContentIfNotHandled()) {
        true -> {
            navController.navigate(createDoneScreen("main", stringResource(id = R.string.game_added)))
        }
        false -> {
            Toast.makeText(LocalContext.current, stringResource(id = R.string.request_failed), Toast.LENGTH_SHORT).show()

        }
    }

}

@Composable
fun RawgGameDetailsContent(vm: RawgGameDetailsViewModel) {

    val game by vm.data.observeAsState()
    val scrollState = rememberScrollState()

    Log.d("DBGL", "$game")

    LazyColumn(Modifier.padding(16.dp)) {
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (game?.background_image!= null) {
                    Image(
                        painter = rememberImagePainter(game?.background_image),
                        contentDescription = stringResource(id = R.string.game_cover_picture),
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                    )
                } else {
                    Text(text = stringResource(id = R.string.no_image), style = MaterialTheme.typography.h4)
                }

            }
            InfoRow(title = stringResource(id = R.string.game_title), value = game?.name!!)
            InfoRow(title = stringResource(id = R.string.game_developers), value = game?.developers!!.joinToString { it.name })
            InfoRow(title = stringResource(id = R.string.game_description), value = Html.fromHtml(game?.description!!).toString())
            Text(text = "${stringResource(id = R.string.score)}:", style = MaterialTheme.typography.h6)
            StaticRating(score = game?.rating?.roundToInt()!!)
            Button(onClick = { vm.checkIfAdded() },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)) {
                Text(text = stringResource(id = R.string.add_to_ratings))
            }
        }
        
    }
}

@Preview(showBackground = true)
@Composable
fun RawgDetailsPreview() {
    RatingsAppTheme {
        RawgGameDetailsContent(RawgGameDetailsViewModel())
    }
}


class RawgGameDetailsViewModel: ViewModel() {

    private lateinit var mainViewModel: MainViewModel
    var hasInit = false
    val gameId = MutableLiveData<Int?>()

    fun init(mainViewModel: MainViewModel,gameId: Int) {
        this.mainViewModel = mainViewModel
        this.gameId.value = gameId
        getDetails()
        hasInit = true
    }


    val loading = MutableLiveData<Boolean>().apply { value = false }
    val data = MutableLiveData<RawgGameDetails?>()
    val error = MutableLiveData<ApiError>().apply { value = null }


    fun getDetails() {
        viewModelScope.launch {
            loading.value = true
            when (val result = mainViewModel.rawgRepo.getGameDetails(gameId.value!!)) {
                is Result.Success -> {
                    data.value = result.data
                }
                is Result.Error -> {
                    error.value = result.exception
                }
            }
            loading.value = false
        }
    }


    val matchEvent = MutableLiveData<Event<Boolean>>()
    // If returns null there was an error
    fun checkIfAdded() {
        viewModelScope.launch {
              when(val result = mainViewModel.repository.getGames(data.value?.name)) {
                is Result.Success -> {
                    matchEvent.postValue(Event(!result.data.isNullOrEmpty()))
                }
                is Result.Error -> {
                    error.value = result.exception

                }
            }
        }
    }
    
    val successEvent = MutableLiveData<Event<Boolean>>()

     fun saveToBeReviewed() {
        val creator = GameCreator(
            title = data.value?.name!!,
            studio = data.value?.developers?.get(0)?.name ?: "No info",
            imageLink = data.value?.background_image!!
        )

        viewModelScope.launch {
            loading.value = true
             when(val result = mainViewModel.repository.postAuthors(creator)) {
                    is Result.Success -> {
                        successEvent.postValue(Event(true))
                    }
                    is Result.Error -> {
                        successEvent.postValue(Event(false))
                    }

            }
            loading.value = false
        }
    }




}
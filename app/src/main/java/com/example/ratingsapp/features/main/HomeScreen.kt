package com.example.ratingsapp.features.main

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.ratingsapp.components.ColumnWithDefaultMargin
import com.example.ratingsapp.components.LoadingOverlay
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.repositories.ApiError
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.GameListProvider
import com.example.ratingsapp.utils.ReloadSnackBar
import com.example.ratingsapp.utils.Result
import com.example.ratingsapp.utils.ReviewProvider
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalCoilApi
@Composable
fun HomeScreen(mainVm:MainViewModel) {
    val vm: HomeViewModel = viewModel()
    if (!vm.hasInit) {
        vm.init(mainVm)
    }
    val games = vm.games.observeAsState()
    val loading = vm.loading.observeAsState()
    val error = vm.error.observeAsState()


    ColumnWithDefaultMargin {
        if (loading.value == true) {
            LoadingOverlay()
        }
        else {
            HomeGameList(games = games.value?: emptyList())
            if (error.value != null) {
                ReloadSnackBar {
                    vm.load()
                }
                Text(text = error.value?.message ?: "Unkown error")
                Text(text = "Code: ${error.value?.code}")
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun GameListItem(item: Game){
    Card(
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .height(250.dp)
            .width(170.dp)
            .padding(4.dp)
            .clickable {
                //TODO NAVIGATION TO DETAILS
            }) {
        Image(
            painter = rememberImagePainter(item.imageLink),
            contentDescription = "My content description",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
@ExperimentalCoilApi
@Preview
@Composable
fun itemPreview(@PreviewParameter(GameListProvider::class) games: List<Game>){
    RatingsAppTheme {
        GameListItem(item = games[0])
    }

}


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun HomeGameList(games: List<Game>){
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 150.dp)) {
        items(games) { game ->
            GameListItem(item = game)
        }
    }

}

class HomeViewModel:ViewModel(){

    lateinit var mainViewModel: MainViewModel

    var hasInit = false

    fun init(mainViewModel: MainViewModel) {
        hasInit = true
        this.mainViewModel = mainViewModel
        load()
    }

    val loading = MutableLiveData<Boolean>().apply { value = false }
    val games = MutableLiveData<List<Game>>()
    val error = MutableLiveData<ApiError>().apply { value = null }

   fun load() {
       viewModelScope.launch {
           loading.value = true
           when (val result = mainViewModel.repository.getGames()) {
               is Result.Success -> {
                   games.value = result.data ?: emptyList()
               }
               is Result.Error -> {
                   error.value = result.exception
               }
           }
           loading.value = false
       }
   }

/*
    val games by lazy {
        mainViewModel.gamesDatabase.gamesList
    }

    val loading by lazy {
        mainViewModel.gamesDatabase.gamesListLoading
    }

    val error by lazy {
        mainViewModel.gamesDatabase.gamesListError
    }


 */

}
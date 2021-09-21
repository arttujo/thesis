package com.example.ratingsapp.features.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.ratingsapp.R
import com.example.ratingsapp.components.ColumnWithDefaultMargin
import com.example.ratingsapp.components.LoadingOverlay
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.repositories.ApiError
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.GameListProvider
import com.example.ratingsapp.utils.ReloadSnackBar
import com.example.ratingsapp.utils.Result
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalCoilApi
@Composable
fun HomeScreen(mainVm: MainViewModel, navController: NavController) {
    val vm: HomeViewModel = viewModel()
    if (!vm.hasInit) {
        vm.init(mainVm)
    }
    val games = vm.games.observeAsState()
    val loading by vm.loading.observeAsState()
    val error = vm.error.observeAsState()



    ColumnWithDefaultMargin {
        if (loading == true) {
            LoadingOverlay()
        } else {
            SwipeRefresh(state = rememberSwipeRefreshState(loading == true),
                onRefresh = { vm.load() }) {
                HomeGameList(games = games.value ?: emptyList(), navController)
            }
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
fun GameListItem(item: Game, navController: NavController) {
    Card(
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .height(250.dp)
            .width(170.dp)
            .padding(4.dp)
            .clickable {
                navController.navigate("gameDetails/${item.id}")
            }) {
        Image(
            painter = rememberImagePainter(item.imageLink),
            contentDescription = stringResource(R.string.game_cover_picture),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@ExperimentalCoilApi
@Preview
@Composable
fun itemPreview(@PreviewParameter(GameListProvider::class) games: List<Game>) {
    RatingsAppTheme {
        GameListItem(item = games[0], rememberNavController())
    }

}


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun HomeGameList(games: List<Game>, navController: NavController) {
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 150.dp)) {
        items(games) { game ->
            GameListItem(item = game, navController)
        }
    }

}

class HomeViewModel : ViewModel() {

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

}
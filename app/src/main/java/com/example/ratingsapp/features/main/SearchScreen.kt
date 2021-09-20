package com.example.ratingsapp.features.main

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.ratingsapp.GAME_ID
import com.example.ratingsapp.R
import com.example.ratingsapp.components.ColumnWithDefaultMargin
import com.example.ratingsapp.components.GenericClearableTextInput
import com.example.ratingsapp.components.LoadingOverlay
import com.example.ratingsapp.components.SearchTextInput
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.models.RawgBaseResponse
import com.example.ratingsapp.models.RawgGameData
import com.example.ratingsapp.repositories.ApiError
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.GameListProvider
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun SearchScreen(mainViewModel: MainViewModel, navController: NavController) {

    val vm: SearchViewModel = viewModel()
    if (!vm.hasInit) vm.init(mainViewModel)

    SearchContent(vm, navController)
}


@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun SearchContent(vm: SearchViewModel, navController: NavController) {
    val focusManager = LocalFocusManager.current

    val input by vm.searchInput.observeAsState()
    val loading by vm.loading.observeAsState()
    val gamesResult by vm.data.observeAsState()
    val hasQueried by vm.hasQueried.observeAsState()

    if (loading==true) {
        LoadingOverlay()
    }


    ColumnWithDefaultMargin {
        SearchTextInput(
            hint = stringResource(id = R.string.search),
            contentDescription = stringResource(id = R.string.search),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            inputValue = input!!,
            onChange = { vm.onInputChange(it) },
            onClick = {
                vm.search()
                focusManager.clearFocus()
            }
        )
        if (hasQueried == true && gamesResult?.results.isNullOrEmpty()) {
            Text(text = stringResource(id = R.string.no_results), style = MaterialTheme.typography.h4)
        }
        SearchGameList(games = gamesResult?.results ?: emptyList(), navController = navController )

    }



}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun SearchContentPreview() {
    RatingsAppTheme {
        SearchContent(SearchViewModel(), rememberNavController())
    }
}


@ExperimentalCoilApi
@Composable
fun RawgListItem(item: RawgGameData, navController: NavController){
    Card(
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .height(250.dp)
            .width(170.dp)
            .padding(4.dp)
            .clickable {
                navController.navigate("rawgGameDetails/${item.id}")
            }) {
        if (item.backgroundImage != null) {
            Image(
                painter = rememberImagePainter(item.backgroundImage),
                contentDescription = stringResource(R.string.game_cover_picture),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.no_image))
            }
        }
       
    }
}


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun SearchGameList(games: List<RawgGameData>, navController: NavController){

    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 150.dp)) {
        items(games) { game ->
            RawgListItem(item = game, navController)
        }
    }

}


class SearchViewModel: ViewModel() {

    private lateinit var mainViewModel: MainViewModel
    var hasInit = false

    fun init(mainViewModel: MainViewModel) {
        this.mainViewModel = mainViewModel
        hasInit = true
    }

    val hasQueried = MutableLiveData<Boolean>().apply { value = false }
    val searchInput = MutableLiveData<String>().apply { value = "" }

    fun onInputChange(string: String) {
        searchInput.value = string
    }

    val loading = MutableLiveData<Boolean>().apply { value = false }
    val data = MutableLiveData<RawgBaseResponse?>()
    val error = MutableLiveData<ApiError>().apply { value = null }



    fun search() {
        viewModelScope.launch {
            loading.value = true
            when (val result = mainViewModel.rawgRepo.getGames(searchInput.value?:"")) {
                is Result.Success -> {
                   data.value = result.data!!
                }
                is Result.Error -> {
                    error.value = result.exception
                }
            }
            hasQueried.value = true
            loading.value = false
        }
    }


}
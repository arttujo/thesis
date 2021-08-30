package com.example.ratingsapp.features.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import coil.compose.rememberImagePainter
import com.example.ratingsapp.components.ColumnWithDefaultMargin
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.GameListProvider
import com.example.ratingsapp.utils.ReviewProvider

@Composable
fun HomeScreen(mainVm:MainViewModel) {
    ColumnWithDefaultMargin {

    }
}

@Composable
fun GameListItem(item: Game){
    Card(modifier = Modifier
        .height(300.dp)
        .width(200.dp), shape = RoundedCornerShape(10)) {
        Image(
            painter = rememberImagePainter(item.imageLink),
            contentDescription = "My content description",
        )
    }
}
@Preview
@Composable
fun itemPreview(@PreviewParameter(GameListProvider::class) games: List<Game>){
    RatingsAppTheme {
        GameListItem(item = games[0])
    }

}


@ExperimentalFoundationApi
@Composable
fun HomeGameList(games: List<Game>){
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 175.dp)) {
        items(games) { game ->
            GameListItem(item = game)
        }
    }

}

class HomeViewModel:ViewModel(){

    lateinit var mainViewModel: MainViewModel

    fun init(mainViewModel: MainViewModel) {
        this.mainViewModel = mainViewModel
    }

}
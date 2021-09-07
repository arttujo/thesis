package com.example.ratingsapp.features.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ratingsapp.R
import com.example.ratingsapp.components.*
import com.example.ratingsapp.features.main.HomeViewModel
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.repositories.GAME_CACHE_KEY
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.GameListProvider


@Composable
fun GameDetailsScreen(mainViewModel: MainViewModel, navController: NavController, gameId: Int?) {

    val vm: GameDetailsViewModel = viewModel()
    if (!vm.hasInit) {
        if (gameId != null) {
            vm.init(mainViewModel, gameId)
        }
    }
    val hasLiked = gameId?.let { mainViewModel.checkLikedGame(it) }

    val game by vm.game.observeAsState()
    val loading by vm.loading.observeAsState()

    Scaffold(topBar = { BackArrowTopBar(navController = navController , showLike = true, likeAction = {}, hasLiked = hasLiked?:false )
    }) {

        if (loading == true) {
            LoadingOverlay()
        }  else {

            if (game != null) {
                GameDetailsContent(game!!, vm)
            } else {
                // TODO ERROR
            }
        }

    }
}

/**
 * This is pretty much equivalent to ScrollView + RecyclerView with nested scrolling disabled in XML
 */
@Composable
fun GameDetailsContent(game: Game, vm: GameDetailsViewModel) {
    ColumnWithDefaultMargin {
        LazyColumn() {
            item {
                Column {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = rememberImagePainter(game.imageLink),
                            contentDescription = stringResource(id = R.string.game_cover_picture),
                            modifier = Modifier
                                .padding(8.dp)
                                .height(300.dp)
                                .width(200.dp),
                        )
                    }
                    InfoRow(title = stringResource(id = R.string.game_title), value = game.title)
                    InfoRow(title = stringResource(id = R.string.game_studio), value = game.studio)
                    InfoRow(title = stringResource(id = R.string.likes), value = game.likes.toString())
                }
            }

            if (!game.reviews.isNullOrEmpty()) {
                item {
                    InfoRow(title = stringResource(id = R.string.reviews), value = "")
                }
                items(game.reviews) { review ->
                    ReviewRow(review = review, onClick = { /** TODO **/ }, onDeleteClick = {}, isDelete = false)
                }

            } else {
                item {
                    InfoRow(title = stringResource(id = R.string.reviews), value = stringResource(id = R.string.no_reviews))
                }
            }



            //ReviewList(reviews = game.reviews ?: emptyList(), shownInProfile = false)

        }
    }


}

@Composable
fun InfoRow(title: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "${title}:", style = MaterialTheme.typography.h6)
        Text(text = value, style = MaterialTheme.typography.body1)
    }
}

@Preview (showBackground = true)
@Composable
fun InfoRowPreview() {
    RatingsAppTheme {
        InfoRow("Title", "The Elder Scrolls V: Skyrim")
    }
}

@Preview(showSystemUi = true)
@Composable
fun GameDetailPreview(@PreviewParameter(GameListProvider::class) games: List<Game>) {
    RatingsAppTheme {
        GameDetailsContent(games[0], GameDetailsViewModel())
    }
}

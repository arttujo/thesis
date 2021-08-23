package com.example.ratingsapp.features.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.GameListProvider
import com.example.ratingsapp.utils.ReviewProvider

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    RatingsAppTheme() {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(text = "Home")
        }
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


@Composable
fun HomeGameList(){


}
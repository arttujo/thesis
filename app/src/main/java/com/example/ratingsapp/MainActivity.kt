package com.example.ratingsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.ui.theme.RatingsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RatingsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RatingsAppTheme {
        Greeting("Android")
    }
}

@PreviewParameter(ReviewProvider::class)
@Composable
fun ReviewRow(review: Review, isDelete: Boolean = false, onClick: () -> Unit , onDeleteClick: () -> Unit){
    val deleteWeight = if (isDelete) 0.85F else 1F //
    Row(
        modifier = Modifier
            .height(80.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Card(
            shape = RoundedCornerShape(10),
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.weight(deleteWeight)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
                val (likes) = createRefs()
                Text(text = review.authorUsername, style = MaterialTheme.typography.h5, modifier = Modifier.padding(start = 8.dp,top = 4.dp))
                Likes(likes = review.likes, modifier = Modifier.constrainAs(likes) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

            }
        }
        if (isDelete) DeleteButton(onDeleteClick, Modifier.weight(1-deleteWeight))
    }
}

/**
 * Stars for the review rows
 */

@Composable
fun ReviewStats(){

}

/**
 * Likes Container
 */
@Composable
fun Likes(likes: Int, modifier: Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = likes.toString(), style = MaterialTheme.typography.body1 )
        Text(text = stringResource(id = R.string.likes), style = MaterialTheme.typography.body1 )  }
}

/**
 * Delete button
 */
@Composable
fun DeleteButton(onClick: () -> Unit, modifier: Modifier) {
    Box(modifier = modifier
        .height(40.dp)
        .width(40.dp).clickable { onClick() })
    {
        Image(
            Icons.Rounded.Delete,
            contentDescription = stringResource(id = R.string.delete),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.error),
            modifier = Modifier.fillMaxSize()
        )

    }
}

fun mockClick(){}
fun mockDelete() {}

@Preview
@Composable
fun PreviewRow(@PreviewParameter(ReviewProvider::class) review: Review)  {
    RatingsAppTheme {
        ReviewRow(review = review,false, { mockClick() }, { mockDelete() })
    }
}

@Preview
@Composable
fun PreviewDeleteRow(@PreviewParameter(ReviewProvider::class) review: Review) {
    RatingsAppTheme {
        ReviewRow(review = review,true, { mockClick() }, { mockDelete() })
    }
}

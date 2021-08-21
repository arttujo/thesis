package com.example.ratingsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    Row(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()

    ) {
        Card(
            shape = RoundedCornerShape(10),
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            ConstraintLayout(Modifier.fillMaxSize()) {
                val (likes) = createRefs()
                Text(text = review.authorUsername,)
                Likes(likes = review.likes, modifier = Modifier.constrainAs(likes) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

            }
        }
    }


}

@Composable
fun Likes(likes: Int, modifier: Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = likes.toString(), )
        Text(text = stringResource(id = R.string.likes))
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

package com.example.ratingsapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ratingsapp.R
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.example.ratingsapp.utils.ReviewProvider

@PreviewParameter(ReviewProvider::class)
@Composable
fun ReviewRow(
    review: Review,
    isDelete: Boolean = false,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val deleteWeight = if (isDelete) 0.85F else 1F //
    Row(
        modifier = Modifier
            .height(80.dp)
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically

    ) {
        Card(
            shape = RoundedCornerShape(10),
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.weight(deleteWeight)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
                val (stars, author, likes) = createRefs()
                Text(
                    text = review.title,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp)
                        .constrainAs(author) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(likes.start)
                            width = Dimension.fillToConstraints
                        },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left
                )
                Likes(likes = review.likes, modifier = Modifier.constrainAs(likes) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

                StaticRating(
                    score = review.reviewScore,
                    modifier = Modifier
                        .constrainAs(stars) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            top.linkTo(author.bottom)
                        }
                        .padding(start = 8.dp))

            }
        }
        if (isDelete) DeleteButton(onDeleteClick, Modifier.weight(1 - deleteWeight))
    }
}

/**
 * Stating rating bar that shows the review score in stars
 */
@Composable
fun StaticRating(score: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {

            val iconMod = Modifier
                .width(40.dp)
                .height(40.dp)
            if (i <= score) {
                Icon(
                    modifier = iconMod,
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = stringResource(id = R.string.star),
                    tint = Color.Black
                )
            } else {
                Icon(
                    modifier = iconMod,
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = stringResource(id = R.string.star),
                    tint = Color.LightGray
                )
            }
        }
    }
}

/**
 * Likes Container
 */
@Composable
fun Likes(likes: Int, modifier: Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = likes.toString(), style = MaterialTheme.typography.h5)
        Text(text = stringResource(id = R.string.likes), style = MaterialTheme.typography.body1)
    }
}

/**
 * Delete button
 */
@Composable
fun DeleteButton(onClick: () -> Unit, modifier: Modifier) {
    Box(modifier = modifier
        .height(40.dp)
        .width(40.dp)
        .clickable { onClick() })
    {
        Image(
            Icons.Rounded.Delete,
            contentDescription = stringResource(id = R.string.delete),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.error),
            modifier = Modifier.fillMaxSize()
        )

    }
}

fun mockClick() {}
fun mockDelete() {}

@Preview
@Composable
fun PreviewRow(@PreviewParameter(ReviewProvider::class) review: Review) {
    RatingsAppTheme {
        ReviewRow(review = review, false, { mockClick() }, { mockDelete() })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDeleteRow(@PreviewParameter(ReviewProvider::class) review: Review) {
    RatingsAppTheme {
        ReviewRow(review = review, true, { mockClick() }, { mockDelete() })
    }
}

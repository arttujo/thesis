package com.example.ratingsapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.ratingsapp.R
import com.example.ratingsapp.ReviewListProvider
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.ui.theme.RatingsAppTheme



@Composable
fun ReviewList(reviews: List<Review>, shownInProfile: Boolean, onRowClick: ()->Unit? = {}, onDeleteClick: ()-> Unit? = {}) {
    Column {
        if (shownInProfile) {
            Text(
                text = stringResource(id = R.string.my_reviews),
                modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
                style = MaterialTheme.typography.h3
            )
        }
        LazyColumn {
            items(reviews) { review ->
                ReviewRow(review = review, onClick = { onRowClick() }, onDeleteClick = { onDeleteClick()}, isDelete = shownInProfile)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMyReviewsComposable(@PreviewParameter(ReviewListProvider::class) reviews: List<Review>) {
    RatingsAppTheme() {
        ReviewList(reviews = reviews, true)
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewReviewsComposable(@PreviewParameter(ReviewListProvider::class) reviews: List<Review>) {
    RatingsAppTheme() {
        ReviewList(reviews = reviews, false)
    }
}



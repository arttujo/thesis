package com.example.ratingsapp.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.ratingsapp.models.Review

class ReviewProvider: PreviewParameterProvider<Review> {
    override val values: Sequence<Review> = sequenceOf(
        Review(1,1,1,"Test","Maija", "TEST",3, 5000),
        Review(1,1,1,"Test","Matti", "TEST",5, 213),
        Review(1,1,1,"Test","Very extremely long username that is most likely not likely", "TEST",5, 213)

    )
}

class ReviewListProvider: PreviewParameterProvider<List<Review>> {
    override val values: Sequence<List<Review>> = sequenceOf(
        listOf(
            Review(1,1,1,"Test","Maija", "TEST",3, 5000),
            Review(1,1,1,"Test","Matti", "TEST",5, 213),
            Review(1,1,1,"Test","Very extremely long username that is most likely not likely", "TEST",5, 213),
            Review(1,1,1,"Test","Maija", "TEST",3, 5000),
            Review(1,1,1,"Test","Matti", "TEST",5, 213),
            Review(1,1,1,"Test","Very extremely long username that is most likely not likely", "TEST",5, 213)
        )
    )
}




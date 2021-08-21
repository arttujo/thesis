package com.example.ratingsapp

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.ratingsapp.models.Review

class ReviewProvider: PreviewParameterProvider<Review> {
    override val values: Sequence<Review> = sequenceOf(
        Review(1,1,1,"Test","Matti", "TEST",4.0, 0)
    )
}




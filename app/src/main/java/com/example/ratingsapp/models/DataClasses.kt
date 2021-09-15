package com.example.ratingsapp.models

import com.google.gson.annotations.SerializedName
import java.lang.reflect.Array


/**
 * JSONServer Models
 */

data class Review(
    @SerializedName("id")
    val id: Int,
    @SerializedName("gameId")
    val gameId:Int,
    @SerializedName("authorId")
    val authorId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("authorUsername")
    val authorUsername: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("reviewScore")
    val reviewScore: Int,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("comments")
    val comments: List<Comment>? = emptyList()
    )

data class Game(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("studio")
    val studio: String,
    @SerializedName("imageLink")
    val imageLink: String,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("reviews")
    val reviews: List<Review> ? = emptyList()
)


data class Author(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("likedGames")
    val likedGames: List<Int>? = null,
    @SerializedName("likedComments")
    val likedComments: List<Int> ? = null,
    @SerializedName("likedReviews")
    val likedReviews: List<Int> ? = null,
    @SerializedName("reviews")
    val reviews: List<Review>
)

data class Comment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("reviewId")
    val reviewId: Int,
    @SerializedName("author")
    val author: String,
    @SerializedName("authorId")
    val authorId: Int,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("likes")
    val likes: Int
)

data class AuthorCreator(val username: String, val firstname: String, val lastname: String, val likedGames: List<Int> = emptyList(), val likedReviews: List<Int> = emptyList(), val likedComments: List<Int> = emptyList() )
data class CommentCreator(val reviewId: Int, val author: String, val authorId: Int, val comment: String, val likes: Int = 0)


/**
 * RAWG.IO Model for games
 */


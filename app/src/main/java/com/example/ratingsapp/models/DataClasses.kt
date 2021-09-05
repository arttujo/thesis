package com.example.ratingsapp.models

import com.google.gson.annotations.SerializedName


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
    val likes: Int )

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
    val likes: Int
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

data class AuthorCreator(val username: String, val firstname: String, val lastname: String)



/**
 * RAWG.IO Model for games
 */


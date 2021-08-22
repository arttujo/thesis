package com.example.ratingsapp.models


data class Review(
    val id: Int,
    val gameId:Int,
    val authorId: Int,
    val title: String,
    val authorUsername: String,
    val body: String,
    val reviewScore: Int,
    val likes: Int )

data class Game(
    val id: Int,
    val title: String,
    val studio: String,
    val imageLink: String,
    val likes: Int
)

data class Author(
    val id: Int,
    val username: String,
    val firstname: String,
    val lastname: String
)

data class Comment(
    val id: Int,
    val reviewId: Int,
    val author: String,
    val authorId: Int,
    val comment: String,
    val likes: Int
)
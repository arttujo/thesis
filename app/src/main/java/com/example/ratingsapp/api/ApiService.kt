package com.example.ratingsapp.api

import com.example.ratingsapp.models.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface JsonApiService {
    @GET("games")
    suspend fun getGames(): Response<List<Game>>

    @GET("authors")
    suspend fun getAuthors(): Response<List<Author>>


    // fake login -> try to fetch the user details
    @GET("authors/{id}")
    suspend fun login(@Path("id") authorId:Int): Response<Author>

    @POST("authors")
    suspend fun registerUser(@Body request: AuthorCreator): Response<Author>

    @GET("games/{id}")
    suspend fun getGameDetails(@Path("id") gameId: Int): Response<Game>

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") reviewId: Int): Response<Void>

    @GET("reviews/{id}")
    suspend fun getReviewDetails(@Path("id") reviewId: Int): Response<Review>

    @POST("comments")
    suspend fun newComment(@Body request: CommentCreator): Response<Comment>
}


object RetrofitBuilder {

    private const val BASE_URL = "http://foxerserver.asuscomm.com:3001/"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: JsonApiService = getRetrofit().create(JsonApiService::class.java)

}

class ApiHelper(private val apiService: JsonApiService) {
    suspend fun getGames() = apiService.getGames()
    suspend fun getGameDetails(gameId: Int) = apiService.getGameDetails(gameId)

    suspend fun getAuthors() = apiService.getAuthors()
    suspend fun login(id:Int) = apiService.login(id)

    suspend fun deleteReview(id: Int) = apiService.deleteReview(id)
    suspend fun getReviewDetails(id: Int) = apiService.getReviewDetails(id)

    suspend fun postAuthors(creator: AuthorCreator) = apiService.registerUser(creator)
    suspend fun newComment(creator: CommentCreator) = apiService.newComment(creator)
}
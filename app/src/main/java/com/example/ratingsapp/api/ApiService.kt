package com.example.ratingsapp.api

import com.example.ratingsapp.models.AuthorCreator
import com.example.ratingsapp.models.Game
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface JsonApiService {
    @GET("games")
    suspend fun getGames(): List<Game>

    @POST("authors")
    suspend fun registerUser(@Body request: AuthorCreator)


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
    suspend fun postAuthors(creator: AuthorCreator) = apiService.registerUser(creator)
}
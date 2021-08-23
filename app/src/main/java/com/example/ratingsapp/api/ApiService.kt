package com.example.ratingsapp.api

import com.example.ratingsapp.models.Game
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET



interface ApiService {
    @GET("games")
    suspend fun getGames(): List<Game>
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

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

}

class ApiHelper(private val apiService: ApiService) {
    suspend fun getGames() = apiService.getGames()
}
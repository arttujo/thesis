package com.example.ratingsapp.api

import com.example.ratingsapp.BuildConfig
import com.example.ratingsapp.models.RawgBaseResponse
import com.example.ratingsapp.models.RawgGameDetails
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {

    @GET("games")
    suspend fun getGames(@Query("search") search: String): Response<RawgBaseResponse>

    @GET("games/{id}")
    suspend fun getGameDetails(@Path("id") gameId: Int): Response<RawgGameDetails>

    @GET("{link}")
    suspend fun loadMore(@Path("link") link: String): Response<RawgBaseResponse>

}

object RawgBuilder {

    private const val BASE_URL = "https://api.rawg.io/api/"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client =
        OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor { chain ->
            // Add requited API Key to every request made to RAWG.IO
            val url =
                chain.request().url().newBuilder().addQueryParameter("key", BuildConfig.RAWG_APIKEY)
                    .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }.build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: RawgApiService = getRetrofit().create(RawgApiService::class.java)

}

class RawgApiHelper(private val rawgApiService: RawgApiService) {

    suspend fun getGames(search: String) = rawgApiService.getGames(search)

    suspend fun getGameDetails(id: Int) = rawgApiService.getGameDetails(id)

}
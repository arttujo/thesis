package com.example.ratingsapp.api

import com.example.ratingsapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface RawgApiService {

}


object RawgBuilder {

    private const val BASE_URL = "https://api.rawg.io/api/"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor { chain ->
        // Add requited API Key to every request made to RAWG.IO
        val url = chain.request().url().newBuilder().addQueryParameter("key", BuildConfig.RAWG_APIKEY).build()
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

class RawgApiHelper(private val RawgApiService: RawgApiService) {

}
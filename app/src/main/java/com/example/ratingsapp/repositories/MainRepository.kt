package com.example.ratingsapp.repositories

import com.example.ratingsapp.api.ApiHelper
import com.example.ratingsapp.models.AuthorCreator
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

data class ApiError(val code: Int, val message: String?)

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getGames(): Result<List<Game>> = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.getGames()
            if (response.isSuccessful) {
                val result = Result.Success(response.body()!!)
                return@withContext result
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e:Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }


    suspend fun postAuthors(creator: AuthorCreator) = apiHelper.postAuthors(creator)
}
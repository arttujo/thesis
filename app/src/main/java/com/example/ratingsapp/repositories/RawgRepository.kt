package com.example.ratingsapp.repositories

import com.example.ratingsapp.api.ApiHelper
import com.example.ratingsapp.api.RawgApiHelper
import com.example.ratingsapp.models.Game
import com.example.ratingsapp.models.RawgBaseResponse
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class RawgRepository(private val apiHelper: RawgApiHelper) {


    suspend fun getGames(search:String): Result<RawgBaseResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.getGames(search)
            if (response.isSuccessful) {
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }


    suspend fun getGameDetails(id:Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.getGameDetails(id)
            if (response.isSuccessful) {
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e:Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }




}
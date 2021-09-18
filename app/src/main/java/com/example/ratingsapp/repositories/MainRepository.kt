package com.example.ratingsapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ratingsapp.api.ApiHelper
import com.example.ratingsapp.models.*
import com.example.ratingsapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

data class ApiError(val code: Int, val message: String?)

/**
 * Very simple cache implementation
 */
interface Cache {
    val size: Int
    operator fun set(key: Any, value: Any)
    operator fun get(key: Any): Any?
    fun remove(key: Any): Any?
    fun clear()
}
const val AUTHOR_CACHE_KEY = "AUTHOR_CACHE_KEY"
const val GAME_CACHE_KEY = "GAME_CACHE_KEY"
const val LOGIN_CACHE = "LOGIN_CACHE_KEY"

class MainRepository(private val apiHelper: ApiHelper):Cache {
    private val cache = HashMap<Any, Any>()
    override val size: Int
        get() = cache.size

    override fun clear() = this.cache.clear()

    override fun get(key: Any) = this.cache[key]

    override fun remove(key: Any) = this.cache.remove(key)

    override fun set(key: Any, value: Any) {
        this.cache[key] = value
    }

    suspend fun getGames(): Result<List<Game>> = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.getGames()
            if (response.isSuccessful) {
                set(GAME_CACHE_KEY, response.body()!!)
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e:Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }

    suspend fun login(id:Int): Result<Author> = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.login(id)
            if (response.isSuccessful) {
                set(LOGIN_CACHE, response.body()!!)
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e:Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }


    suspend fun getAuthors(): Result<List<Author>> = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.getAuthors()
            if (response.isSuccessful) {
                set(AUTHOR_CACHE_KEY, response.body()!!)
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e:Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }


    suspend fun postAuthors(creator: AuthorCreator): Result<Author> = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.postAuthors(creator)
            if (response.isSuccessful) {
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }

    suspend fun deleteReview(id:Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.deleteReview(id)
            if (response.isSuccessful) {
                return@withContext Result.Success(null)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e:Exception) {
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

    suspend fun getReviewDetails(id:Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.getReviewDetails(id)
            if (response.isSuccessful) {
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e:Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }

    suspend fun newComment(creator: CommentCreator): Result<Comment> = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.newComment(creator)
            if (response.isSuccessful) {
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }

    suspend fun newReview(creator: ReviewCreator): Result<Review> = withContext(Dispatchers.IO) {
        try {
            val response = apiHelper.newReview(creator)
            if (response.isSuccessful) {
                return@withContext Result.Success(response.body()!!)
            } else {
                return@withContext Result.Error(ApiError(response.code(),response.message()))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(ApiError(-1, e.message ?: "Unknown Exception"))
        }
    }




}
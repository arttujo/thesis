package com.example.ratingsapp.databases

import androidx.lifecycle.liveData
import com.example.ratingsapp.models.AuthorCreator
import com.example.ratingsapp.repositories.MainRepository
import com.example.ratingsapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class AuthorDatabase(private val mainRepository: MainRepository) {

    fun postAuthor(creator: AuthorCreator) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.postAuthors(creator = creator)))
        } catch (e:Exception) {
            emit(Resource.error(null, e.message ?: "Failed to create user"))
        }
    }


}
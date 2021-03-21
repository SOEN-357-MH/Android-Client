package com.example.moviehub.repository

import com.example.moviehub.models.ResultBody
import com.example.moviehub.retrofit.MediaApiService
import com.example.moviehub.utils.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMediaRepository @Inject constructor(
    private val api: MediaApiService
): MediaRepository {

    override suspend fun getTrendingMoviesByPage(page: Int): Resource<ResultBody> {
        return try {
            val response = api.getTrendingMoviesByPage(page)
            if(response.isSuccessful){
                Resource.Success(response.body()!!, null)
            }else {
                Resource.Error(response.message())
            }
        } catch (e: Exception){
            Resource.Exception(e.message ?: "An error occurred")
        }
    }
}